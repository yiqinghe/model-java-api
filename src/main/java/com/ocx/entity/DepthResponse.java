package com.ocx.entity;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private Data data ;

    public static class Data{
        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
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

        private long timestamp ;
        private List<String[]> asks ;
        private List<String[]> bids ;

    }

    public void testjson() throws Exception{
        String j = "{\"data\":{\"timestamp\":1529650749,\"asks\":[[\"0.07780500\",\"35.859079\"],[\"0.07768600\",\"0.000327\"],[\"0.07751829\",\"0.200000\"],[\"0.07751780\",\"0.200000\"],[\"0.07751757\",\"0.775346\"],[\"0.07750468\",\"0.048820\"],[\"0.07744800\",\"0.055622\"],[\"0.07739200\",\"15.451563\"],[\"0.07739100\",\"0.055622\"],[\"0.07733000\",\"7.128975\"],[\"0.07732700\",\"0.071981\"],[\"0.07731400\",\"0.071981\"],[\"0.07729700\",\"3.583626\"],[\"0.07729600\",\"1.266226\"],[\"0.07729500\",\"6.609578\"],[\"0.07727240\",\"1.000000\"],[\"0.07726584\",\"5.300000\"],[\"0.07725928\",\"16.302000\"],[\"0.07725708\",\"39.646543\"],[\"0.07725707\",\"2.000000\"]],\"bids\":[[\"0.07725706\",\"0.130000\"],[\"0.07725705\",\"5.810000\"],[\"0.07725704\",\"1.100000\"],[\"0.07725702\",\"15.081354\"],[\"0.07725701\",\"46.858000\"],[\"0.07725682\",\"2.754393\"],[\"0.07725516\",\"0.225566\"],[\"0.07725104\",\"14.084000\"],[\"0.07725100\",\"21.460000\"],[\"0.07725099\",\"55.846000\"],[\"0.07725092\",\"2.162000\"],[\"0.07724561\",\"2.000000\"],[\"0.07724559\",\"29.997291\"],[\"0.07724558\",\"22.758000\"],[\"0.07724556\",\"50.035496\"],[\"0.07724554\",\"50.000000\"],[\"0.07724550\",\"47.471508\"],[\"0.07724548\",\"19.393925\"],[\"0.07719679\",\"15.072000\"],[\"0.07719678\",\"7.000000\"]]}}" ;
        DepthResponse t = JSON.parseObject(j,DepthResponse.class);
        String[] ts = t.getData().getAsks().get(0);
        System.out.println(ts);
    }
}

