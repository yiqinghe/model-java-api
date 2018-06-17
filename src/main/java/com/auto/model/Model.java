package com.auto.model;

import com.auto.model.entity.DepthData;
import com.auto.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by caigaonian870 on 18/6/17.
 */
public class Model {

    private static final Logger log = LoggerFactory.getLogger(Model.class);


    public Api api;

    public TradeSymbol symbol;

    //量化周期开始A余额快照
    public Balance balanceAtStart_Target;
    //量化周期开始U余额快照
    public Balance balanceAtStart_Base;
    //量化周期结束A余额快照
    public Balance balanceAtEnd_Target;
    //量化周期结束U余额快照
    public Balance balanceAtEnd_Base;



    private Pool pool_Buy = new Pool(TradeType.buy, symbol,new BigDecimal(Config.tagetCurrencyPoolSize));
    private Pool pool_Sell = new Pool(TradeType.sell,symbol,new BigDecimal(Config.baseCurrencyPoolSize));

    // 	量化周期待买数组
    public List<Element> tradingList_Buy = new ArrayList<>();
    // 	量化周期待卖数组
    public List<Element> tradingList_Sell = new ArrayList<>();

    //本轮量化周期是否结束
    public boolean isFinish;

    private    ExecutorService executor = new ThreadPoolExecutor(6, 20,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());

    public Model(Api api, TradeSymbol symbol) {
        this.api = api;
        this.symbol=symbol;

    }

    /**
     * 初始化
     */
    public void init(){
        isFinish = false;
        tradingList_Buy.clear();;
        tradingList_Sell.clear();

        balanceAtStart_Target = api.getBalance(Currency.eth);
        balanceAtStart_Base = api.getBalance(Currency.usdt);

        BigDecimal each = pool_Buy.amount.divide(new BigDecimal(Config.quota));

        for(int i = 0;i< Config.quota;i++){
            Element element = new Element(i,symbol,TradeType.buy);
            element.targetAmount = each;
            tradingList_Buy.add(element);

            Element elementS = new Element(i,symbol,TradeType.sell);
            elementS.targetAmount = each;
            tradingList_Sell.add(elementS);
        }
    }
    /**
     * 周期开始 todo 根据一轮周期结束后的数据判断是否开启下一轮，或者自动调整参数
     */
    public QuantitativeResult periodStart() throws InterruptedException {
        init();

        for(int i =0;i<Config.concurrency;i++){
            Task task = new Task();
            task.start();
        }

        // 等待结束
        while(!isFinish){
            Thread.sleep(10);
        }
        balanceAtEnd_Target = api.getBalance(Currency.eth);
        balanceAtEnd_Base = api.getBalance(Currency.usdt);


        QuantitativeResult quantitativeResult =
                new QuantitativeResult(balanceAtStart_Target,balanceAtStart_Base,
                balanceAtEnd_Target,balanceAtEnd_Base,
                tradingList_Buy,tradingList_Sell);

        return quantitativeResult;
    }


    class Task extends Thread {
        private final Object listLockB = new Object();
        private final Object listLockS = new Object();

        private Order orderSell;
        private Order orderBuy;


        Element elementB = null;
        Element elementS = null;

        // 交易发起时间
        private long tradeTime;

        // 避免发单后，查询频率太快，至少等多少ms后查询
        private long queryIntervalMillsec=200;


        public void run() {
            while (true) {
                if (orderBuy != null || orderSell != null) {
                    // 查询订单状态
                    queryBuyOrderStatus();
                    querySellOrderStatus();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    DepthData depthData = api.getDepthData(symbol);
                    TradeContext tradeContext = api.buildTradeContext(depthData);
                    // 判断是否可以交易
                    if(tradeContext.canTrade){
                        int i = 0;
                        synchronized (listLockB) {
                            for (Element element : tradingList_Buy) {
                                if (element.tradeStatus == TradeStatus.init) {
                                    elementB = element;
                                    elementB.tradeStatus = TradeStatus.trading;
                                    synchronized (listLockS) {
                                        elementS = tradingList_Sell.get(i);
                                        elementS.tradeStatus = TradeStatus.trading;
                                    }
                                    break;
                                }
                                i++;
                            }
                        }

                        if(elementB == null | elementS == null){
                            // 全部交易对都已发出。本轮量化周期结束
                            log.warn("period finish {}");
                            isFinish = true;
                            break;
                        }

                        // 组装订单
                        orderBuy = new Order(symbol, TradeType.buy, tradeContext.buyPrice, elementB.targetAmount.toString());
                        orderSell = new Order(symbol, TradeType.buy, tradeContext.sellPrice, elementS.targetAmount.toString());

                        // 并发创建买单跟卖单
                        createOrder();


                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         *  发起交易，并发执行
         */
        public void createOrder(){
            tradeTime = System.currentTimeMillis();

            BuyTask buyTask = new BuyTask(api,orderBuy);
            SellTask sellTask = new SellTask(api,orderSell);

            FutureTask<Order> buyFuture = new FutureTask<Order>(buyTask) {
                // 异步任务执行完成，回调
                @Override
                protected void done() {
                    try {
                        System.out.println("buyFuture.done():" + get());
                    } catch (InterruptedException e) {
                        log.error("buyFuture exception {}",e);
                    } catch (ExecutionException e) {
                        log.error("buyFuture exception {}",e);
                    }
                }
            };
            FutureTask<Order> sellFuture = new FutureTask<Order>(sellTask) {
                // 异步任务执行完成，回调
                @Override
                protected void done() {
                    try {
                        System.out.println("sellFuture.done():" + get());
                    } catch (InterruptedException e) {
                        log.error("sellFuture exception {}",e);
                    } catch (ExecutionException e) {
                        log.error("sellFuture exception {}",e);
                    }
                }
            };


            executor.execute(buyFuture);
            executor.execute(sellFuture);

            try {
                // 阻塞获取返回结果
                orderBuy = buyFuture.get();
                orderSell = sellFuture.get();
            } catch (InterruptedException e) {
                log.error("buyFuture exception get {}",e);
            } catch (ExecutionException e) {
                log.error("sellFuture exception get {}",e);
            }
        }



        /**
         * 查询买订单状态
         */
        public void queryBuyOrderStatus(){
            long now = System.currentTimeMillis();
            if (orderBuy != null) {
                if(now > tradeTime + queryIntervalMillsec){
                    orderBuy = api.queryOrder(orderBuy);
                    if (orderBuy.tradeStatus == TradeStatus.done) {
                        // 更新list
                        synchronized (listLockB) {
                            elementB.tradeStatus=TradeStatus.done;
                            elementB.excutedAmount=new BigDecimal(orderBuy.excutedAmount);
                            orderBuy = null;
                            elementB = null;
                        }

                    }
                    else if (orderBuy.tradeStatus == TradeStatus.trading) {
                        // 超过最长等待时效
                        if (now > tradeTime + Config.waitOrderDoneMillSec) {
                            // 取消订单
                            if (!elementB.isCanceling) {
                                log.warn("cancel buy order");
                                api.cancel(orderBuy);
                                elementB.isCanceling = true;
                            }else{
                                log.warn("canceling buy order");
                            }
                        }
                    }
                }
            }
        }
        /**
         * 查询卖订单状态
         */
        public void querySellOrderStatus(){
            long now = System.currentTimeMillis();
            if (orderSell != null) {
                if(now > tradeTime + queryIntervalMillsec){
                    orderSell = api.queryOrder(orderSell);
                    if (orderSell.tradeStatus == TradeStatus.done) {
                        // 更新list
                        synchronized (listLockS) {
                            elementS.tradeStatus=TradeStatus.done;
                            elementS.excutedAmount=new BigDecimal(orderSell.excutedAmount);

                            orderSell = null;
                            elementS = null;

                        }
                    } else if (orderSell.tradeStatus == TradeStatus.trading) {
                        // 超过最长等待时效
                        if (now > tradeTime + Config.waitOrderDoneMillSec) {
                            // 取消订单
                            if (!elementS.isCanceling) {
                                log.warn("cancel sell order");
                                api.cancel(orderSell);
                                elementS.isCanceling = true;
                            }else{
                                log.warn("canceling sell order");
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * 购买异步任务
     */
     class BuyTask implements Callable<Order> {

        private Api api;
        private Order order;

        public BuyTask(Api api, Order order) {
            this.api = api;
            this.order=order;
        }

        // 返回异步任务的执行结果
        @Override
        public Order call() throws Exception {
            return api.buy(order);
        }
    }

    /**
     * 卖出异步任务
     */
    class SellTask implements Callable<Order> {

        private Api api;
        private Order order;

        public SellTask(Api api, Order order) {
            this.api = api;
            this.order=order;
        }

        // 返回异步任务的执行结果
        @Override
        public Order call() throws Exception {
            return api.sell(order);
        }
    }

}