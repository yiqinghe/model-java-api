package com.auto.model;

import com.auto.model.entity.Balance;
import com.auto.model.entity.Currency;
import com.auto.trade.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by gof on 18/6/24.
 */
public class ApiOcxTest {
    @Test
    public void getBalance() throws Exception {
    }

    @Test
    public void getDepthData() throws Exception {
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
    public void signedParams() throws Exception {

        Constants.APIKEY="xxx";
        Constants.SECRET="abc";
        Map<String,String> paras = new HashMap<>();
        paras.put("foo","bar");
        String sign = ApiOcx.signedParams("GET","/api/v2/markets",paras,123456789);
        Assert.assertTrue(sign.equals("access_key=xxx&foo=bar&tonce=123456789&signature=704f773b6b26772fd82bd3a8115079fb4f71d7baa1aad6b2922e99b17ed95cdc"));

    }

}