package com.auto.trade.response;

/**
 * Created by gof on 18/2/10.
 */
public class AccountResponse {
    private String btc;
    private String eth;
    private String usdt;
    private String bnb;

    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public String getEth() {
        return eth;
    }

    public void setEth(String eth) {
        this.eth = eth;
    }

    public String getUsdt() {
        return usdt;
    }

    public void setUsdt(String usdt) {
        this.usdt = usdt;
    }

    public String getBnb() {
        return bnb;
    }

    public void setBnb(String bnb) {
        this.bnb = bnb;
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "btc='" + btc + '\'' +
                ", eth='" + eth + '\'' +
                ", usdt='" + usdt + '\'' +
                ", bnb='" + bnb + '\'' +
                '}';
    }
}
