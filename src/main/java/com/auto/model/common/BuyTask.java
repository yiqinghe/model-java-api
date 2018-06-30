package com.auto.model.common;

import com.auto.model.entity.Order;

import java.util.concurrent.Callable;

/**
 * Created by gof on 18/6/30.
 * 购买异步任务
 */
public class BuyTask implements Callable<Order> {

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
