package com.auto.trade.entity;

/**
 * Created by caigaonian870 on 18/6/18.
 *
 * 交易所
 */
public enum Exchange {
    binance("binance");

    private String exchange;

    Exchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExchange() {
        return exchange;
    }
}
