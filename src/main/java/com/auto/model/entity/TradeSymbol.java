package com.auto.model.entity;

/**
 * Created by caigaonian870 on 18/6/17.
 */
public class TradeSymbol {
     public Currency targetCurrency;
     public Currency baseCurrency;

    public TradeSymbol(Currency targetCurrency, Currency baseCurrency) {
        this.targetCurrency = targetCurrency;
        this.baseCurrency = baseCurrency;
    }

    @Override
    public String toString() {
        return "TradeSymbol{" +
                "targetCurrency=" + targetCurrency +
                ", baseCurrency=" + baseCurrency +
                '}';
    }
}
