package com.auto.model.entity;

import java.math.BigDecimal;

/**
 * Created by gof on 18/6/17.
 */
public class Element {

    public int elementId;

    public TradeSymbol tradeSymbol;

    public TradeType tradeType;

    public TradeStatus tradeStatus=TradeStatus.init;

    // 目标成交数量
    public BigDecimal targetAmount;
    // 实际成交数量
    public BigDecimal excutedAmount;

    // 是否已经发起订单取消请求
    private boolean isCanceling;

    // 延时取消订单模型里面会用到
    private Order order;

    public boolean isCanceling() {
        return isCanceling;
    }

    public void setCanceling(boolean canceling) {
        isCanceling = canceling;
    }

    public Element(int elementId, TradeSymbol tradeSymbol, TradeType tradeType) {
        this.elementId = elementId;
        this.tradeSymbol = tradeSymbol;
        this.tradeType = tradeType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Element{" +
                "elementId=" + elementId +
                ", tradeSymbol=" + tradeSymbol +
                ", tradeType=" + tradeType +
                ", tradeStatus=" + tradeStatus +
                ", targetAmount=" + targetAmount +
                ", excutedAmount=" + excutedAmount +
                ", isCanceling=" + isCanceling +
                '}';
    }
}
