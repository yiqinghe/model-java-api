package com.auto.trade.common;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

/**
 * Created by gof on 18/6/25.
 */
public class CacluateFeeUtilTest {

    @Test
    public void caculateFee(){
        BigDecimal fee = new BigDecimal("0.02")
                .multiply(new BigDecimal("0.073645"))
                .multiply(new BigDecimal("0.001"));
        BigDecimal fee2=fee.setScale(8, RoundingMode.UP);
        Assert.assertTrue(fee2.toString().equals("0.00000148"));

        BigDecimal fee3=fee.setScale(6, RoundingMode.UP);
        Assert.assertTrue(fee3.toString().equals("0.000002"));
    }
}