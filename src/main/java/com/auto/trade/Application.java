package com.auto.trade;


import com.auto.model.ApiCoinbene;
import com.auto.model.Model;
import com.auto.model.entity.*;
import com.auto.trade.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;

@SpringBootApplication
@EnableScheduling
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);


    public  static boolean isListenerProcess = true;
    public  static boolean isFlushProcess = false;

    public static TradeSymbol tradeSymbol;
    public static Currency targetCurrency;
    public static Currency baseCurrency;
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        // 处理参数
        processParameters(args);

        tradeSymbol = new TradeSymbol(targetCurrency,baseCurrency);
        Config.symbol = tradeSymbol;
        Model model = new Model(new ApiCoinbene(),tradeSymbol);

        Config.totalFeeRate =caculateFreeRateFromLossRate(Config.baseFeeRate,Config.maxLossRate,new BigDecimal(1).subtract(Config.maxLossRate));
        // 公式

        int times =0;
        while(true){
            try {
                log.info("start model.periodStart,times:{}",times);
                QuantitativeResult quantitativeResult = model.periodStart();
                times++;

                double increaseBase = Double.valueOf(quantitativeResult.balanceAtEnd_Base.amount)
                        - Double.valueOf(quantitativeResult.balanceAtStart_Base.amount);
                double increaseBaseRate = increaseBase/Double.valueOf(quantitativeResult.balanceAtStart_Base.amount);

                double increaseTarget = Double.valueOf(quantitativeResult.balanceAtEnd_Target.amount)
                        - Double.valueOf(quantitativeResult.balanceAtStart_Target.amount);
                double increaseTargetRate = increaseTarget/Double.valueOf(quantitativeResult.balanceAtStart_Target.amount);
                log.info("quantitativeResult:"+quantitativeResult);

                log.info("increaseBase:"+increaseBase);
                log.info("increaseBaseRate:"+increaseBaseRate);
                log.info("increaseTarget:"+increaseTarget);
                log.info("increaseTargetRate:"+increaseTargetRate);

                Application.Result result =caculate(quantitativeResult);
                log.info("result:"+result.toString());


                if(result.lossRate.compareTo(new BigDecimal(0))==0 && result.successRate.compareTo(new BigDecimal(0))==0){
                    continue;
                }
                if(result.lossRate.compareTo(Config.maxLossRate) > 0 || result.successRate.compareTo(Config.minSuccessRate) <0){
                    log.info("not match,lossRate:{},successRate:{}",result.lossRate,result.successRate);
                    // todo 自动调整参数
                    break;
                }
                if(increaseTarget < 0 && Math.abs(increaseTargetRate) > 0.01){
                    log.info("taget lost:{},rate:{}",increaseTarget,increaseTargetRate);
                    break;
                }

                if(increaseBase < 0 && Math.abs(increaseBaseRate) > 0.01){
                    log.info("base lost:{},rate:{}",increaseBase,increaseBaseRate);
                    break;
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param baseFreeRate 基础交易费率
     * @param lossRate
     * @return
     */
    public static BigDecimal caculateFreeRateFromLossRate(BigDecimal baseFreeRate,BigDecimal lossRate,BigDecimal successRate){
        // loss后，反向操作也是需要手续费
        BigDecimal lossFreeRate = baseFreeRate.multiply(new BigDecimal(2)).multiply(lossRate).divide(successRate,6,BigDecimal.ROUND_CEILING);
        BigDecimal totalFreeRate = baseFreeRate.multiply(new BigDecimal(2)).add(lossFreeRate);
        log.info("totalFreeRate:"+totalFreeRate);

        return totalFreeRate;

    }

    public static Result caculate(QuantitativeResult quantitativeResult){
        int index = 0;
        //
        BigDecimal allAmount= new BigDecimal(0);
        // 成对成交数量
        BigDecimal pairDone= new BigDecimal(0);
        // 完全成对成交数量
        BigDecimal totalPairDone= new BigDecimal(0);
        // 部分成对成交数量
        BigDecimal partPairDone= new BigDecimal(0);
        // 每一对没有成交的额度总和,取绝对值
        BigDecimal diff= new BigDecimal(0);
        // 买－卖
        BigDecimal diffGloble= new BigDecimal(0);

        int totalSuccessCount = 0;
        int partSuccessCount = 0;
        for(Element elementB:quantitativeResult.tradingList_Buy){
            Element elementS=quantitativeResult.tradingList_Sell.get(index);
            if(elementB.excutedAmount.compareTo(elementB.targetAmount)==0&&
                    elementS.excutedAmount.compareTo(elementS.targetAmount)==0){

                pairDone = pairDone.add(elementB.excutedAmount);
                totalPairDone = totalPairDone.add(elementB.excutedAmount);
                totalSuccessCount++;

            }else if(elementB.excutedAmount.compareTo(elementS.excutedAmount) == 0){

                pairDone = pairDone.add(elementB.excutedAmount);
                partPairDone = partPairDone.add(elementB.excutedAmount);
                partSuccessCount++;

            }else if(elementB.excutedAmount.compareTo(elementS.excutedAmount) != 0){
                BigDecimal tmpDiff = elementB.excutedAmount.subtract(elementS.excutedAmount);

                diffGloble = diffGloble.add(tmpDiff);

                diff = diff.add(tmpDiff.abs());
                partPairDone = partPairDone.add(elementB.excutedAmount.compareTo(elementS.excutedAmount)<0?elementB.excutedAmount:elementS.excutedAmount);
                pairDone = pairDone.add(elementB.excutedAmount.compareTo(elementS.excutedAmount)<0?elementB.excutedAmount:elementS.excutedAmount);
                partSuccessCount++;

            }else{
                log.error("error else");
            }
            allAmount = allAmount.add(elementB.targetAmount);
            index++;
        }
        // loss率
        BigDecimal lossRate = diff.abs().divide(allAmount,6,BigDecimal.ROUND_CEILING);
        // 成对率
        BigDecimal successRate = pairDone.divide(allAmount,6,BigDecimal.ROUND_CEILING);

        // 资金池平衡损失率
        BigDecimal amountLostRate =  diffGloble.divide(allAmount,6,BigDecimal.ROUND_CEILING);

        log.info("lossRate:"+lossRate.toString());
        log.info("successRate:"+successRate.toString());
        log.info("amountLostRate:"+amountLostRate.toString());

        Result result = new Result( allAmount,  pairDone,  totalPairDone,  partPairDone,  diff,  lossRate,  successRate,amountLostRate);

        return result;
    }

    public static class Result{
        public BigDecimal allAmount;
        // 成对成交数量
        public BigDecimal pairDone;
        // 完全成对成交数量
        public BigDecimal totalPairDone;
        // 部分成对成交数量
        public  BigDecimal partPairDone;
        // 买－卖
        public BigDecimal diff;
        // loss率
        public BigDecimal lossRate;
        // 成对率
        public BigDecimal successRate;

        public BigDecimal diffGloble;

        public Result(BigDecimal allAmount, BigDecimal pairDone, BigDecimal totalPairDone, BigDecimal partPairDone, BigDecimal diff, BigDecimal lossRate, BigDecimal successRate,BigDecimal diffGloble) {
            this.allAmount = allAmount;
            this.pairDone = pairDone;
            this.totalPairDone = totalPairDone;
            this.partPairDone = partPairDone;
            this.diff = diff;
            this.lossRate = lossRate;
            this.successRate = successRate;
            this.diffGloble=diffGloble;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "allAmount=" + allAmount +
                    ", pairDone=" + pairDone +
                    ", totalPairDone=" + totalPairDone +
                    ", partPairDone=" + partPairDone +
                    ", diff=" + diff +
                    ", lossRate=" + lossRate +
                    ", successRate=" + successRate +
                    ", diffGloble=" + diffGloble +
                    '}';
        }
    }

    private static void processParameters(String[] args) {
        log.info("args:{}",args);
        for (String arg : args) {
            String[] argsStr = arg.split("=");

            if (argsStr.length == 2) {
                if ("isListenerProcess".equals(argsStr[0])) {
                    isListenerProcess = Boolean.valueOf(argsStr[1]);
                }
                if ("isFlushProcess".equals(argsStr[0])) {
                    isFlushProcess = Boolean.valueOf(argsStr[1]);
                }
                if ("APIKEY".equals(argsStr[0])) {
                    Constants.APIKEY = String.valueOf(argsStr[1]);
                }
                if ("SECRET".equals(argsStr[0])) {
                    Constants.SECRET = String.valueOf(argsStr[1]);
                }
                if ("quota".equals(argsStr[0])) {
                    Config.quota = Integer.valueOf(argsStr[1]);
                }
                if ("tagetCurrencyPoolSize".equals(argsStr[0])) {
                    Config.tagetCurrencyPoolSize = String.valueOf(argsStr[1]);
                }
                if ("baseCurrencyPoolSize".equals(argsStr[0])) {
                    Config.baseCurrencyPoolSize = String.valueOf(argsStr[1]);
                }
                if ("concurrency".equals(argsStr[0])) {
                    Config.concurrency = Integer.valueOf(argsStr[1]);
                }
                if ("waitOrderDoneMillSec".equals(argsStr[0])) {
                    Config.waitOrderDoneMillSec = Integer.valueOf(argsStr[1]);
                }
                if ("querySleepMillSec".equals(argsStr[0])) {
                    Config.querySleepMillSec = Integer.valueOf(argsStr[1]);
                }
                if ("depthDataValideSecs".equals(argsStr[0])) {
                    Config.depthDataValideSecs = Integer.valueOf(argsStr[1]);
                }
                if ("baseFeeRate".equals(argsStr[0])) {
                    Config.baseFeeRate = new BigDecimal(argsStr[1]);
                }
                if ("maxLossRate".equals(argsStr[0])) {
                    Config.maxLossRate = new BigDecimal(argsStr[1]);
                }
                if ("minSuccessRate".equals(argsStr[0])) {
                    Config.minSuccessRate = new BigDecimal(argsStr[1]);
                }
                if ("targetCurrency".equals(argsStr[0])) {
                    targetCurrency = Currency.getCurrencyByStr(argsStr[1]);
                }
                if ("baseCurrency".equals(argsStr[0])) {
                    baseCurrency = Currency.getCurrencyByStr(argsStr[1]);
                }
            }
        }
    }
}

