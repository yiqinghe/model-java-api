package com.auto.model.entity;

/**
 * Created by gof on 18/6/17.
 */
public enum Currency {
    eth("eth"),
    usdt("usdt"),
    bnb("bnb"),
    coni("coni"),
    xrp("xrp");

    private String currency;

    Currency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "currency='" + currency + '\'' +
                '}';
    }

    public static Currency getCurrencyByStr(String currency){
        for(Currency value: Currency.values()){
            if(value.getCurrency().equals(currency)){
                return value;
            }
        }
        return null;
    }
}
