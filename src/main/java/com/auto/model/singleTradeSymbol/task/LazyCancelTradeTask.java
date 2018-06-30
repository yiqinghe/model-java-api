package com.auto.model.singleTradeSymbol.task;

import com.auto.model.common.Api;
import com.auto.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by gof on 18/6/30.
 * 发单、初步查询任务
 */
public class LazyCancelTradeTask extends AbstractTradeTask {

    private static final Logger log = LoggerFactory.getLogger(LazyCancelTradeTask.class);

    public LazyCancelTradeTask(List<Element> tradingList_Buy, List<Element> tradingList_Sell, Api api,
                               Object listLockB, Object listLockS, TradeSymbol symbol,
                               ExecutorService executor){
        super(tradingList_Buy,tradingList_Sell, api,
                 listLockB,listLockS , symbol,
                 executor);
    }

    /**
     * 查询买订单状态
     */
    @Override
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
                        log.warn("add to cancel pool buy order *********");

                        // 将订单状态置为等待取消，waitCancel，纪录订单价格、放入取消池的时间

                        // 更新list
                        synchronized (listLockB) {
                            orderBuy.lastUpdateTime=System.currentTimeMillis();
                            elementB.setOrder(orderBuy);
                            elementB.tradeStatus=TradeStatus.waitCancel;
                            elementB.excutedAmount=new BigDecimal(orderBuy.excutedAmount);

                            // 留作判断是否需要反向操作
                            elementBRevert = elementB;

                            orderBuy = null;
                            elementB = null;

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
    @Override
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
                        log.warn("add to cancel pool sell order *********");
                        synchronized (listLockS) {
                            orderSell.lastUpdateTime=System.currentTimeMillis();
                            elementS.setOrder(orderSell);
                            elementS.tradeStatus=TradeStatus.waitCancel;
                            elementS.excutedAmount=new BigDecimal(orderSell.excutedAmount);

                            // 留作判断是否需要反向操作
                            elementSRevert = elementS;

                            orderSell = null;
                            elementS = null;

                        }
                    }

                }
            }
        }
        return orderSell;
    }
}
