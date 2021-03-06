package com.auto.model;

import com.auto.model.common.Api;
import com.auto.model.entity.Config;
import com.binance.ApiBinance;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by gof on 18/6/19.
 */
public class ApiBinanceTest {
    @Test
    public void getTargetAmountForBuy() throws Exception {
        Config.baseFeeRate=new BigDecimal("0.0005");
        Api api = new ApiBinance();
        BigDecimal originalTargetAmount = new BigDecimal("1");
        BigDecimal targetAmount = api.getTargetAmountForBuy(originalTargetAmount);
        Assert.assertTrue(targetAmount.compareTo(new BigDecimal("1"))==0);


    }

    @Test
    public void getTargetAmountForSell() throws Exception {
        Config.baseFeeRate=new BigDecimal("0.0005");
        Api api = new ApiBinance();
        BigDecimal originalTargetAmount = new BigDecimal("1");
        BigDecimal targetAmount = api.getTargetAmountForSell(originalTargetAmount);
        Assert.assertTrue(targetAmount.compareTo(new BigDecimal("1"))==0);
    }

}