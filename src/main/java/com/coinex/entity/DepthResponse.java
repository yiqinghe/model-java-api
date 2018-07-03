package com.coinex.entity;

import java.util.List;

public class DepthResponse {

    private String code;
    private String message;

    private Data data ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private String last ;
        private List<String[]> asks ;
        private List<String[]> bids ;

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public List<String[]> getAsks() {
            return asks;
        }

        public void setAsks(List<String[]> asks) {
            this.asks = asks;
        }

        public List<String[]> getBids() {
            return bids;
        }

        public void setBids(List<String[]> bids) {
            this.bids = bids;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "last='" + last + '\'' +
                    ", asks=" + asks +
                    ", bids=" + bids +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DepthResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

