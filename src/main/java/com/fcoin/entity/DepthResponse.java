package com.fcoin.entity;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * {
 *     "data":{
 *         "timestamp":1529650749,
 *         "asks":[
 *             [
 *                 "0.07780500",
 *                 "35.859079"
 *             ],
 *             [
 *                 "0.07725707",
 *                 "2.000000"
 *             ]
 *         ],
 *         "bids":[
 *             [
 *                 "0.07725706",
 *                 "0.130000"
 *             ],
 *             [
 *                 "0.07719678",
 *                 "7.000000"
 *             ]
 *         ]
 *     }
 * }
 *
 */
public class DepthResponse {

    private String status;

    private Data data ;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Data{
        private long ts ;
        private long seq ;
        private String[] asks ;
        private String[] bids ;

        public long getTs() {
            return ts;
        }

        public void setTs(long ts) {
            this.ts = ts;
        }

        public long getSeq() {
            return seq;
        }

        public void setSeq(long seq) {
            this.seq = seq;
        }

        public String[] getAsks() {
            return asks;
        }

        public void setAsks(String[] asks) {
            this.asks = asks;
        }

        public String[] getBids() {
            return bids;
        }

        public void setBids(String[] bids) {
            this.bids = bids;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "ts=" + ts +
                    ", seq=" + seq +
                    ", asks=" + asks +
                    ", bids=" + bids +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DepthResponse{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}

