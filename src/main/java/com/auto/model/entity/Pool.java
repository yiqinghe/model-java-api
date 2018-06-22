package com.auto.model.entity;

import java.math.BigDecimal;

/**
 * Created by gof on 18/6/17.
 */
public class Pool {

    public TradeType tradeType;

    public TradeSymbol tradeSymbol;

    public BigDecimal amount;

    public Pool(TradeType tradeType, TradeSymbol tradeSymbol, BigDecimal amount) {
        this.tradeType = tradeType;
        this.tradeSymbol = tradeSymbol;
        this.amount = amount;
    }
}
