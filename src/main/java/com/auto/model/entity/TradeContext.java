package com.auto.model.entity;

/**
 * Created by gof on 18/6/17.
 * 交易上下文，记录是否能发起交易，交易的价格
 */
public class TradeContext {
    // 是否能发起交易
    public boolean canTrade;
    // 发起交易的买单价格
    public String buyPrice;
    // 发起交易的卖单价格
    public String sellPrice;

    @Override
    public String toString() {
        return "TradeContext{" +
                "canTrade=" + canTrade +
                ", buyPrice='" + buyPrice + '\'' +
                ", sellPrice='" + sellPrice + '\'' +
                '}';
    }
}
