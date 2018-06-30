package com.auto.model;

import com.alibaba.fastjson.JSON;
import com.auto.model.common.Api;
import com.auto.trade.common.Constants;
import com.fcoin.ApiFcoin;
import com.fcoin.entity.TickerResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gof on 18/6/27.
 */
public class ApiFcoinTest {
    @Test
    public void getBalance() throws Exception {
    }

    @Test
    public void getDepthData() throws Exception {
        String response ="{\n" +
                "  \"status\": 0,\n" +
                "  \"data\": {\n" +
                "    \"type\": \"ticker.btcusdt\",\n" +
                "    \"seq\": 680035,\n" +
                "    \"ticker\": [\n" +
                "      7140.890000000000000000,\n" +
                "      1.000000000000000000,\n" +
                "      7131.330000000,\n" +
                "      233.524600000,\n" +
                "      7140.890000000,\n" +
                "      225.495049866,\n" +
                "      7140.890000000,\n" +
                "      7140.890000000,\n" +
                "      7140.890000000,\n" +
                "      1.000000000,\n" +
                "      7140.890000000000000000\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        TickerResponse ticketResponse = JSON.parseObject(response, TickerResponse.class);
        Assert.assertTrue(ticketResponse!=null && ticketResponse.getStatus().equals("0"));
    }

    @Test
    public void getOrderPrice() throws Exception {
    }

    @Test
    public void buy() throws Exception {
    }

    @Test
    public void sell() throws Exception {
    }

    @Test
    public void buyMarket() throws Exception {
    }

    @Test
    public void sellMarket() throws Exception {
    }

    @Test
    public void cancel() throws Exception {
    }

    @Test
    public void queryOrder() throws Exception {
    }

    @Test
    public void buildTradeContext() throws Exception {
    }

    @Test
    public void getTargetAmountForBuy() throws Exception {
    }

    @Test
    public void getTargetAmountForSell() throws Exception {
    }

    @Test
    public void buildSign() throws Exception {
        String url ="https://api.fcoin.com/v2/orders";
        String http = "POST";
        Api api = new ApiFcoin();
        Map<String ,String> paras = new HashMap<>();
        paras.put("type","limit");
        paras.put("side","buy");
        paras.put("amount","100.0");
        paras.put("price","100.0");
        paras.put("symbol","btcusdt");
        Constants.SECRET="3600d0a74aa3410fb3b1996cca2419c8";
        String sign = api.buildSign(http,url,paras,Long.valueOf("1523069544359"));
        Assert.assertTrue(sign.equals("DeP6oftldIrys06uq3B7Lkh3a0U="));
    }


}