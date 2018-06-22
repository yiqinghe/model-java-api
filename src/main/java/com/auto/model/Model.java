package com.auto.model;

import com.auto.trade.entity.DepthData;
import com.auto.model.entity.*;
import com.auto.trade.entity.OrderPrice;
import com.binance.api.client.domain.TimeInForce;
import org.aspectj.weaver.ast.Or;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by gof on 18/6/17.
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
    public boolean init(){
        isFinish = false;
        tradingList_Buy.clear();;
        tradingList_Sell.clear();

        balanceAtStart_Target = api.getBalance(symbol.targetCurrency);
        balanceAtStart_Base = api.getBalance(symbol.baseCurrency);

        log.info("balanceAtStart_Target:{}",balanceAtStart_Target);
        log.info("balanceAtStart_Base:{}",balanceAtStart_Base);

        if(new BigDecimal(balanceAtStart_Target.amount).compareTo(new BigDecimal(Config.tagetCurrencyPoolSize )) < 0
                || new BigDecimal(balanceAtStart_Base.amount).compareTo(new BigDecimal(Config.baseCurrencyPoolSize )) < 0){
            log.error("init not enough balance");
            return false;
        }

        BigDecimal each = pool_Buy.amount.divide(new BigDecimal(Config.quota));

        for(int i = 0;i< Config.quota;i++){
            Element element = new Element(i,symbol,TradeType.buy);
            element.targetAmount = each;
            // 手续费抵扣
            element.targetAmount = api.getTargetAmountForBuy(element.targetAmount);
            tradingList_Buy.add(element);

            Element elementS = new Element(i,symbol,TradeType.sell);
            elementS.targetAmount = each;
            elementS.targetAmount = api.getTargetAmountForSell(elementS.targetAmount);
            tradingList_Sell.add(elementS);
        }
        return true;
    }
    /**
     * 周期开始 todo 根据一轮周期结束后的数据判断是否开启下一轮，或者自动调整参数
     */
    public QuantitativeResult periodStart() throws InterruptedException {
        while (true){
            try{
                boolean flag = init();
                if(flag){
                    break;
                }else{
                    log.warn("init fail >>>");
                    Thread.sleep(10000);
                }

            }catch (Exception e){
                log.warn("init fail {}",e);
                Thread.sleep(10000);
            }

        }

        if(tradingList_Buy.size()>0 && tradingList_Sell.size() >0){
            for(int i =0;i<Config.concurrency;i++){
                Task task = new Task();
                task.start();
            }

            // 等待结束
            while(!isFinish){
                Thread.sleep(10);
            }
        }
        balanceAtEnd_Target = api.getBalance(symbol.targetCurrency);
        balanceAtEnd_Base = api.getBalance(symbol.baseCurrency);


        QuantitativeResult quantitativeResult =
                new QuantitativeResult(balanceAtStart_Target,balanceAtStart_Base,
                balanceAtEnd_Target,balanceAtEnd_Base,
                tradingList_Buy,tradingList_Sell);

        return quantitativeResult;
    }


    // 测试钩子
    public Task taskForTestHook = new Task();

    class Task extends Thread {
        private final Object listLockB = new Object();
        private final Object listLockS = new Object();

        public Order orderSell;
        public Order orderBuy;


        public Element elementB = null;
        public Element elementS = null;

        // 反向操作使用
        public Element elementBRevert = null;
        public Element elementSRevert = null;

        // 交易发起时间
        public long tradeTime;

        // 避免发单后，查询频率太快，至少等多少ms后查询
        private long queryIntervalMillsec=200;

        // 测试钩子
        public Map<String,String> mapForTestHook = new HashMap<>();


        public void run() {
            int count =0;
            while (true) {
                try{
                    if (orderBuy != null || orderSell != null) {
                        // 查询订单状态
                        orderBuy = queryBuyOrderStatus();
                        orderSell = querySellOrderStatus();

                        try {
                            Thread.sleep(Config.querySleepMillSec);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        if(count%100==0){
                            log.info("excute {} times",count);
                        }
                        count++;
                        // fixme 判断是否需要立马发一个反向操作市场单
                        if(elementBRevert != null && elementSRevert !=null){
                            // revertOperate(elementBRevert,elementSRevert);
                            elementBRevert=null;
                            elementSRevert=null;
                        }

                        DepthData depthData = api.getDepthData(symbol);
                        DateTime dateTime = new DateTime(depthData.getEventTime());
                        DateTime now = new DateTime();
                        if(dateTime.isBefore(now.minusSeconds(Config.depthDataValideSecs))){
                            log.warn("{},{},depthData expire,time:{},now:{},data:{}",depthData.ask_1_price,depthData.bid_1_price,dateTime,now,depthData);
                            Thread.sleep(1000);
                            continue;
                        }

                        if(depthData == null || depthData.ask_1_price==null || depthData.bid_1_price==null){
                            log.warn("depthData is null,{}",depthData);
                            Thread.sleep(1000);
                            continue;
                        }
                        if(new BigDecimal(depthData.ask_1_price).compareTo(new BigDecimal(depthData.bid_1_price)) < 0){
                            log.error("depthData ask_1_price < bid_1_price,{}",depthData);
                            continue;
                        }
                        TradeContext tradeContext = api.buildTradeContext(depthData);

                        // 判断是否可以交易
                        if(tradeContext != null && tradeContext.canTrade){
                            int index = 0;
                            synchronized (listLockB) {
                                // 取出可以交易的交易对（状态init）
                                for (Element element : tradingList_Buy) {
                                    if (element.tradeStatus == TradeStatus.init) {
                                        elementB = element;
                                        elementB.tradeStatus = TradeStatus.trading;
                                        synchronized (listLockS) {
                                            elementS = tradingList_Sell.get(index);
                                            elementS.tradeStatus = TradeStatus.trading;
                                        }
                                        break;
                                    }
                                    index++;
                                }
                            }
                            log.info("tradeContext:{},index:{}",tradeContext,index);

                            if(elementB == null || elementS == null){
                                // 全部交易对都已发出。本轮量化周期结束
                                log.warn("period finish {}");
                                isFinish = true;
                                break;
                            }

                            // 组装订单
                            Order orderB = new Order(symbol, TradeType.buy, tradeContext.buyPrice, elementB.targetAmount.toString());
                            Order orderS = new Order(symbol, TradeType.sell, tradeContext.sellPrice, elementS.targetAmount.toString());

                            // 并发创建买单跟卖单
                            createOrder(orderB,orderS);


                        }
                    }
                    try {
                        Thread.sleep(Config.querySleepMillSec);
                    } catch (InterruptedException e) {
                        log.error("sleep InterruptedException  {}",e);
                    }
                }catch (Exception e){
                    log.error("while Exception  {}",e);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        }

        /**
         *  发起交易，并发执行
         */
        public void createOrder(Order orderB, Order orderS){
            log.info("createOrder,orderBuy:{},orderSell:{}",orderBuy,orderSell);
            tradeTime = System.currentTimeMillis();

            BuyTask buyTask = new BuyTask(api,orderB);
            SellTask sellTask = new SellTask(api,orderS);

            FutureTask<Order> buyFuture = new FutureTask<Order>(buyTask) {
                // 异步任务执行完成，回调
                @Override
                protected void done() {
                    try {
                        mapForTestHook.put("buyFuture.done","true");
                        //log.info("buyFuture.done():{}" , get());
                    } catch (Exception e) {
                        log.error("buyFuture exception {}",e);
                    }
                }
            };
            FutureTask<Order> sellFuture = new FutureTask<Order>(sellTask) {
                // 异步任务执行完成，回调
                @Override
                protected void done() {
                    try {
                        mapForTestHook.put("sellFuture.done","true");
                        //log.info("sellFuture.done():{}" , get());
                    } catch (Exception e) {
                        log.error("sellFuture exception {}",e);
                    }
                }
            };


            executor.submit(buyFuture);
            executor.submit(sellFuture);

            try {
                //log.info("before future get");

                // 阻塞获取返回结果
                orderBuy = buyFuture.get(10,TimeUnit.SECONDS);
                orderSell = sellFuture.get(10,TimeUnit.SECONDS);

                log.info("after future get >>>>>>>>");

            } catch (InterruptedException e) {
                log.error("Future InterruptedException get {}",e);
            } catch (ExecutionException e) {
                log.error("Future ExecutionException get {}",e);
            } catch (TimeoutException e) {
                log.error("Future TimeoutException get {}",e);
            }
        }

        /**
         * 反向操作，平衡资金池
         * @param elementBtmp
         * @param elementStmp
         */
        public void revertOperate(Element elementBtmp,Element elementStmp){

            BigDecimal tmpBamount = new BigDecimal(0);
            BigDecimal tmpSamount = new BigDecimal(0);

            if(elementBtmp.targetAmount !=null){
                tmpBamount = elementBtmp.excutedAmount;
            }
            if(elementStmp.targetAmount !=null){
                tmpSamount = elementStmp.excutedAmount;
            }
            BigDecimal diff = tmpBamount.subtract(tmpSamount);

            if(diff.compareTo(new BigDecimal(0)) > 0){
                log.warn("revertOperate sell:{},{},{}",diff,elementBtmp,elementStmp);
                // fixme 反向卖出市场单
                OrderPrice orderPrice =  api.getOrderPrice(symbol);
                Order order = new Order(symbol,TradeType.sell,null,diff.abs().toString());
                order.timeInForce= TimeInForce.IOC;
                order.price=orderPrice.price;
                log.info("sellMarketPrice:{}",orderPrice.price);
                api.sell(order);
            }
            if(diff.compareTo(new BigDecimal(0)) < 0){
                log.warn("revertOperate buy:{},{},{}",diff,elementBtmp,elementStmp);
                // fixme 反向买入市场单
                OrderPrice orderPrice =  api.getOrderPrice(symbol);
                Order order = new Order(symbol,TradeType.buy,null,diff.abs().toString());
                order.timeInForce= TimeInForce.IOC;
                order.price=orderPrice.price;
                log.info("buyMarketPrice:{}",orderPrice.price);
                api.buy(order);
            }
            if(diff.compareTo(new BigDecimal(0)) ==0){
                log.warn("revertOperate equal:{},{},{}",diff,elementBtmp,elementStmp);
            }
        }
        /**
         * 查询买订单状态
         */
        public Order queryBuyOrderStatus(){
            long now = System.currentTimeMillis();
            if (orderBuy != null) {
                if(orderBuy.orderId==null){
                    log.warn("orderBuy.orderId==null");
                    elementB.tradeStatus=TradeStatus.init;
                    elementB=null;
                    orderBuy = null;
                    return null;
                }
                if(now > tradeTime + queryIntervalMillsec){
                    orderBuy = api.queryOrder(orderBuy);
                    if (orderBuy.tradeStatus == TradeStatus.done) {
                        log.warn("query buy done");
                        // 更新list
                        synchronized (listLockB) {
                            elementB.tradeStatus=TradeStatus.done;
                            elementB.excutedAmount=new BigDecimal(orderBuy.excutedAmount);

                            // 留作判断是否需要反向操作
                            elementBRevert = elementB;

                            orderBuy = null;
                            elementB = null;

                        }

                    }
                    else if (orderBuy.tradeStatus == TradeStatus.trading) {
                        elementB.tradeStatus=TradeStatus.trading;
                        // 超过最长等待时效
                        if (now > tradeTime + Config.waitOrderDoneMillSec) {
                            // 取消订单
                            if (!elementB.isCanceling()) {
                                mapForTestHook.put("isCancelingForBuy","true");
                                log.warn("cancel buy order *********");
                                api.cancel(orderBuy);
                                elementB.setCanceling(true);
                            }else{
                                mapForTestHook.put("isCancelingForBuy","false");
                                log.warn("canceling buy order");
                            }
                        }
                    }
                }
            }
            return orderBuy;
        }
        /**
         * 查询卖订单状态
         */
        public Order querySellOrderStatus(){
            long now = System.currentTimeMillis();
            if (orderSell != null) {
                if(orderSell.orderId==null){
                    log.warn("orderSell.orderId==null");
                    elementS.tradeStatus=TradeStatus.init;
                    elementS=null;
                    orderSell = null;
                    return null;
                }
                if(now > tradeTime + queryIntervalMillsec){
                    orderSell = api.queryOrder(orderSell);
                    if (orderSell.tradeStatus == TradeStatus.done) {
                        log.warn("query sell done");
                        // 更新list
                        synchronized (listLockS) {
                            elementS.tradeStatus=TradeStatus.done;
                            elementS.excutedAmount=new BigDecimal(orderSell.excutedAmount);

                            // 留作判断是否需要反向操作
                            elementSRevert = elementS;

                            orderSell = null;
                            elementS = null;

                        }
                    } else if (orderSell.tradeStatus == TradeStatus.trading) {

                        elementS.tradeStatus=TradeStatus.trading;

                        // 超过最长等待时效
                        if (now > tradeTime + Config.waitOrderDoneMillSec) {
                            // 取消订单
                            if (!elementS.isCanceling()) {
                                mapForTestHook.put("isCancelingForSell","true");
                                log.warn("cancel sell order *********");
                                api.cancel(orderSell);
                                elementS.setCanceling(true);
                            }else{
                                mapForTestHook.put("isCancelingForSell","false");
                                log.warn("canceling sell order");
                            }
                        }

                    }
                }
            }
            return orderSell;
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
            //log.info("buytask call");
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
           // log.info("selltask call");
            return api.sell(order);
        }
    }

}