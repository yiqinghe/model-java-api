package com.auto.model.common;

import com.auto.model.common.Api;
import com.auto.model.entity.Order;
import com.auto.model.entity.TradeType;

import java.util.concurrent.Callable;

/**
 * Created by gof on 18/6/30.
 * 购买异步任务
 */
public class BuySellTask implements Callable<Order> {

    private Api api;
    private Order order;

    public BuySellTask(Api api, Order order) {
        this.api = api;
        this.order=order;
    }

    // 返回异步任务的执行结果
    @Override
    public Order call() throws Exception {
        if(order.tradeType==TradeType.buy){
            return api.buy(order);
        }
        if(order.tradeType==TradeType.sell){
            return api.sell(order);
        }
        return null;
    }
}
