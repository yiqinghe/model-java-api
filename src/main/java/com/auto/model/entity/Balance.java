package com.auto.model.entity;

/**
 * Created by gof on 18/6/17.
 */
public class Balance {

    public Currency currency;

    public String amount;

    public Balance(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "currency=" + currency +
                ", amount='" + amount + '\'' +
                '}';
    }
}
