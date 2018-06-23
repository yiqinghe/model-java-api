package com.coinbene;

import com.alibaba.fastjson.JSON;
import com.auto.model.entity.TradeType;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by gof on 18/6/21.
 */
public class UtilTest {


    @Test
    public void testScal() throws Exception {
        BigDecimal amount = new BigDecimal(100.01);
        String str = amount.setScale(2, RoundingMode.HALF_UP).toString();
        Assert.assertTrue(str.equals("100.01"));

        amount = new BigDecimal(100.06);
        str = amount.setScale(2, RoundingMode.HALF_UP).toString();
        Assert.assertTrue(str.equals("100.06"));

        amount = new BigDecimal(100.15);
        str = amount.setScale(2, RoundingMode.HALF_UP).toString();
        Assert.assertTrue(str.equals("100.15"));

        amount = new BigDecimal(100.06);
        str = amount.setScale(0, RoundingMode.HALF_UP).toString();
        Assert.assertTrue(str.equals("100"));

    }
    @Test
    public void testScal2() throws Exception {
        String price = new BigDecimal("0.076896")
                .multiply(new BigDecimal(1).subtract(new BigDecimal(0.00001))).setScale(6, RoundingMode.CEILING).toString();

        Assert.assertTrue(price.equals("0.076896"));
    }
    @Test
    public void testJson() throws Exception {
        String json = "{\"data\":{\"asks\":[[\"0.8\",\"0.9\"]]}}";
        DataResponse data = JSON.parseObject(json,DataResponse.class);
        String[] tickers = data.getData().getAsks().get(0);
        Assert.assertTrue(tickers[0].equals("0.8"));
        Assert.assertTrue(tickers[1].equals("0.9"));

    }

    static class DataResponse{
        private Data data;

        static class Data{
            private List<String[]> asks;

            public List<String[]> getAsks() {
                return asks;
            }

            public void setAsks(List<String[]> asks) {
                this.asks = asks;
            }
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "DataResponse{" +
                    "data=" + data +
                    '}';
        }
    }

    @Test
    public void buildMd5Sign() throws Exception {
        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid","ecc31028073bfdd44a75cd42d12e8201");
        paras.put("secret","783b66ca8b774e4d8ca59f4f3aadbc93");
        paras.put("timestamp",11223112231L);
        paras.put("account","exchange");
        String signStr = Util.buildMd5Sign(paras);
        System.out.println(signStr);
        Assert.assertTrue(signStr.equals("ACCOUNT=EXCHANGE&APIID=ECC31028073BFDD44A75CD42D12E8201&SECRET=783B66CA8B774E4D8CA59F4F3AADBC93&TIMESTAMP=11223112231"));

    }

    @Test
    public void string2MD5() throws Exception {
        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid","ecc31028073bfdd44a75cd42d12e8201");
        paras.put("secret","783b66ca8b774e4d8ca59f4f3aadbc93");
        paras.put("timestamp",11223112231L);
        paras.put("account","exchange");
        String signStr = Util.buildMd5Sign(paras);
        System.out.println(signStr);

        String sign = Util.string2MD5(signStr);
        System.out.println("sign:{}"+sign);
        Assert.assertTrue(sign.equals("4ef3198c37a9702c1dd847dbbcdd764a"));

    }

    @Test
    public void doGetRequest(){
//        Util.doGetRequest(Api.market_url+"ticker?symbol=btcusdt");
    }

    @Test
    public void buildPostJson(){
        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid","ecc31028073bfdd44a75cd42d12e8201");
        paras.put("secret","783b66ca8b774e4d8ca59f4f3aadbc93");
        paras.put("timestamp",11223112231L);
        paras.put("account","exchange");
        String json = Util.buildPostJson(paras);
        System.out.println("json:{}"+json);

    }

}