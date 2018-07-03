package com.auto.model.tripleTradeSymbol.task;

import com.auto.model.common.Api;
import com.auto.model.entity.*;
import com.auto.model.tripleTradeSymbol.task.AbstractTradeTask;
import com.auto.trade.entity.DepthData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by gof on 18/6/30.
 * 异步延时取消策略
 */
public abstract  class LazyCancelTradeTask extends AbstractTradeTask {

    private static final Logger log = LoggerFactory.getLogger(LazyCancelTradeTask.class);

    public LazyCancelTradeTask(List<Element> tradingList_A, List<Element> tradingList_B,List<Element> tradingList_C, Api api,
                               Object listLockA, Object listLockB, Object listLockC,TradeSymbol symbolA,TradeSymbol symbolB,TradeSymbol symbolC,
                               ExecutorService executor){
        super( tradingList_A, tradingList_B,tradingList_C,  api,
                 listLockA,  listLockB,  listLockC, symbolA, symbolB, symbolC,
                 executor);
    }

    @Override
    public String getTaskName() {
        return "lazyCancelTradeTask";
    }

    @Override
    Order queryOrderStatus(Order order,Object listLock) {
        long now = System.currentTimeMillis();
        Order orderReturn = null;
        if (order != null) {
            if(order.orderId==null){
                log.warn("order.orderId==null");
                // fixme 状态往终态走，或者下一个状态走，不要往回走。
                order.element.tradeStatus=TradeStatus.done;
                return null;
            }
            if(now > tradeTime + queryIntervalMillsec){
                orderReturn = api.queryOrder(order);
                if (order.tradeStatus == TradeStatus.done) {
                    log.warn("query buy done");
                    // 更新list
                    synchronized (listLock) {
                        order.element.tradeStatus=TradeStatus.done;
                        order.element.excutedAmount=new BigDecimal(order.excutedAmount);
                        orderReturn = null;
                    }

                }
                else if (order.tradeStatus == TradeStatus.trading) {
                    order.element.tradeStatus=TradeStatus.trading;
                    // 超过最长等待时效
                    if (now > tradeTime + Config.waitOrderDoneMillSec) {
                        log.warn("add to cancel pool buy order *********");

                        // 将订单状态置为等待取消，waitCancel，纪录订单价格、放入取消池的时间

                        // 更新list
                        synchronized (listLock) {
                            order.lastUpdateTime=System.currentTimeMillis();
                            order.element.setOrder(order);
                            order.element.tradeStatus=TradeStatus.waitCancel;
                            order.element.excutedAmount=new BigDecimal(order.excutedAmount);
                            orderReturn = null;

                        }
                    }
                }
            }
        }
        return orderReturn;
    }
}
