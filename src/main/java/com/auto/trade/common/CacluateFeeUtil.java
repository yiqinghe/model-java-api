package com.auto.trade.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by gof on 18/6/25.
 */
public class CacluateFeeUtil {

    static class Fee{
        public  BigDecimal caculateFee;
        public BigDecimal actualFee;

        @Override
        public String toString() {
            return "Fee{" +
                    "caculateFee=" + caculateFee +
                    ", actualFee=" + actualFee +
                    '}';
        }
    }
    public static Fee caculateFee(String excutedAmount,String price,String feeRate){
        Fee fee = new Fee();
        fee.caculateFee = new BigDecimal(excutedAmount)
                .multiply(new BigDecimal(price))
                .multiply(new BigDecimal(feeRate)).setScale(10, RoundingMode.UP);
        fee.actualFee = new BigDecimal(excutedAmount)
                .multiply(new BigDecimal(price))
                .multiply(new BigDecimal(feeRate)).setScale(6, RoundingMode.UP);
        return fee;
    }

}
