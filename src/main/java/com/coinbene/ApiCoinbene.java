package com.coinbene;

import com.alibaba.fastjson.JSON;
import com.auto.model.common.Api;
import com.auto.model.entity.*;
import com.auto.trade.common.Constants;
import com.auto.trade.entity.DepthData;
import com.auto.trade.entity.OrderPrice;
import com.auto.trade.common.HttpUtil;
import com.coinbene.entity.BalanceResponse;
import com.coinbene.entity.OrderQueryResponse;
import com.coinbene.entity.OrderResponse;
import com.coinbene.entity.TickerResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gof on 18/6/18.
 */
public class ApiCoinbene implements Api<Object> {
    private static final Logger log = LoggerFactory.getLogger(ApiCoinbene.class);

    public static String market_url = "http://api.coinbene.com/v1/market/";
    public static String trade_url = "http://api.coinbene.com/v1/trade/";


    @Override
    public Balance getBalance(Currency currency) {

        long start = System.currentTimeMillis();

        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid", Constants.APIKEY);
        paras.put("secret",Constants.SECRET);
        paras.put("timestamp",start);
        paras.put("account","exchange");
        String json = HttpUtil.buildPostJsonWithMd5Sign(paras);

        Balance balanceReturn = new Balance(currency);
        String response = HttpUtil.doPostForJson(trade_url+"balance",json);
        if(response!=null) {

            BalanceResponse balanceResponse = JSON.parseObject(response, BalanceResponse.class);
            if(balanceResponse.getStatus()!=null && balanceResponse.getStatus().equals("ok")
                    && balanceResponse.getBalance()!=null && balanceResponse.getBalance().size()>0){

                for(BalanceResponse.Balance balance:balanceResponse.getBalance()){
                    if(StringUtils.upperCase(balance.getAsset()).equals(StringUtils.upperCase(currency.getCurrency()))){
                        balanceReturn.amount = balance.getTotal();
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
        String response = HttpUtil.doGetRequest(market_url+"ticker?symbol="+getSymbol(symbol));
        long end = System.currentTimeMillis();
        log.info("getDepthData cost:{},depthData:{}",end-start,response);

        if(response!=null) {

            TickerResponse ticketResponse = JSON.parseObject(response, TickerResponse.class);
            if(ticketResponse.getStatus()!=null && ticketResponse.getStatus().equals("ok")
                    && ticketResponse.getTicker()!=null && ticketResponse.getTicker().size()>0){

                TickerResponse.Ticker tickerPrice = ticketResponse.getTicker().get(0);
                depthData.eventTime=ticketResponse.getTimestamp();
                depthData.bid_1_price=tickerPrice.getLast();
                depthData.ask_1_price=tickerPrice.getLast();
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
        String response = HttpUtil.doGetRequest(market_url+"ticker?symbol="+getSymbol(symbol));
        long end = System.currentTimeMillis();
        log.info("getOrderPrice cost:{},getOrderPrice:{}",end-start,response);

        if(response!=null) {

            TickerResponse ticketResponse = JSON.parseObject(response, TickerResponse.class);
            if(ticketResponse.getStatus()!=null && ticketResponse.getStatus().equals("ok")
                    && ticketResponse.getTicker()!=null && ticketResponse.getTicker().size()>0){

                TickerResponse.Ticker tickerPrice = ticketResponse.getTicker().get(0);
                orderPrice.price=tickerPrice.getLast();
                orderPrice.setEventTime(ticketResponse.getTimestamp());
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

        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid", Constants.APIKEY);
        paras.put("secret",Constants.SECRET);
        paras.put("price",order.price);
        paras.put("quantity",order.amount);
        paras.put("symbol",getSymbol(order));
        paras.put("type","buy-limit");
        paras.put("timestamp",start);
        String json = HttpUtil.buildPostJsonWithMd5Sign(paras);

        String response = HttpUtil.doPostForJson(trade_url+"order/place",json);
        long end = System.currentTimeMillis();
        log.info("buy cost:{},response:{}",end-start,response);

        if(response!=null) {
            // fixme 是否需要加try catch
            OrderResponse orderResponse= JSON.parseObject(response, OrderResponse.class);
            if(orderResponse.getStatus()!=null && orderResponse.getStatus().equals("ok")){
                order.orderId=orderResponse.getOrderid();
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

        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid", Constants.APIKEY);
        paras.put("secret",Constants.SECRET);
        paras.put("price",order.price);
        paras.put("quantity",order.amount);
        paras.put("symbol",getSymbol(order));
        paras.put("type","sell-limit");
        paras.put("timestamp",start);
        String json = HttpUtil.buildPostJsonWithMd5Sign(paras);

        String response = HttpUtil.doPostForJson(trade_url+"order/place",json);
        long end = System.currentTimeMillis();
        log.info("sell cost:{},response:{}",end-start,response);

        if(response!=null) {

            OrderResponse orderResponse= JSON.parseObject(response, OrderResponse.class);
            if(orderResponse.getStatus()!=null && orderResponse.getStatus().equals("ok")){
                order.orderId=orderResponse.getOrderid();
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

        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid", Constants.APIKEY);
        paras.put("secret",Constants.SECRET);
        paras.put("orderid",order.orderId);
        paras.put("timestamp",start);
        String json = HttpUtil.buildPostJsonWithMd5Sign(paras);

        String response = HttpUtil.doPostForJson(trade_url+"order/cancel",json);
        long end = System.currentTimeMillis();
        log.info("cancel cost:{},response:{}",end-start,response);

        if(response!=null) {

            OrderResponse orderResponse= JSON.parseObject(response, OrderResponse.class);
            if(orderResponse.getStatus()!=null && orderResponse.getStatus().equals("ok")){

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

        Map<String,Object> paras =new HashMap<>();
        paras.put("apiid", Constants.APIKEY);
        paras.put("secret",Constants.SECRET);
        paras.put("orderid",order.orderId);
        paras.put("timestamp",start);
        String json = HttpUtil.buildPostJsonWithMd5Sign(paras);

        String response = HttpUtil.doPostForJson(trade_url+"order/info",json);
        long end = System.currentTimeMillis();
        log.info("query cost:{},response:{}",end-start,response);

        if(response!=null) {

            OrderQueryResponse orderQueryResponse= JSON.parseObject(response, OrderQueryResponse.class);
            if(orderQueryResponse.getStatus()!=null && orderQueryResponse.getStatus().equals("ok")
                    &&orderQueryResponse.getOrder()!=null){
                OrderQueryResponse.Order orderQuery = orderQueryResponse.getOrder();

                if(orderQuery.getOrderstatus().equals("canceled") || orderQuery.getOrderstatus().equals("filled")  || orderQuery.getOrderstatus().equals("partialCanceled")){
                    order.tradeStatus=TradeStatus.done;
                    order.excutedAmount=orderQuery.getFilledquantity();
                }
                if(orderQuery.getOrderstatus().equals("unfilled")||orderQuery.getOrderstatus().equals("partialFilled")){
                    order.tradeStatus=TradeStatus.trading;
                    order.excutedAmount=orderQuery.getFilledquantity();
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

        tradeContext.buyPrice= buyPriceInDepth.setScale(Config.priceScale, RoundingMode.CEILING).toString();
        if(Config.abTest.equals("a")){
            tradeContext.sellPrice=buyPriceInDepth.setScale(Config.priceScale, RoundingMode.CEILING).toString();
        }else if(Config.abTest.equals("b")){
            BigDecimal increase = new BigDecimal("1")
                    .divide(new BigDecimal(10).pow(Config.priceScale),Config.priceScale, RoundingMode.CEILING)
                    .setScale(Config.priceScale, RoundingMode.CEILING);

            tradeContext.sellPrice=buyPriceInDepth.add(increase).setScale(Config.priceScale, RoundingMode.CEILING).toString();
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
        return null;
    }


}
