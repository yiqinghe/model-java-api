package com.auto.model.entity;

import com.binance.api.client.domain.TimeInForce;

/**
 * Created by gof on 18/6/17.
 */
public class Order {

    public String orderId;

    public TradeSymbol symbol;

    public TradeType tradeType;

    public TradeStatus tradeStatus=TradeStatus.init;

    public String price;

    public String amount;

    public TimeInForce timeInForce=TimeInForce.GTC;

    // 实际成交数量
    public String excutedAmount;

    public long lastUpdateTime;

    public Order(TradeSymbol symbol, TradeType tradeType, String price, String amount) {
        this.symbol = symbol;
        this.tradeType = tradeType;
        this.price = price;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", symbol=" + symbol +
                ", tradeType=" + tradeType +
                ", tradeStatus=" + tradeStatus +
                ", price='" + price + '\'' +
                ", amount='" + amount + '\'' +
                ", excutedAmount='" + excutedAmount + '\'' +
                '}';
    }
}
