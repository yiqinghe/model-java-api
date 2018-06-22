package com.coinbene.entity;

import java.util.List;

/**
 * Created by caigaonian870 on 18/6/21.
 */
public class TickerResponse {

    private String status;
    // private List ticker;
    private long timestamp;

    private List<Ticker> ticker;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Ticker> getTicker() {
        return ticker;
    }

    public void setTicker(List<Ticker> ticker) {
        this.ticker = ticker;
    }

    @Override
    public String toString() {
        return "TickerResponse{" +
                "status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", ticker=" + ticker +
                '}';
    }

    public class Ticker{
        private  String ask;
        private  String bid;
        private  String last;
        private  String symbol;

        public String getAsk() {
            return ask;
        }

        public void setAsk(String ask) {
            this.ask = ask;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return "Ticker{" +
                    "ask='" + ask + '\'' +
                    ", bid='" + bid + '\'' +
                    ", last='" + last + '\'' +
                    ", symbol='" + symbol + '\'' +
                    '}';
        }
    }

}

