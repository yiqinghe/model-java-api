package com.ocx.entity;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

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

    public static class Data {
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

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
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

        private String high;
        private String last;
        private String market_code;
        private String open;
        private String volume;
        private int timestamp;
        private String sell;
        private String buy;

    }

    @Test
    public void testjson() throws Exception {
        String j = "{\"data\":[{\"low\":\"0.0404532\",\"high\":\"0.089\",\"last\":\"0.07764402\",\"market_code\":\"ethbtc\",\"open\":\"0.07958421\",\"volume\":\"617498.49640735\",\"sell\":\"0.07764403\",\"buy\":\"0.07764402\",\"timestamp\":1529657659},{\"low\":\"0.07218281\",\"high\":\"0.161818\",\"last\":\"0.12615507\",\"market_code\":\"bchbtc\",\"open\":\"0.1315612\",\"volume\":\"3970.22552001\",\"sell\":\"0.12674462\",\"buy\":\"0.12644332\",\"timestamp\":1529657659},{\"low\":\"0.000461\",\"high\":\"0.0147805\",\"last\":\"0.01000001\",\"market_code\":\"ltcbtc\",\"open\":\"0.01450001\",\"volume\":\"138654.24693465\",\"sell\":\"0.01371013\",\"buy\":\"0.01000001\",\"timestamp\":1529657659},{\"low\":\"0.00133455\",\"high\":\"0.001708\",\"last\":\"0.0015\",\"market_code\":\"eosbtc\",\"open\":\"0.001512\",\"volume\":\"10497.99398473\",\"sell\":\"0.0133455\",\"buy\":\"0.00133455\",\"timestamp\":1529657659},{\"low\":\"6007.0\",\"high\":\"7329.58\",\"last\":\"6432.23\",\"market_code\":\"btcusdt\",\"open\":\"6704.3883\",\"volume\":\"20638.4688786\",\"sell\":\"6432.2302\",\"buy\":\"6432.23\",\"timestamp\":1529657659},{\"low\":\"492.6565\",\"high\":\"544.4755\",\"last\":\"494.9205\",\"market_code\":\"ethusdt\",\"open\":\"533.2491\",\"volume\":\"629771.76190737\",\"sell\":\"494.9204\",\"buy\":\"494.9203\",\"timestamp\":1529657659},{\"low\":\"0.018\",\"high\":\"0.02529254\",\"last\":\"0.01928813\",\"market_code\":\"eoseth\",\"open\":\"0.02062077\",\"volume\":\"331621.98170312\",\"sell\":\"0.01936125\",\"buy\":\"0.01928812\",\"timestamp\":1529657659},{\"low\":\"0.000001\",\"high\":\"0.00005812\",\"last\":\"0.00003913\",\"market_code\":\"motbtc\",\"open\":\"0.00004627\",\"volume\":\"5408.17355211\",\"sell\":\"0.0000645\",\"buy\":\"0.00003265\",\"timestamp\":1529657659},{\"low\":\"0.0001666\",\"high\":\"0.000862\",\"last\":\"0.00060018\",\"market_code\":\"moteth\",\"open\":\"0.00057375\",\"volume\":\"17757133.56650356\",\"sell\":\"0.00060018\",\"buy\":\"0.00060005\",\"timestamp\":1529657659},{\"low\":\"0.00241855\",\"high\":\"0.00241855\",\"last\":\"0.00241855\",\"market_code\":\"etcbtc\",\"open\":\"0.00241855\",\"volume\":\"0.01\",\"sell\":\"0.0\",\"buy\":\"0.00241855\",\"timestamp\":1529657659},{\"low\":\"0.028\",\"high\":\"0.056\",\"last\":\"0.05\",\"market_code\":\"etceth\",\"open\":\"0.028583\",\"volume\":\"9.229199\",\"sell\":\"0.0\",\"buy\":\"0.028583\",\"timestamp\":1529657659},{\"low\":\"0.0001389\",\"high\":\"0.00025996\",\"last\":\"0.00019301\",\"market_code\":\"engbtc\",\"open\":\"0.00023\",\"volume\":\"306.68383578\",\"sell\":\"0.00023999\",\"buy\":\"0.00019301\",\"timestamp\":1529657659},{\"low\":\"0.00205243\",\"high\":\"0.0042684\",\"last\":\"0.00305403\",\"market_code\":\"engeth\",\"open\":\"0.00285252\",\"volume\":\"1241853.21067379\",\"sell\":\"0.00327998\",\"buy\":\"0.00305403\",\"timestamp\":1529657659},{\"low\":\"0.000001\",\"high\":\"0.000596\",\"last\":\"0.0000135\",\"market_code\":\"manabtc\",\"open\":\"0.0000153\",\"volume\":\"23701260.28454113\",\"sell\":\"0.00001399\",\"buy\":\"0.00000913\",\"timestamp\":1529657659},{\"low\":\"0.00006666\",\"high\":\"0.00025102\",\"last\":\"0.00017357\",\"market_code\":\"manaeth\",\"open\":\"0.00019036\",\"volume\":\"96127009.86126463\",\"sell\":\"0.0002\",\"buy\":\"0.00017357\",\"timestamp\":1529657659},{\"low\":\"0.00038927\",\"high\":\"0.00060025\",\"last\":\"0.00039968\",\"market_code\":\"gxsbtc\",\"open\":\"0.00042\",\"volume\":\"1488995.04555\",\"sell\":\"0.00041\",\"buy\":\"0.00038936\",\"timestamp\":1529657659},{\"low\":\"0.00500006\",\"high\":\"0.0061\",\"last\":\"0.0052006\",\"market_code\":\"gxseth\",\"open\":\"0.00527019\",\"volume\":\"769693.89392\",\"sell\":\"0.00542169\",\"buy\":\"0.0052006\",\"timestamp\":1529657659},{\"low\":\"0.00000441\",\"high\":\"0.00002\",\"last\":\"0.00000461\",\"market_code\":\"showeth\",\"open\":\"0.00000881\",\"volume\":\"1091925521.2265828\",\"sell\":\"0.00000463\",\"buy\":\"0.00000462\",\"timestamp\":1529657659},{\"low\":\"0.00000351\",\"high\":\"0.00000538\",\"last\":\"0.00000366\",\"market_code\":\"insureth\",\"open\":\"0.00000483\",\"volume\":\"137912426.64854984\",\"sell\":\"0.00000367\",\"buy\":\"0.00000365\",\"timestamp\":1529657659},{\"low\":\"0.00001645\",\"high\":\"0.000025\",\"last\":\"0.00001998\",\"market_code\":\"bwteth\",\"open\":\"0.00002003\",\"volume\":\"226341206.68671317\",\"sell\":\"0.00001999\",\"buy\":\"0.00001998\",\"timestamp\":1529657659},{\"low\":\"0.0000031\",\"high\":\"0.00005\",\"last\":\"0.000048\",\"market_code\":\"kkgeth\",\"open\":\"0.0000386\",\"volume\":\"165941981.61\",\"sell\":\"0.00005\",\"buy\":\"0.000048\",\"timestamp\":1529657659},{\"low\":\"0.0000275\",\"high\":\"0.0000488\",\"last\":\"0.00003015\",\"market_code\":\"sfceth\",\"open\":\"0.00003105\",\"volume\":\"312203623.61387991\",\"sell\":\"0.00003087\",\"buy\":\"0.00003015\",\"timestamp\":1529657659},{\"low\":\"0.000338\",\"high\":\"0.00063399\",\"last\":\"0.00034\",\"market_code\":\"rceth\",\"open\":\"0.00056199\",\"volume\":\"11974299.02460432\",\"sell\":\"0.00044\",\"buy\":\"0.00034\",\"timestamp\":1529657659},{\"low\":\"0.00000802\",\"high\":\"0.00001212\",\"last\":\"0.00000872\",\"market_code\":\"lveth\",\"open\":\"0.00001062\",\"volume\":\"135319716.05446986\",\"sell\":\"0.000009\",\"buy\":\"0.00000843\",\"timestamp\":1529657659},{\"low\":\"0.00085601\",\"high\":\"0.003\",\"last\":\"0.00087403\",\"market_code\":\"wicceth\",\"open\":\"0.00105343\",\"volume\":\"9585306.72213812\",\"sell\":\"0.00092801\",\"buy\":\"0.00085604\",\"timestamp\":1529657659},{\"low\":\"0.00018\",\"high\":\"0.000356\",\"last\":\"0.00025486\",\"market_code\":\"ocxeth\",\"open\":\"0.00028069\",\"volume\":\"1229229154.22997338\",\"sell\":\"0.00025542\",\"buy\":\"0.00025541\",\"timestamp\":1529657659},{\"low\":\"0.000015\",\"high\":\"0.00002869\",\"last\":\"0.00001998\",\"market_code\":\"ocxbtc\",\"open\":\"0.0000223\",\"volume\":\"41695702.52156884\",\"sell\":\"0.00001998\",\"buy\":\"0.00001983\",\"timestamp\":1529657659},{\"low\":\"0.088\",\"high\":\"0.1878\",\"last\":\"0.1272\",\"market_code\":\"ocxusdt\",\"open\":\"0.1501\",\"volume\":\"77250058.55769557\",\"sell\":\"0.1267\",\"buy\":\"0.1266\",\"timestamp\":1529657659},{\"low\":\"0.00023\",\"high\":\"0.00033064\",\"last\":\"0.00025694\",\"market_code\":\"pgdeth\",\"open\":\"0.00027144\",\"volume\":\"11618206.74\",\"sell\":\"0.00027284\",\"buy\":\"0.00025098\",\"timestamp\":1529657659},{\"low\":\"0.00000116\",\"high\":\"0.00000197\",\"last\":\"0.00000152\",\"market_code\":\"bwtbtc\",\"open\":\"0.0000016\",\"volume\":\"4646310.73\",\"sell\":\"0.00000157\",\"buy\":\"0.00000152\",\"timestamp\":1529657659},{\"low\":\"0.0000006\",\"high\":\"0.00000111\",\"last\":\"0.00000062\",\"market_code\":\"lvbtc\",\"open\":\"0.00000108\",\"volume\":\"1058155.69\",\"sell\":\"0.000001\",\"buy\":\"0.0000006\",\"timestamp\":1529657659},{\"low\":\"0.0000198\",\"high\":\"0.000027\",\"last\":\"0.00002006\",\"market_code\":\"pgdbtc\",\"open\":\"0.00002001\",\"volume\":\"75068.89\",\"sell\":\"0.000028\",\"buy\":\"0.000015\",\"timestamp\":1529657659},{\"low\":\"0.000049\",\"high\":\"0.00006992\",\"last\":\"0.00005004\",\"market_code\":\"tmteth\",\"open\":\"0.00005349\",\"volume\":\"1860093214.41953439\",\"sell\":\"0.00005012\",\"buy\":\"0.00005004\",\"timestamp\":1529657659},{\"low\":\"0.6217\",\"high\":\"1.2\",\"last\":\"0.812\",\"market_code\":\"ocxxcny\",\"open\":\"0.9765\",\"volume\":\"76781513.71031489\",\"sell\":\"0.81205\",\"buy\":\"0.812\",\"timestamp\":1529657659},{\"low\":\"3170.0\",\"high\":\"3600.0\",\"last\":\"3282.7088\",\"market_code\":\"ethxcny\",\"open\":\"3475.0\",\"volume\":\"91844.54866652\",\"sell\":\"3282.7088\",\"buy\":\"3271.29771\",\"timestamp\":1529657659},{\"low\":\"38250.0\",\"high\":\"48339.8438\",\"last\":\"42585.0\",\"market_code\":\"btcxcny\",\"open\":\"41760.0\",\"volume\":\"418.31599014\",\"sell\":\"42792.5\",\"buy\":\"42585.0\",\"timestamp\":1529657659},{\"low\":\"0.0000888\",\"high\":\"0.0008\",\"last\":\"0.00017268\",\"market_code\":\"zibeth\",\"open\":\"0.00017268\",\"volume\":\"2147167.14911993\",\"sell\":\"0.00017535\",\"buy\":\"0.00017268\",\"timestamp\":1529657659}]}";
        TickerResponse t = JSON.parseObject(j, TickerResponse.class);
        Data ts = t.getData().get(0);
        System.out.println(ts);
    }
}

