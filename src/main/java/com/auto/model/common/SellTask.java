package com.auto.model.common;

import com.auto.model.entity.Order;

import java.util.concurrent.Callable;

/**
 * Created by gof on 18/6/
 * 卖出异步任务
 */
public class SellTask implements Callable<Order> {

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
