package com.auto.model.entity;

import java.math.BigDecimal;

/**
 * Created by caigaonian870 on 18/6/17.
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
}
