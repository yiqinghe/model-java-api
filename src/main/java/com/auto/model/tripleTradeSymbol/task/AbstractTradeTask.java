package com.auto.model.tripleTradeSymbol.task;

import com.auto.model.common.*;
import com.auto.model.common.BuySellTask;
import com.auto.model.entity.*;
import com.auto.trade.entity.DepthData;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by gof on 18/6/30.
 * 发单、初步查询任务
 */
public abstract class AbstractTradeTask extends Thread implements AbstractTask {

    private static final Logger log = LoggerFactory.getLogger(AbstractTradeTask.class);

    protected TradeSymbol symbolA;
    protected TradeSymbol symbolB;
    protected TradeSymbol symbolC;

    protected Order orderA;
    protected Order orderB;
    protected Order orderC;

    // 交易发起时间
    protected long tradeTime;

    // 避免发单后，查询频率太快，至少等多少ms后查询
    protected long queryIntervalMillsec=200;


    //任务是否结束
    protected boolean isFinish=false;


    protected List<Element> tradingList_A;
    protected List<Element> tradingList_B;
    protected List<Element> tradingList_C;
    protected Api api;
    protected Object listLockA;
    protected Object listLockB;
    protected Object listLockC;

    protected ExecutorService executor;

    public AbstractTradeTask(List<Element> tradingList_A, List<Element> tradingList_B,List<Element> tradingList_C, Api api,
                             Object listLockA, Object listLockB, Object listLockC,TradeSymbol symbolA,TradeSymbol symbolB,TradeSymbol symbolC,
                             ExecutorService executor){
        this.tradingList_A=tradingList_A;
        this.tradingList_B=tradingList_B;
        this.tradingList_C=tradingList_C;
        this.api=api;
        this.listLockA=listLockA;
        this.listLockB=listLockB;
        this.listLockC=listLockC;
        this.symbolA=symbolA;
        this.symbolB=symbolB;
        this.symbolC=symbolC;
        this.executor=executor;
    }

    @Override
    public void run() {
        int count =0;
        while (true) {
            try{
                if (orderA != null|| orderB != null||orderC != null ) {
                    // 查询订单状态
                    orderA = queryOrderStatus(orderA,listLockA);
                    orderB = queryOrderStatus(orderB,listLockB);
                    orderC = queryOrderStatus(orderC,listLockC);

                    try {
                        Thread.sleep(Config.querySleepMillSec);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    if(count%100==0){
                        log.info("excute {} times",count);
                    }

                    // todo 并发执行
                    DepthDataAll depthDataAll = queryDepthDataAll(symbolA,symbolB,symbolC);

                    if (!validateDepthData(depthDataAll.depthDataA)){
                        continue;
                    }if (!validateDepthData(depthDataAll.depthDataB)){
                        continue;
                    }if (!validateDepthData(depthDataAll.depthDataC)){
                        continue;
                    }

                    TradeContext tradeContext = buildTradeContext(depthDataAll.depthDataA,
                            depthDataAll.depthDataB,depthDataAll.depthDataC);
                    if(tradeContext!=null && tradeContext.canTrade){
                        // 并发创建订单
                        createOrder(tradeContext.orderA,tradeContext.orderB,tradeContext.orderC);
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
            }finally {
                count++;
            }

        }
    }



    class DepthDataAll{
        public DepthData depthDataA;
        public DepthData depthDataB;
        public DepthData depthDataC;
    }

    private DepthDataAll queryDepthDataAll(TradeSymbol symbolA,TradeSymbol symbolB,TradeSymbol symbolC){

        DepthDataAll depthDataAll = new DepthDataAll();
        QueryDepthTask aTask = new QueryDepthTask(api,symbolA);
        QueryDepthTask bTask = new QueryDepthTask(api,symbolB);
        QueryDepthTask cTask = new QueryDepthTask(api,symbolC);

        FutureTask<DepthData> aFuture = new FutureTask<DepthData>(aTask) {
            // 异步任务执行完成，回调
            @Override
            protected void done() {
                try {
                    //log.info("aFuture.done():{}" , get());
                } catch (Exception e) {
                    log.error("aFuture exception {}",e);
                }
            }
        };
        FutureTask<DepthData> bFuture = new FutureTask<DepthData>(bTask) {
            // 异步任务执行完成，回调
            @Override
            protected void done() {
                try {
                    //log.info("bFuture.done():{}" , get());
                } catch (Exception e) {
                    log.error("bFuture exception {}",e);
                }
            }
        };
        FutureTask<DepthData> cFuture = new FutureTask<DepthData>(cTask) {
            // 异步任务执行完成，回调
            @Override
            protected void done() {
                try {
                    //log.info("cFuture.done():{}" , get());
                } catch (Exception e) {
                    log.error("cFuture exception {}",e);
                }
            }
        };


        executor.submit(aFuture);
        executor.submit(bFuture);
        executor.submit(cFuture);

        try {
            //log.info("before future get");

            // 阻塞获取返回结果 fixme // 任务里面的睡眠时间不能超过future的超时时间
            depthDataAll.depthDataA = aFuture.get(20, TimeUnit.SECONDS);
            depthDataAll.depthDataB = bFuture.get(20,TimeUnit.SECONDS);
            depthDataAll.depthDataC = cFuture.get(20,TimeUnit.SECONDS);

//            //log.info("after future get depthDataA:{},depthDataB{},depthDataC{}>>>>>>>>",
//                    depthDataAll.depthDataA,depthDataAll.depthDataB,depthDataAll.depthDataC);

        } catch (InterruptedException e) {
            log.error("Future InterruptedException get {}",e);
        } catch (ExecutionException e) {
            log.error("Future ExecutionException get {}",e);
        } catch (TimeoutException e) {
            log.error("Future TimeoutException get {}",e);
        }

        return depthDataAll;
    }

    private boolean validateDepthData(DepthData depthDataA) throws InterruptedException {
        if(depthDataA==null){
            log.warn("depthDataA is null");
            Thread.sleep(1000);
            return false;
        }
        DateTime dateTime = new DateTime(depthDataA.getEventTime());
        DateTime now = new DateTime();
        if(dateTime.isBefore(now.minusSeconds(Config.depthDataValideSecs))){
            log.warn("{},{},dateTime expire,time:{},now:{},data:{}",depthDataA.ask_1_price,depthDataA.bid_1_price,dateTime,now,depthDataA);
            Thread.sleep(1000);
            return false;
        }

        if(depthDataA == null || depthDataA.ask_1_price==null || depthDataA.bid_1_price==null){
            log.warn("depthData is null,{}",depthDataA);
            Thread.sleep(1000);
            return false;
        }
        if(new BigDecimal(depthDataA.ask_1_price).compareTo(new BigDecimal(depthDataA.bid_1_price)) < 0){
            log.error("depthData ask_1_price < bid_1_price,{}",depthDataA);
            return false;
        }
        return true;
    }

    /**
     *  发起交易，并发执行
     */
    public void createOrder(Order orderA, Order orderB,Order orderC){
        log.info("createOrder,orderA:,orderA:orderC");
        tradeTime = System.currentTimeMillis();

        BuySellTask aTask = new BuySellTask(api,orderA);
        BuySellTask bTask = new BuySellTask(api,orderB);
        BuySellTask cTask = new BuySellTask(api,orderC);

        FutureTask<Order> aFuture = new FutureTask<Order>(aTask) {
            // 异步任务执行完成，回调
            @Override
            protected void done() {
                try {
                    //log.info("aFuture.done():{}" , get());
                } catch (Exception e) {
                    log.error("aFuture exception {}",e);
                }
            }
        };
        FutureTask<Order> bFuture = new FutureTask<Order>(bTask) {
            // 异步任务执行完成，回调
            @Override
            protected void done() {
                try {
                    //log.info("bFuture.done():{}" , get());
                } catch (Exception e) {
                    log.error("bFuture exception {}",e);
                }
            }
        };
        FutureTask<Order> cFuture = new FutureTask<Order>(cTask) {
            // 异步任务执行完成，回调
            @Override
            protected void done() {
                try {
                    //log.info("cFuture.done():{}" , get());
                } catch (Exception e) {
                    log.error("cFuture exception {}",e);
                }
            }
        };


        executor.submit(aFuture);
        executor.submit(bFuture);
        executor.submit(cFuture);

        try {
            //log.info("before future get");

            // 阻塞获取返回结果 fixme // 任务里面的睡眠时间不能超过future的超时时间
            orderA = aFuture.get(20, TimeUnit.SECONDS);
            orderB = bFuture.get(20,TimeUnit.SECONDS);
            orderC = cFuture.get(20,TimeUnit.SECONDS);

            log.info("after future get orderB:{},orderA{},orderC{}>>>>>>>>",orderB,orderA,orderC);

        } catch (InterruptedException e) {
            log.error("Future InterruptedException get {}",e);
        } catch (ExecutionException e) {
            log.error("Future ExecutionException get {}",e);
        } catch (TimeoutException e) {
            log.error("Future TimeoutException get {}",e);
        }
    }

    /**
     * 查询订单状态
     */
    abstract Order queryOrderStatus(Order order,Object listLock);
    /**
     * 查询C订单状态
     */
    abstract TradeContext buildTradeContext(DepthData depthDataA,DepthData depthDataB,DepthData depthDataC);

    @Override
    public boolean isFinish() {
        return isFinish;
    }

}
