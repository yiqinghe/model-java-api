package com.auto.model;

import com.alibaba.fastjson.JSON;
import com.auto.model.entity.Config;
import com.coinbene.entity.BalanceResponse;
import com.coinbene.entity.TickerResponse;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by gof on 18/6/21.
 */
public class ApiCoinbeneTest {
    @Test
    public void getBalance() throws Exception {
        String response ="{\"account\":\"exchange\",         \"balance\":[         {           \"asset\":\"ACT\",      \"available\":\"999999.0000000000000000\",            \"reserved\":\"0.0000000000000000\",           \"total\":\"999999.0000000000000000\"     },       {            \"asset\":\"AE\",            \"available\":\"999999.0000000000000000\",            \"reserved\":\"0.0000000000000000\",            \"total\":\"999999.0000000000000000\"         }     ],         \"status\":\"ok\",         \"timestamp\":1517536673213}";
        BalanceResponse ticketResponse = JSON.parseObject( response,BalanceResponse.class);
        Assert.assertTrue(ticketResponse!=null && ticketResponse.getStatus().equals("ok"));
        Assert.assertTrue(ticketResponse!=null && ticketResponse.getBalance().size()==2);
    }

    @Test
    public void getDepthData() throws Exception {
        String response ="{'timestamp': 1529461186102, 'status': 'ok', " +
                "'ticker':[{'symbol': 'BTCUSDT', '24hrHigh': '6832.65', " +
                "'ask': '6603.77', '24hrVol': '5329.3321', " +
                "'24hrLow': '6547.0', 'bid': '6598.89', " +
                "'last': '6598.90', '24hrAmt': '35754346.714095'}]}";
        TickerResponse ticketResponse = JSON.parseObject( response,TickerResponse.class);
        Assert.assertTrue(ticketResponse!=null && ticketResponse.getStatus().equals("ok"));
        Assert.assertTrue(ticketResponse!=null && ticketResponse.getTicker().size()==1);
        Assert.assertTrue(ticketResponse!=null && ticketResponse.getTicker().get(0).getLast().equals("6598.90"));


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

        BigDecimal buy = new BigDecimal("0.234456789").setScale(Config.priceScale, RoundingMode.CEILING);

        BigDecimal increase = new BigDecimal("1")
                .divide(new BigDecimal(10).pow(Config.priceScale),Config.priceScale, RoundingMode.CEILING)
                .setScale(Config.priceScale, RoundingMode.CEILING);

        BigDecimal sell = buy.add(increase);

        Assert.assertTrue(sell.toString().equals("0.234458"));


        buy = new BigDecimal("0.234456").setScale(Config.priceScale, RoundingMode.CEILING);

        increase = new BigDecimal("1")
                .divide(new BigDecimal(10).pow(Config.priceScale),Config.priceScale, RoundingMode.CEILING)
                .setScale(Config.priceScale, RoundingMode.CEILING);

        sell = buy.add(increase);

        Assert.assertTrue(sell.toString().equals("0.234457"));

        buy = new BigDecimal("10000.23").setScale(2, RoundingMode.CEILING);

        increase = new BigDecimal("1")
                .divide(new BigDecimal(10).pow(2),Config.priceScale, RoundingMode.CEILING)
                .setScale(2, RoundingMode.CEILING);

        sell = buy.add(increase);

        Assert.assertTrue(sell.toString().equals("10000.24"));


        buy = new BigDecimal("10000.231").setScale(2, RoundingMode.CEILING);

        increase = new BigDecimal("1")
                .divide(new BigDecimal(10).pow(2),Config.priceScale, RoundingMode.CEILING)
                .setScale(2, RoundingMode.CEILING);

        sell = buy.add(increase);

        Assert.assertTrue(sell.toString().equals("10000.25"));


        buy = new BigDecimal("10000.23").setScale(2, RoundingMode.CEILING);

        increase = new BigDecimal("1")
                .divide(new BigDecimal(10).pow(1),2, RoundingMode.CEILING)
                .setScale(2, RoundingMode.CEILING);

        sell = buy.add(increase);

        Assert.assertTrue(sell.toString().equals("10000.33"));
    }

    @Test
    public void getTargetAmountForBuy() throws Exception {
    }

    @Test
    public void getTargetAmountForSell() throws Exception {
    }

    @Test
    public void getSymbol() throws Exception {
    }

    @Test
    public void getSymbol1() throws Exception {
    }

}