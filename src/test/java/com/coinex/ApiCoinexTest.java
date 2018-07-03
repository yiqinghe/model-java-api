package com.coinex;

import com.auto.model.common.Api;
import com.auto.trade.common.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by caigaonian870 on 18/7/2.
 */
public class ApiCoinexTest {
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
    public void buildSign() throws Exception {
        Api api = new ApiCoinex();
        Map<String, String> params = new HashMap<>();
        params.put("access_id","4DA36FFC61334695A66F8D29020EB589");
        params.put("amount","1.0");
        params.put("market","BTCBCH");
        params.put("price","680");
        params.put("tonce","1513746038205");
        params.put("type","buy");
        Constants.SECRET="B51068CF10B34E7789C374AB932696A05E0A629BE7BFC62F";
        String sign = api.buildSign("","",params,System.currentTimeMillis());
        //Assert.assertTrue(sign.equals("C6F0DDA352101C2258F992A277397F4A"));
    }

}