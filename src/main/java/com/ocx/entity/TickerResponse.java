package com.ocx.entity;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * {
 * "data": [{
 * "low": "0.051",
 * "high": "0.0537",
 * "last" : "0.053",
 * "market_code" : "ethbtc",
 * "open" : "0.0517",
 * "volume" : "454.3",
 * "timestamp" : 1529275425,
 * "sell": "0.07914108",
 * "buy": "0.07902933",
 * }]
 * }
 */
public class TickerResponse {


    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    private List<Data> data;

    @Override
    public String toString() {
        return "TickerResponse{" +
                "data=" + data +
                '}';
    }

    public static class Data {
        private String high;
        private String last;
        private String market_code;
        private String open;
        private String volume;
        private long timestamp;
        private String sell;
        private String buy;

        private String low;

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getMarket_code() {
            return market_code;
        }

        public void setMarket_code(String market_code) {
            this.market_code = market_code;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getVolume() {
            return volume;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getSell() {
            return sell;
        }

        public void setSell(String sell) {
            this.sell = sell;
        }

        public String getBuy() {
            return buy;
        }

        public void setBuy(String buy) {
            this.buy = buy;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "high='" + high + '\'' +
                    ", last='" + last + '\'' +
                    ", market_code='" + market_code + '\'' +
                    ", open='" + open + '\'' +
                    ", volume='" + volume + '\'' +
                    ", timestamp=" + timestamp +
                    ", sell='" + sell + '\'' +
                    ", buy='" + buy + '\'' +
                    ", low='" + low + '\'' +
                    '}';
        }
    }
}

