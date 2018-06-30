package com.fcoin;

import com.alibaba.fastjson.JSON;
import com.auto.model.common.Api;
import com.auto.model.entity.*;
import com.auto.trade.common.*;
import com.auto.trade.entity.DepthData;
import com.auto.trade.entity.OrderPrice;
import com.fcoin.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gof on 18/6/18.
 */
public class ApiFcoin implements Api<Object> {
    private static final Logger log = LoggerFactory.getLogger(ApiFcoin.class);
    public static String url = "https://api.fcoin.com/v2/";

    @Override
    public Balance getBalance(Currency currency) {

        long start = System.currentTimeMillis();

        Map<String,String> paras =new HashMap<>();

        Balance balanceReturn = new Balance(currency);
        String sign = buildSign("GET",url+"accounts/balance",paras,start);
        String response = HttpUtilForFcoin.doGetRequest(url+"accounts/balance",sign,start);
        long end = System.currentTimeMillis();
        log.info("getBalance cost:{},getBalance:{}",end-start,response);
        if(response!=null) {

            BalanceResponse balanceResponse = JSON.parseObject(response, BalanceResponse.class);
            if(balanceResponse.getStatus()!=null && balanceResponse.getStatus().equals("0")
                    && balanceResponse.getData()!=null && balanceResponse.getData().size()>0){

                for(BalanceResponse.Balance balance:balanceResponse.getData()){
                    if(StringUtils.upperCase(balance.getCurrency()).equals(StringUtils.upperCase(currency.getCurrency()))){
                        balanceReturn.amount = balance.getAvailable();
                        return balanceReturn;
                    }
                }

            }else{
                log.error("balanceResponse.getStatus() null");
            }

        }else{
            log.error("balanceResponse null");

        }

        return balanceReturn;
    }

    @Override
    public DepthData getDepthData(TradeSymbol symbol) {
        DepthData depthData = new DepthData();
        long start = System.currentTimeMillis();
        Map<String,String> paras =new HashMap<>();

        String sign = buildSign("GET",url+"market/ticker/"+getSymbol(symbol),paras,start);
        String response = HttpUtilForFcoin.doGetRequest(url+"market/ticker/"+getSymbol(symbol),sign,start);
        long end = System.currentTimeMillis();
        log.info("getDepthData cost:{},depthData:{}",end-start,response);

        if(response!=null) {

            TickerResponse ticketResponse = JSON.parseObject(response, TickerResponse.class);
            if(ticketResponse.getStatus()!=null && ticketResponse.getStatus().equals("0")
                    && ticketResponse.getData()!=null && ticketResponse.getData().getTicker()!=null){

                String[] ticker = ticketResponse.getData().getTicker();
                depthData.eventTime=start;
                depthData.bid_1_price=ticker[0];
                depthData.ask_1_price=ticker[0];
            }else{
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.error("ticketResponse.getStatus() null");
            }

        }else{
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.error("ticketResponse null");

        }

        return depthData;

    }

    @Override
    public OrderPrice getOrderPrice(TradeSymbol symbol) {
        OrderPrice orderPrice = new OrderPrice();
        long start = System.currentTimeMillis();
        Map<String,String> paras =new HashMap<>();

        String sign = buildSign("GET",url+"market/ticker/"+getSymbol(symbol),paras,start);
        String response = HttpUtilForFcoin.doGetRequest(url+"market/ticker/"+getSymbol(symbol),sign,start);
        long end = System.currentTimeMillis();
        log.info("getOrderPrice cost:{},getOrderPrice:{}",end-start,response);

        if(response!=null) {

            TickerResponse ticketResponse = JSON.parseObject(response, TickerResponse.class);
            if(ticketResponse.getStatus()!=null && ticketResponse.getStatus().equals("0")
                    && ticketResponse.getData()!=null && ticketResponse.getData().getTicker()!=null){

                String[] ticker = ticketResponse.getData().getTicker();
                orderPrice.eventTime=start;
                orderPrice.price=ticker[0];
            }else{
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.error("getOrderPrice.getStatus() null");
            }

        }else{
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.error("getOrderPrice null");

        }

        return orderPrice;
    }

    @Override
    public Order buy(Order order) {
        long start = System.currentTimeMillis();

        Map<String,String> paras =new HashMap<>();
        paras.put("symbol",getSymbol(order));
        paras.put("side","buy");
        paras.put("type","limit");
        paras.put("price",order.price);
        paras.put("amount",order.amount);

        String sign = buildSign("POST",url+"orders",paras,start);
        String response = HttpUtilForFcoin.doPostForJson(url+"orders", JSON.toJSONString(paras),sign,start);

        long end = System.currentTimeMillis();
        log.info("buy cost:{},response:{}",end-start,response);

        if(response!=null) {
            // fixme 是否需要加try catch
            OrderTradeResponse orderResponse= JSON.parseObject(response, OrderTradeResponse.class);
            if(orderResponse.getStatus()!=null && orderResponse.getStatus().equals("0")){
                order.orderId=orderResponse.getData();
                order.tradeStatus=TradeStatus.trading;
            }else{
                log.error("orderResponse.getStatus() buy null");
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }else{
            log.error("orderResponse buy null");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return order;
    }

    @Override
    public Order sell(Order order) {
        long start = System.currentTimeMillis();

        Map<String,String> paras =new HashMap<>();
        paras.put("symbol",getSymbol(order));
        paras.put("side","sell");
        paras.put("type","limit");
        paras.put("price",order.price);
        paras.put("amount",order.amount);

        String sign = buildSign("POST",url+"orders",paras,start);
        String response = HttpUtilForFcoin.doPostForJson(url+"orders", JSON.toJSONString(paras),sign,start);

        long end = System.currentTimeMillis();
        log.info("sell cost:{},response:{}",end-start,response);

        if(response!=null) {
            // fixme 是否需要加try catch
            OrderTradeResponse orderResponse= JSON.parseObject(response, OrderTradeResponse.class);
            if(orderResponse.getStatus()!=null && orderResponse.getStatus().equals("0")){
                order.orderId=orderResponse.getData();
                order.tradeStatus=TradeStatus.trading;
            }else{
                log.error("orderResponse.getStatus() sell null");
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }else{
            log.error("orderResponse sell null");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return order;
    }

    @Override
    public Order buyMarket(Order order) {
        return null;
    }

    @Override
    public Order sellMarket(Order order) {
        return null;
    }

    @Override
    public Order cancel(Order order) {
        long start = System.currentTimeMillis();

        Map<String,String> paras =new HashMap<>();

        String sign = buildSign("POST",url+"orders/"+order.orderId+"/submit-cancel",paras,start);
        String response = HttpUtilForFcoin.doPostForJson(url+"orders/"+order.orderId+"/submit-cancel", "{}",sign,start);

        long end = System.currentTimeMillis();
        log.info("cancel cost:{},response:{}",end-start,response);

        if(response!=null) {

            OrderCancelResponse orderResponse= JSON.parseObject(response, OrderCancelResponse.class);
            if(orderResponse.getStatus()!=null && (orderResponse.getStatus().equals("0")||orderResponse.getStatus().equals("3008"))){

            }else{
                log.error("orderResponse.cancel() null");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }else{
            log.error("orderResponse cancel null");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;

        }
        return order;
    }

    @Override
    public Order queryOrder(Order order) {
        long start = System.currentTimeMillis();

        Map<String,String> paras =new HashMap<>();

        String sign = buildSign("GET",url+"orders/"+order.orderId,paras,start);
        String response = HttpUtilForFcoin.doGetRequest(url+"orders/"+order.orderId,sign,start);

        long end = System.currentTimeMillis();
        log.info("query cost:{},response:{}",end-start,response);

        if(response!=null) {

            OrderQueryResponse orderQueryResponse= JSON.parseObject(response, OrderQueryResponse.class);
            if(orderQueryResponse.getStatus()!=null && orderQueryResponse.getStatus().equals("0")
                    &&orderQueryResponse.getData()!=null){
                OrderQueryResponse.Data orderQuery = orderQueryResponse.getData();

                if(orderQuery.getState().equals("canceled") || orderQuery.getState().equals("filled")  || orderQuery.getState().equals("partial_canceled")){
                    order.tradeStatus=TradeStatus.done;
                    order.excutedAmount=orderQuery.getFilled_amount();
                }
                if(orderQuery.getState().equals("submitted")||orderQuery.getState().equals("partial_filled")
                        ||orderQuery.getState().equals("partial_filled")||orderQuery.getState().equals("pending_cancel")){
                    order.tradeStatus=TradeStatus.trading;
                    order.excutedAmount=orderQuery.getFilled_amount();
                }

            }else{
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.error("orderQueryResponse.getStatus() null");
            }

        }else{
            log.error("orderQueryResponse null");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return order;
    }

    @Override
    public TradeContext buildTradeContext(DepthData depthData) {
        BigDecimal sellPriceInDepth = new BigDecimal(depthData.getAsk_1_price());
        BigDecimal buyPriceInDepth = new BigDecimal(depthData.getBid_1_price());
        TradeContext tradeContext = new TradeContext();
        tradeContext.canTrade=false;
        // 防止太大差额
        if(buyPriceInDepth.multiply(new BigDecimal(1.02)).compareTo(sellPriceInDepth) < 0){
            log.warn("exceed limit");
            return tradeContext;
        }
        //判断是否能发起交易、抵过交易手续费就可以，价格就是卖一买一价
//        if (buyPriceInDepth.multiply(new BigDecimal(1).add(Config.totalFeeRate)).compareTo(sellPriceInDepth) < 0) {
//            System.out.println("canTrade:"+depthData);
//            tradeContext.canTrade=true;
//            // todo 避免恶意拉盘，砸盘
//
//            tradeContext.buyPrice=buyPriceInDepth.toString();
//            tradeContext.sellPrice=sellPriceInDepth.toString();
//
//        }

        tradeContext.canTrade=true;
            // todo 避免恶意拉盘，砸盘

        // todo 根据趋势来造价
        // todo 判断价格比较平稳的时候操作。

        //  防止吃到天价单
        if(buyPriceInDepth.compareTo(new BigDecimal(Config.maxPrice))>0){
            tradeContext.canTrade=false;
            log.error("price exception >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            return tradeContext;
        }

        tradeContext.buyPrice= buyPriceInDepth.setScale(Config.priceScale, RoundingMode.CEILING).toString();
        if(Config.abTest.equals("a")){
            tradeContext.sellPrice=buyPriceInDepth.setScale(Config.priceScale, RoundingMode.CEILING).toString();
        }else if(Config.abTest.equals("b")){
            BigDecimal increase = new BigDecimal(Config.unit)
                    .divide(new BigDecimal(10).pow(Config.increasePriceScale),Config.priceScale, RoundingMode.CEILING)
                    .setScale(Config.priceScale, RoundingMode.CEILING);

            tradeContext.sellPrice=buyPriceInDepth.add(increase).setScale(Config.priceScale, RoundingMode.CEILING).toString();
        }else if(Config.abTest.equals("c")){
            BigDecimal diff = new BigDecimal(Config.unit)
                    .divide(new BigDecimal(10).pow(Config.increasePriceScale),Config.priceScale, RoundingMode.CEILING)
                    .setScale(Config.priceScale, RoundingMode.CEILING);
            log.info("pscale:{},diffIncreate:{}",Config.increasePriceScale,diff);

            tradeContext.buyPrice= buyPriceInDepth.subtract(diff).setScale(Config.priceScale, RoundingMode.CEILING).toString();
            tradeContext.sellPrice=buyPriceInDepth.add(diff).setScale(Config.priceScale, RoundingMode.CEILING).toString();
        }

        return tradeContext;
    }

    @Override
    public BigDecimal getTargetAmountForBuy(BigDecimal originalTargetAmount) {
        // 抵扣手续费,多买
//        BigDecimal targetAmount = originalTargetAmount.multiply(new BigDecimal(1).add(Config.baseFeeRate));
//        System.out.println("getTargetAmountForBuy:"+targetAmount);
//        return targetAmount;
        return originalTargetAmount;
    }

    @Override
    public BigDecimal getTargetAmountForSell(BigDecimal originalTargetAmount) {
        // 币安模式，抵扣手续费bnb,少卖
//        BigDecimal targetAmount = originalTargetAmount.multiply(new BigDecimal(1).subtract(Config.baseFeeRate));
//        System.out.println("getTargetAmountForSell:"+targetAmount);
//        return targetAmount;
        return originalTargetAmount;
    }

    public static String getSymbol(Order order){
        String target=StringUtils.lowerCase(order.symbol.targetCurrency.getCurrency());
        String base=StringUtils.lowerCase(order.symbol.baseCurrency.getCurrency());
        return target+base;
    }
    public static String getSymbol(TradeSymbol symbol){
        String target=StringUtils.lowerCase(symbol.targetCurrency.getCurrency());
        String base=StringUtils.lowerCase(symbol.baseCurrency.getCurrency());
        return target+base;
    }
    @Override
    public String buildSign(String http_head, String path, Map<String, String> params, long systemTimeMillsecs) {
       if(http_head.equals("GET")){
           String url_params = null;
           String payload = null;
           if(params.size()>0){
               url_params = SignUtil.formatUrlMap(params, false, false);
               payload = String.format("%s%s%s", http_head, path+"?"+url_params, systemTimeMillsecs);
           }else{
               payload = String.format("%s%s%s", http_head, path, systemTimeMillsecs);
           }
           String sign = null;
           try {
               sign = HmacSha1Util.sha1_HMAC(payload, Constants.SECRET);
           } catch (InvalidKeyException e) {
               e.printStackTrace();
           } catch (NoSuchAlgorithmException e) {
               e.printStackTrace();
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           }
           ;
           return sign;
       }else{
           String url_params = null;
           String payload = null;

           url_params = SignUtil.formatUrlMap(params, false, false);
           payload = String.format("%s%s%s%s", http_head, path, systemTimeMillsecs,url_params);
           String sign = null;
           try {
               sign = HmacSha1Util.sha1_HMAC(payload, Constants.SECRET);
           } catch (InvalidKeyException e) {
               e.printStackTrace();
           } catch (NoSuchAlgorithmException e) {
               e.printStackTrace();
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           }
           ;
           return sign;
       }
    }

}
