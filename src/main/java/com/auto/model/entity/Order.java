package com.auto.model.entity;

/**
 * Created by caigaonian870 on 18/6/17.
 */
public class Order {

    public String orderId;

    public TradeSymbol symbol;

    public TradeType tradeType;

    public TradeStatus tradeStatus=TradeStatus.init;

    public String price;

    public String amount;

    // 实际成交数量
    public String excutedAmount;

    public Order(TradeSymbol symbol, TradeType tradeType, String price, String amount) {
        this.symbol = symbol;
        this.tradeType = tradeType;
        this.price = price;
        this.amount = amount;
    }
}
