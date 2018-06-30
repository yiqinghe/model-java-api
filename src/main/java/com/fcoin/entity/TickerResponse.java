package com.fcoin.entity;

import java.util.Arrays;

public class TickerResponse {
    private  String status;
    private  Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private  String type;
        private  long seq;
        private String[] ticker;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getSeq() {
            return seq;
        }

        public void setSeq(long seq) {
            this.seq = seq;
        }

        public String[] getTicker() {
            return ticker;
        }

        public void setTicker(String[] ticker) {
            this.ticker = ticker;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "type='" + type + '\'' +
                    ", seq=" + seq +
                    ", ticker=" + Arrays.toString(ticker) +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TickerResponse{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}

