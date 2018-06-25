package com.auto.trade;


import com.auto.model.Api;
import com.auto.model.ApiCoinbene;
import com.auto.model.ApiOcx;
import com.auto.model.Model;
import com.auto.model.entity.*;
import com.auto.trade.common.Constants;
import com.auto.trade.entity.OrderPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootApplication
@EnableScheduling
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);


    public  static boolean isListenerProcess = true;
    public  static boolean isFlushProcess = false;

    public static TradeSymbol tradeSymbol;
    public static Currency targetCurrency;
    public static Currency baseCurrency;

    // 本金损失率
    public static Double baseAmoutLostRate= 0.1;
    public static Double targetAmoutLostRate=0.1;

    // 平衡资金池最大失败次数
    public static int balanceMaxFailTimes=3;
    public static int balanceFailTimes=0;

    public static String exchange="coinbene";


    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(Application.class, args);
        // 处理参数
        processParameters(args);

        tradeSymbol = new TradeSymbol(targetCurrency,baseCurrency);
        Config.symbol = tradeSymbol;
        Api api = null;
        if(exchange.equals("coinbene")){
            api =new ApiCoinbene();
        }
        if(exchange.equals("ocx")){
            api =new ApiOcx();
        }
        Model model = new Model(api,tradeSymbol);

        Config.totalFeeRate =caculateFreeRateFromLossRate(Config.baseFeeRate,Config.maxLossRate,new BigDecimal(1).subtract(Config.maxLossRate));
        // 公式

        int times =0;
        while(true){
            try {
                log.info("start model.periodStart,times:{}",times);
                long startTime = System.currentTimeMillis();
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

                //Thread.sleep(10000);
                Application.Result result =caculate(quantitativeResult);
                log.info("result:{},cost:{}",result.toString(),System.currentTimeMillis()-startTime);

                // todo 平衡资金池
                boolean flag = balancePool(api, increaseTarget, increaseTargetRate,quantitativeResult.balanceAtStart_Target.amount);
                if(!flag){
                    break;
                }

                Thread.sleep(10000);

//                if(result.lossRate.compareTo(new BigDecimal(0))==0 && result.successRate.compareTo(new BigDecimal(0))==0){
//                    continue;
//                }
//                if(result.lossRate.compareTo(Config.maxLossRate) > 0 || result.successRate.compareTo(Config.minSuccessRate) <0){
//                    log.info("not match,lossRate:{},successRate:{}",result.lossRate,result.successRate);
//                    // todo 自动调整参数
//                    break;
//                }
//                if(increaseTarget < 0 && Math.abs(increaseTargetRate) > targetAmoutLostRate){
//                    log.info("taget lost:{},rate:{}",increaseTarget,increaseTargetRate);
//                    break;
//                }
//
//                if(increaseBase < 0 && Math.abs(increaseBaseRate) > baseAmoutLostRate){
//                    log.info("base lost:{},rate:{}",increaseBase,increaseBaseRate);
//                    break;
//                }


            } catch (Exception e) {
                e.printStackTrace();
                log.error("while Exception",e);
                Thread.sleep(60000);
            }
        }
    }


    /**
     * 平衡资金池
     * @param api
     * @param increaseTarget
     * @param increaseTargetRate
     * @return
     */
    public static boolean balancePool(Api api, double increaseTarget, double increaseTargetRate,String targetAmountAtStart) throws InterruptedException {

        boolean balanceFlag = false;
        balanceFailTimes=0;
        if(Math.abs(increaseTarget) <= Double.valueOf(Config.quota)){
            log.warn("balance Pool no need to balance,{}");
            return true;
        }

        // 尽最大可能去平衡资金池
        while(!balanceFlag && Math.abs(increaseTarget) > Double.valueOf(Config.quota) && balanceFailTimes<balanceMaxFailTimes){
            log.warn("balance Pool increaseTarget,{}",increaseTarget);
            OrderPrice orderPrice  = api.getOrderPrice(tradeSymbol);
            long tradeTime = System.currentTimeMillis();
            Order order = null;
            if(increaseTargetRate > 0 ){
                // 卖掉
                order = new Order(tradeSymbol, TradeType.sell,orderPrice.price,
                        BigDecimal.valueOf(Math.abs(increaseTarget)).setScale(Config.amountScale, RoundingMode.HALF_UP).toString());
               log.warn("balance Pool sell,{}",order);
                order = api.sell(order);

            }
            if(increaseTargetRate < 0 ){
                // 买入
                order = new Order(tradeSymbol, TradeType.buy,orderPrice.price,
                        BigDecimal.valueOf(Math.abs(increaseTarget)).setScale(Config.amountScale, RoundingMode.HALF_UP).toString());
                log.warn("balance Pool buy,{}",order);
                order = api.buy(order);

            }
            boolean isCancel = false;
            int cancelQueryTimes = 0;
            while(order!=null && order.orderId!=null){
                order = api.queryOrder(order);
                if (order.tradeStatus == TradeStatus.done) {
                    log.warn("balance query  done");
                    if(isCancel){
                        balanceFlag = false;
                    }else{
                        balanceFlag = true;
                    }
                    break;
                }
                else if (order.tradeStatus == TradeStatus.trading) {
                    order.tradeStatus=TradeStatus.trading;
                    // 超过最长等待时效
                    if (System.currentTimeMillis() > tradeTime + 10000) {
                        balanceFlag = false;
                        cancelQueryTimes++;
                        if(!isCancel){
                            balanceFailTimes++;
                            // 取消订单
                            log.warn("balance cancel  order *********");
                            api.cancel(order);
                            isCancel = true;
                        }else{
                            log.warn("balance canceling  order *********");
                        }

                    }
                }
                if(cancelQueryTimes > 100){
                    log.warn("balance query times exceed *********");
                    break;
                }
                Thread.sleep(5000);
            }
            Thread.sleep(10000);
            // 每次都去重新查询，看下次是否还需要

            if(!balanceFlag){
                try{
                    Balance balanceNow = api.getBalance(tradeSymbol.targetCurrency);
                    increaseTarget = Double.valueOf(balanceNow.amount)-Double.valueOf(targetAmountAtStart);
                    log.warn("balance Pool increaseTarget:{},balanceNow:{},targetAmountAtStart:{}",increaseTarget,balanceNow,targetAmountAtStart);
                    if(Math.abs(increaseTarget) <= Double.valueOf(Config.quota)){
                        balanceFlag = true;
                    }

                }catch (Exception e){
                    log.error("balance api.getBalance error,{}",e);
                }
            }

        }
        log.warn("balance result:{}",balanceFlag);

        return balanceFlag;
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
        //log.info("caculate:{}",quantitativeResult);
        for(Element elementB:quantitativeResult.tradingList_Buy){
            try{
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

            }catch (Exception e){
                log.error("caculate excepton :{}",e);
            }
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
                    Config.quota = String.valueOf(argsStr[1]);
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
                if ("baseAmoutLostRate".equals(argsStr[0])) {
                    baseAmoutLostRate = Double.valueOf(argsStr[1]);
                }
                if ("targetAmoutLostRate".equals(argsStr[0])) {
                    targetAmoutLostRate = Double.valueOf(argsStr[1]);
                }
                if ("amountScale".equals(argsStr[0])) {
                    Config.amountScale = Integer.valueOf(argsStr[1]);
                }
                if ("balanceMaxFailTimes".equals(argsStr[0])) {
                    balanceMaxFailTimes = Integer.valueOf(argsStr[1]);
                }
                if ("exchange".equals(argsStr[0])) {
                    exchange = String.valueOf(argsStr[1]);
                }
            }
        }
    }
}

