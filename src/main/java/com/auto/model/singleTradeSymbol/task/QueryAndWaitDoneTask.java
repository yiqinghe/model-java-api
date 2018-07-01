package com.auto.model.singleTradeSymbol.task;

import com.auto.model.common.Api;
import com.auto.model.common.Util;
import com.auto.model.entity.Config;
import com.auto.model.entity.Element;
import com.auto.model.entity.Order;
import com.auto.model.entity.TradeStatus;
import com.auto.model.common.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * 专有查询订单任务，查询取消中的订单，直至done
 */
public  class QueryAndWaitDoneTask extends Thread implements AbstractTask {
    private static final Logger log = LoggerFactory.getLogger(QueryAndWaitDoneTask.class);


    //任务是否结束
    private boolean isFinish=false;

    private List<Element> tradingList_Buy;
    private List<Element> tradingList_Sell;
    private Api api;
    private Object listLockB;
    private Object listLockS;

    public QueryAndWaitDoneTask(List<Element> tradingList_Buy,List<Element> tradingList_Sell,Api api,
                                Object listLockB,Object listLockS ){
        this.tradingList_Buy=tradingList_Buy;
        this.tradingList_Sell=tradingList_Sell;
        this.api=api;
        this.listLockB=listLockB;
        this.listLockS=listLockS;
    }

    @Override
    public void run() {
        while (true){
            try{
                int buyDoneCount =0;
                int sellDoneCount =0;
                for (Element element : tradingList_Buy) {
                    if (element.tradeStatus == TradeStatus.done) {
                        buyDoneCount++;
                    }
                    // fixme 是否要降低锁粒度
                    if (element.tradeStatus == TradeStatus.canceling) {
                        Order order = api.queryOrder(element.getOrder());
                        if (order.tradeStatus == TradeStatus.done) {
                            log.warn("QueryTask buy done ,order:{}",order.orderId);                                // 更新list
                            synchronized (listLockB) {
                                element.tradeStatus=TradeStatus.done;
                                element.excutedAmount=new BigDecimal(order.excutedAmount);

                            }

                        }
                    }
                }
                for (Element element : tradingList_Sell) {
                    if (element.tradeStatus == TradeStatus.done) {
                        sellDoneCount++;
                    }
                    if (element.tradeStatus == TradeStatus.canceling) {
                        Order order = api.queryOrder(element.getOrder());
                        if (order.tradeStatus == TradeStatus.done) {
                            log.warn("QueryTask query sell done ,order:{}",order.orderId);
                            // 更新list
                            synchronized (listLockB) {
                                element.tradeStatus=TradeStatus.done;
                                element.excutedAmount=new BigDecimal(order.excutedAmount);

                            }

                        }
                    }
                }
                if(buyDoneCount==tradingList_Buy.size() && sellDoneCount==tradingList_Sell.size()){
                    log.warn("QueryTask isfinish >>>>>>>>>>>>>>>>>>>>>>>>>");
                    isFinish = true;
                    break;
                }
                Util.threadSleep(Config.querySleepMillSec);

            }catch (Exception e){
                log.error("QueryTask exception,{}",e);
                Util.threadSleep(10000);
            }
        }
    }

    @Override
    public boolean isFinish() {
        return isFinish;
    }
    @Override
    public String getTaskName() {
        return "queryTask";
    }
}
