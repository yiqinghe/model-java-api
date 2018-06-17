package com.auto.model.entity;

/**
 * Created by caigaonian870 on 18/6/17.
 */
public enum Currency {
    eth("eth"),
    usdt("usdt");

    private String currency;

    Currency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
