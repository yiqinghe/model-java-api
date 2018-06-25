package com.ocx.entity;

import com.alibaba.fastjson.JSON;

import java.util.List;


public class BalanceResponse {


    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BalanceResponse{" +
                "data=" + data +
                '}';
    }

    private List<Data> data;

    public static class Data {
        private String currency_code;
        private String balance;
        private String locked;

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getLocked() {
            return locked;
        }

        public void setLocked(String locked) {
            this.locked = locked;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "currency_code='" + currency_code + '\'' +
                    ", balance='" + balance + '\'' +
                    ", locked='" + locked + '\'' +
                    '}';
        }
    }
}

