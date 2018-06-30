package com.auto.model.singleTradeSymbol.task;

import com.auto.model.common.Api;
import com.auto.model.common.BuyTask;
import com.auto.model.common.SellTask;
import com.auto.model.entity.*;
import com.auto.model.common.AbstractTask;
import com.auto.trade.entity.DepthData;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by gof on 18/6/30.
 * 发单、初步查询任务
 */
public abstract class AbstractTradeTask extends Thread implements AbstractTask {

    private static final Logger log = LoggerFactory.getLogger(AbstractTradeTask.class);

    public TradeSymbol symbol;

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
    protected long queryIntervalMillsec=200;

    // 测试钩子
    public Map<String,String> mapForTestHook = new HashMap<>();


    //任务是否结束
    protected boolean isFinish=false;


    protected List<Element> tradingList_Buy;
    protected List<Element> tradingList_Sell;
    protected Api api;
    protected Object listLockB;
    protected Object listLockS;

    protected ExecutorService executor;

    public AbstractTradeTask(List<Element> tradingList_Buy, List<Element> tradingList_Sell, Api api,
                             Object listLockB, Object listLockS, TradeSymbol symbol,
                             ExecutorService executor){
        this.tradingList_Buy=tradingList_Buy;
        this.tradingList_Sell=tradingList_Sell;
        this.api=api;
        this.listLockB=listLockB;
        this.listLockS=listLockS;
        this.symbol=symbol;
        this.executor=executor;
    }

    @Override
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
        log.info("createOrder,orderBuy:,orderSell:");
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

            // 阻塞获取返回结果 fixme // 任务里面的睡眠时间不能超过future的超时时间
            orderBuy = buyFuture.get(20, TimeUnit.SECONDS);
            orderSell = sellFuture.get(20,TimeUnit.SECONDS);

            log.info("after future get orderBuy:{},orderSell{}>>>>>>>>",orderBuy,orderSell);

        } catch (InterruptedException e) {
            log.error("Future InterruptedException get {}",e);
        } catch (ExecutionException e) {
            log.error("Future ExecutionException get {}",e);
        } catch (TimeoutException e) {
            log.error("Future TimeoutException get {}",e);
        }
    }

    /**
     * 查询买订单状态
     */
    abstract Order queryBuyOrderStatus();
    /**
     * 查询卖订单状态
     */
    abstract Order querySellOrderStatus();

    @Override
    public boolean isFinish() {
        return isFinish;
    }

}
