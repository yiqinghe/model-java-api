package com.auto.model;

import com.auto.model.entity.*;
import com.auto.trade.entity.DepthData;
import com.auto.trade.entity.Exchange;
import com.auto.trade.entity.OrderPrice;
import com.auto.trade.services.ApiClient;
import com.auto.trade.services.AppContext;
import com.auto.trade.services.DataService;
import com.binance.api.client.domain.OrderStatus;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.market.BookTicker;
import com.binance.api.client.domain.market.TickerPrice;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.binance.api.client.domain.account.NewOrder.*;

/**
 * Created by caigaonian870 on 18/6/18.
 */
public class ApiBinance implements Api<Object>{
    private static final Logger log = LoggerFactory.getLogger(ApiBinance.class);


    private DataService dataService;

    private  DataService getDataService(){
        if(dataService == null){
            dataService = AppContext.getDataService();
        }
        return dataService;
    }

    public void getExchangeInfo(){
        //ApiClient.getBinanceClientInstant().
    }

    @Override
    public Balance getBalance(Currency currency) {
        Account account = ApiClient.getBinanceClientInstant().getAccount();
        String free = account.getAssetBalance(StringUtils.upperCase(currency.getCurrency())).getFree();
        Balance balance =  new Balance(currency);
        balance.amount=free;
        return balance;
    }

    @Override
    public DepthData getDepthData(TradeSymbol symbol) {
        DepthData depthData = new DepthData();
        long start = System.currentTimeMillis();
        // 获取实时价，代替深度
        List<TickerPrice> tickerPriceList= ApiClient.getBinanceClientInstant().getAllPrices();
        for(TickerPrice tickerPrice:tickerPriceList){
            if(tickerPrice.getSymbol().equals(getSymbol(symbol))){
                depthData.ask_1_price=tickerPrice.getPrice();
                depthData.bid_1_price=tickerPrice.getPrice();
                depthData.eventTime = System.currentTimeMillis();
                break;
            }
        }
        long end = System.currentTimeMillis();
        log.info("getDepthData cost:{},depthData:{}",end-start,depthData.getAsk_1_price());

       // DepthData depthData = getDataService().queryLatestDepthData(Exchange.binance,symbol);
        return depthData;
    }

    @Override
    public OrderPrice getOrderPrice(TradeSymbol symbol) {
        OrderPrice orderPrice = new OrderPrice();
        List<TickerPrice> tickerPriceList= ApiClient.getBinanceClientInstant().getAllPrices();
        for(TickerPrice tickerPrice:tickerPriceList){
            if(tickerPrice.getSymbol().equals(getSymbol(symbol))){
                orderPrice.price=tickerPrice.getPrice();
                break;
            }
        }
        return orderPrice;
    }

    @Override
    public Order buy(Order order) {
        NewOrderResponse newOrderResponse = ApiClient.getBinanceClientInstant().newOrder(limitBuy(getSymbol(order), order.timeInForce,order.amount,order.price));
        order.orderId= String.valueOf(newOrderResponse.getOrderId());
        order.tradeStatus=TradeStatus.trading;
        return order;
    }

    @Override
    public Order sell(Order order) {
        NewOrderResponse newOrderResponse = ApiClient.getBinanceClientInstant().newOrder(limitSell(getSymbol(order), order.timeInForce,order.amount,order.price));
        order.orderId= String.valueOf(newOrderResponse.getOrderId());

        // fixme 是否需要判断有效
        order.tradeStatus=TradeStatus.trading;
        return order;
    }

    @Override
    public Order buyMarket(Order order) {
        NewOrderResponse newOrderResponse = ApiClient.getBinanceClientInstant().newOrder(marketBuy(getSymbol(order), order.amount));
        order.orderId= String.valueOf(newOrderResponse.getOrderId());
        order.tradeStatus=TradeStatus.trading;
        return order;
    }

    @Override
    public Order sellMarket(Order order) {
        NewOrderResponse newOrderResponse = ApiClient.getBinanceClientInstant().newOrder(marketSell(getSymbol(order), order.amount));
        order.orderId= String.valueOf(newOrderResponse.getOrderId());
        order.tradeStatus=TradeStatus.trading;
        return order;
    }

    @Override
    public Order cancel(Order order) {
        CancelOrderRequest cancelOrderRequest =  new CancelOrderRequest(getSymbol(order),Long.valueOf(order.orderId));
        ApiClient.getBinanceClientInstant().cancelOrder(cancelOrderRequest);
        return order;
    }

    @Override
    public Order queryOrder(Order order) {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(getSymbol(order),Long.valueOf(order.orderId));
        com.binance.api.client.domain.account.Order binanceOrder = ApiClient.getBinanceClientInstant().getOrderStatus(orderStatusRequest);
        order.excutedAmount = binanceOrder.getExecutedQty();
        OrderStatus orderStatus = binanceOrder.getStatus();
        if(orderStatus==OrderStatus.CANCELED || orderStatus==OrderStatus.EXPIRED
                || orderStatus==OrderStatus.FILLED || orderStatus==OrderStatus.REJECTED){
            order.tradeStatus=TradeStatus.done;
        }
        if(orderStatus==OrderStatus.NEW || orderStatus==OrderStatus.PARTIALLY_FILLED
                || orderStatus==OrderStatus.PENDING_CANCEL ){
            order.tradeStatus=TradeStatus.trading;
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

//        tradeContext.buyPrice= buyPriceInDepth.multiply(new BigDecimal(1).subtract(Config.totalFeeRate.multiply(new BigDecimal(0.5)))).setScale(4, RoundingMode.CEILING).toString();
//        tradeContext.sellPrice=buyPriceInDepth.multiply(new BigDecimal(1).add(Config.totalFeeRate.multiply(new BigDecimal(0.5)))).setScale(4, RoundingMode.CEILING).toString();

          tradeContext.buyPrice= buyPriceInDepth.setScale(4, RoundingMode.CEILING).toString();
          tradeContext.sellPrice=buyPriceInDepth.setScale(4, RoundingMode.CEILING).toString();
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
        String target=StringUtils.upperCase(order.symbol.targetCurrency.getCurrency());
        String base=StringUtils.upperCase(order.symbol.baseCurrency.getCurrency());
        return target+base;
    }
    public static String getSymbol(TradeSymbol symbol){
        String target=StringUtils.upperCase(symbol.targetCurrency.getCurrency());
        String base=StringUtils.upperCase(symbol.baseCurrency.getCurrency());
        return target+base;
    }
}
