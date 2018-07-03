package com.coinex.entity;

import java.util.List;

/**
 * Created by gof on 18/6/21.
 */
public class BalanceResponse {

    private  String status;
    private List<Balance> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Balance> getData() {
        return data;
    }

    public void setData(List<Balance> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BalanceResponse{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }

    public class Balance{
        private  String currency;
        private  String available;
        private  String frozen;
        private  String balance;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getAvailable() {
            return available;
        }

        public void setAvailable(String available) {
            this.available = available;
        }

        public String getFrozen() {
            return frozen;
        }

        public void setFrozen(String frozen) {
            this.frozen = frozen;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        @Override
        public String toString() {
            return "Balance{" +
                    "currency='" + currency + '\'' +
                    ", available='" + available + '\'' +
                    ", frozen='" + frozen + '\'' +
                    ", balance='" + balance + '\'' +
                    '}';
        }
    }
}
