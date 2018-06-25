package com.auto.model;

import com.alibaba.fastjson.JSON;
import com.auto.model.entity.*;
import com.auto.model.entity.Currency;
import com.auto.trade.common.Constants;
import com.auto.trade.common.HmacSha256Util;
import com.auto.trade.common.SignUtil;
import com.auto.trade.entity.DepthData;
import com.auto.trade.entity.OrderPrice;
import com.auto.trade.common.HttpUtil;
import com.ocx.entity.BalanceResponse;
import com.ocx.entity.OrderQueryResponse;
import com.ocx.entity.OrderResponse;
import com.ocx.entity.TickerResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ApiOcx implements Api<Object> {
    private static final Logger log = LoggerFactory.getLogger(ApiOcx.class);

    @Override
    public Balance getBalance(Currency currency) {

        long start = System.currentTimeMillis();

        Map<String,String> paraMap =new HashMap<>();
        String paras = signedParams("GET","/api/v2/accounts",paraMap,System.currentTimeMillis());

        Balance balanceReturn = new Balance(currency);
        String response = HttpUtil.doGetRequest(com.ocx.Api.url+"api/v2/accounts?"+paras);
        long end = System.currentTimeMillis();
        //log.info("getBalance cost:{},data:{}",end-start,response);

        if(response!=null) {

            BalanceResponse balanceResponse = JSON.parseObject(response, BalanceResponse.class);
            if(balanceResponse.getData()!=null){
                for(BalanceResponse.Data data:balanceResponse.getData()){
                    if(StringUtils.upperCase(data.getCurrency_code()).equals(StringUtils.upperCase(currency.getCurrency()))){
                        balanceReturn.amount = data.getBalance();
                        return balanceReturn;
                    }
                }

            }else{
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.error("balanceResponse.getData() null");
            }

        }else{
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.error("balanceResponse null");

        }

        return balanceReturn;
    }

    @Override
    public DepthData getDepthData(TradeSymbol symbol) {
        DepthData depthData = new DepthData();

        long start = System.currentTimeMillis();
        Map<String,String> paraMap =new HashMap<>();
        String paras = signedParams("GET","/api/v2/tickers",paraMap,System.currentTimeMillis());


        String response = HttpUtil.doGetRequest(com.ocx.Api.url+"api/v2/tickers?"+paras);
        long end = System.currentTimeMillis();
        log.info("getDepthData cost:{},getDepthData:{}",end-start,response);

        if(response!=null) {

            TickerResponse ticketResponse = JSON.parseObject(response, TickerResponse.class);
            if(ticketResponse.getData()!=null){

                for(TickerResponse.Data tickerPrice:ticketResponse.getData()){
                    if(StringUtils.upperCase(tickerPrice.getMarket_code()).equals(StringUtils.upperCase(getSymbol(symbol)))){
                        depthData.eventTime= System.currentTimeMillis();//fixme
                        depthData.bid_1_price=tickerPrice.getLast();
                        depthData.ask_1_price=tickerPrice.getLast();
                    }
                }

            }else{
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.error("getDepthData.getStatus() null");
            }

        }else{
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.error("getDepthData null");

        }

        return depthData;

    }

    @Override
    public OrderPrice getOrderPrice(TradeSymbol symbol) {
        OrderPrice orderPrice = new OrderPrice();

        long start = System.currentTimeMillis();
        Map<String,String> paraMap =new HashMap<>();
        String paras = signedParams("GET","/api/v2/tickers",paraMap,System.currentTimeMillis());


        String response = HttpUtil.doGetRequest(com.ocx.Api.url+"api/v2/tickers?"+paras);
        long end = System.currentTimeMillis();
        log.info("getOrderPrice cost:{},getOrderPrice:{}",end-start,response);

        if(response!=null) {

            TickerResponse ticketResponse = JSON.parseObject(response, TickerResponse.class);
            if(ticketResponse.getData()!=null){

                for(TickerResponse.Data tickerPrice:ticketResponse.getData()){
                    if(StringUtils.upperCase(tickerPrice.getMarket_code()).equals(StringUtils.upperCase(getSymbol(symbol)))){
                        orderPrice.price=tickerPrice.getLast();
                        orderPrice.setEventTime(System.currentTimeMillis());//fixme
                    }
                }

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

        Map<String,String> paraMap =new HashMap<>();
        paraMap.put("market_code",getSymbol(order.symbol));
        paraMap.put("side","buy");
        paraMap.put("price",order.price);
        paraMap.put("volume",order.amount);
        String paras = signedParams("POST","/api/v2/orders",paraMap,System.currentTimeMillis());


        String response = HttpUtil.doPostForJson(com.ocx.Api.url+"api/v2/orders?"+paras,"{}");
        long end = System.currentTimeMillis();
        log.info("buy cost:{},response:{}",end-start,response);

        if(response!=null) {
            try{
                OrderResponse orderResponse= JSON.parseObject(response, OrderResponse.class);
                if(orderResponse.getData()!=null){

                    OrderResponse.Data data = orderResponse.getData();
                    order.orderId=data.getId();
                    order.tradeStatus=TradeStatus.trading;

                }else{
                    log.error("orderResponse.getStatus() buy null");
                    try {
                        // 不能超过future的超时时间
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                log.error("orderResponse.getStatus() buy exception,e",e);
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException ei) {
                    ei.printStackTrace();
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

        Map<String,String> paraMap =new HashMap<>();
        paraMap.put("market_code",getSymbol(order.symbol));
        paraMap.put("side","sell");
        paraMap.put("price",order.price);
        paraMap.put("volume",order.amount);
        String paras = signedParams("POST","/api/v2/orders",paraMap,System.currentTimeMillis());


        String response = HttpUtil.doPostForJson(com.ocx.Api.url+"api/v2/orders?"+paras,"{}");
        long end = System.currentTimeMillis();
        log.info("sell cost:{},response:{}",end-start,response);

        if(response!=null) {

            try{
                OrderResponse orderResponse= JSON.parseObject(response, OrderResponse.class);
                if(orderResponse.getData()!=null){

                    OrderResponse.Data data = orderResponse.getData();
                    order.orderId=data.getId();
                    order.tradeStatus=TradeStatus.trading;

                }else{
                    log.error("orderResponse.getStatus() sell null");
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                log.error("orderResponse.getStatus() sell exception,e",e);
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException ei) {
                    ei.printStackTrace();
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

        Map<String,String> paraMap =new HashMap<>();
        // fixme
        paraMap.put("id",order.orderId);
        String paras = signedParams("POST","/api/v2/orders/"+order.orderId+"/cancel",paraMap,System.currentTimeMillis());


        String response = HttpUtil.doPostForJson(com.ocx.Api.url+"api/v2/orders/"+order.orderId+"/cancel?"+paras,"{}");
        long end = System.currentTimeMillis();
        log.info("cancel cost:{},response:{}",end-start,response);

        if(response!=null) {

            OrderResponse orderResponse= JSON.parseObject(response, OrderResponse.class);
            if(orderResponse.getData()!=null){

            }else{
                log.error("orderResponse.cancel() null");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }else{
            log.error("orderResponse cancel null");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return order;
    }

    @Override
    public Order queryOrder(Order order) {
        long start = System.currentTimeMillis();

        Map<String,String> paraMap =new HashMap<>();
        // fixme
        paraMap.put("id",order.orderId);
        String paras = signedParams("GET","/api/v2/orders/"+order.orderId,paraMap,System.currentTimeMillis());


        String response = HttpUtil.doGetRequest(com.ocx.Api.url+"api/v2/orders/"+order.orderId+"?"+paras);
        long end = System.currentTimeMillis();
        log.info("query cost:{},response:{}",end-start,response);

        if(response!=null) {

            OrderQueryResponse orderQueryResponse= JSON.parseObject(response, OrderQueryResponse.class);
            if(orderQueryResponse.getData()!=null){

                OrderQueryResponse.Data data = orderQueryResponse.getData();
                if(data.getState().equals("done") ||data.getState().equals("cancel")){
                    order.tradeStatus=TradeStatus.done;
                    order.excutedAmount=data.getExecuted_volume();
                }
                if(data.getState().equals("wait")){
                    order.tradeStatus=TradeStatus.trading;
                    order.excutedAmount=data.getExecuted_volume();
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

        tradeContext.buyPrice= buyPriceInDepth.setScale(6, RoundingMode.CEILING).toString();
        tradeContext.sellPrice=buyPriceInDepth.setScale(6, RoundingMode.CEILING).toString();
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


    /**
     * 获取包含签名的请求url的参数
     * @param http_head
     * @param path
     * @param params
     * @return
     */
    public static String signedParams(String http_head, String path, Map<String, String> params,long systemTimeMillsecs) {

        params.put("access_key",Constants.APIKEY);
        params.put("tonce",  systemTimeMillsecs+"");

        String url_params = SignUtil.formatUrlMap(params, false, false);
        String payload = String.format("%s|%s|%s", http_head, path, url_params);
        String signature = HmacSha256Util.sha256_HMAC(payload,Constants.SECRET);

        String signed_url_params = url_params + "&signature=" + signature;
        return signed_url_params;
    }
    /**
     * 获取包含签名的请求url的参数
     * @param http_head
     * @param path
     * @param params
     * @return
     */
    public static String signedJsonParams(String http_head, String path, Map<String, String> params,long systemTimeMillsecs) {

        params.put("access_key",Constants.APIKEY);
        params.put("tonce",  systemTimeMillsecs+"");

        String url_params = SignUtil.formatUrlMap(params, false, false);
        String payload = String.format("%s|%s|%s", http_head, path, url_params);
        String signature = HmacSha256Util.sha256_HMAC(payload,Constants.SECRET);
        params.put("signature",signature);


        String signed_url_paramsJson = JSON.toJSONString(params);
        log.info("signed_url_paramsJson:{}",signed_url_paramsJson);
        return signed_url_paramsJson;
    }


}
