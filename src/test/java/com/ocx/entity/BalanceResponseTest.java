package com.ocx.entity;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gof on 18/6/24.
 */
public class BalanceResponseTest {
    @Test
    public void testJson() throws Exception {
        String j = "{\"data\":[{\"currency_code\":\"btc\",\"balance\":\"0.089\",\"locked\":\"0.07764402\"},{\"currency_code\":\"btc\",\"balance\":\"0.189\",\"locked\":\"0.07764402\"}]}";
        BalanceResponse t = JSON.parseObject(j, BalanceResponse.class);
        BalanceResponse.Data ts = t.getData().get(0);
        BalanceResponse.Data ts1 = t.getData().get(1);
        Assert.assertTrue(ts.getBalance().equals("0.089"));
        Assert.assertTrue(ts1.getBalance().equals("0.189"));

    }

}