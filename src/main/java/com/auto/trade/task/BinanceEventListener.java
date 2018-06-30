package com.auto.trade.task;

import com.binance.ApiBinance;
import com.auto.model.entity.Config;
import com.auto.trade.entity.DepthData;
import com.auto.trade.Application;
import com.auto.trade.entity.Exchange;
import com.auto.trade.entity.OrderPrice;
import com.auto.trade.services.AppContext;
import com.auto.trade.services.DataService;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.event.DepthEvent;
import com.binance.api.client.domain.market.OrderBookEntry;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by gof on 18/1/13.
 */
@Component
public class BinanceEventListener {
    private static final Logger log = LoggerFactory.getLogger(BinanceEventListener.class);

    private DateTime lastSaveDateTime = new DateTime().withYear(2010);

    @Autowired
    private DataService dataService;

    private DateTime lastHeatbeetTime = new DateTime();

    //@Scheduled(fixedRate = 10000)
    public void listen() throws InterruptedException {
        // fixme
        if(dataService != null){
            AppContext.setDataService(dataService);
        }
        DateTime now = new DateTime();
        // 失去心跳
        if(lastHeatbeetTime.plusSeconds(10).isBefore(now)){
            log.info("start listen isListenerProcess:{}", Application.isListenerProcess);
            if(Application.isListenerProcess){
                BinanceApiWebSocketClient binanceApiWebSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();
               // binanceApiWebSocketClient.onCandlestickEvent("ethusdt", CandlestickInterval.ONE_MINUTE, response ->getOrderPriceFromKline1Minte(response));

                binanceApiWebSocketClient.onDepthEvent(StringUtils.lowerCase(ApiBinance.getSymbol(Config.symbol)),response2 ->printlnDepthResponse(response2));

            }
        }
    }

    /**
     * 实时价格
     * @param candlestickEvent
     */
    public void getOrderPriceFromKline1Minte(CandlestickEvent candlestickEvent){
//        if(count %100==0){
//            log.info("CandlestickEvent:{}",candlestickEvent);
//        }
        OrderPrice orderPrice = new OrderPrice();
        orderPrice.setSymbol(candlestickEvent.getSymbol());
        orderPrice.setEventTime(candlestickEvent.getEventTime());
        orderPrice.setPrice(candlestickEvent.getClose());
        orderPrice.setQty(candlestickEvent.getVolume());
        orderPrice.setExchange(Exchange.binance.getExchange());
        OrderPrice orderPriceTmp =  dataService.queryOrderPrice(orderPrice.getEventTime());
        if(orderPriceTmp == null){
            dataService.saveOrderPrice(orderPrice);
        }
    }

    int count = 0;
    /**
     *  深度数据
     * @param depthEvent
     */
    public void printlnDepthResponse(DepthEvent depthEvent){
        lastHeatbeetTime = new DateTime();
        if(count %100==0){
            log.info("depthEvent:{}",depthEvent);
        }
        count++;
        DepthData depthData = new DepthData();
        depthData.setUpdateId(depthEvent.getUpdateId());
        depthData.setEventTime(depthEvent.getEventTime());
        depthData.setSymbol(depthEvent.getSymbol());
        depthData.setExchange(Exchange.binance.getExchange());
        List<OrderBookEntry> bidList= depthEvent.getBids();
        if(bidList!=null){
            if(bidList.size() > 0){
                depthData.setBid_1_price(bidList.get(0).getPrice());
                depthData.setBid_1_qty(bidList.get(0).getQty());
            }
            if(bidList.size() > 1){
                depthData.setBid_2_price(bidList.get(1).getPrice());
                depthData.setBid_2_qty(bidList.get(1).getQty());
            }
            if(bidList.size() > 2){
                depthData.setBid_3_price(bidList.get(2).getPrice());
                depthData.setBid_3_qty(bidList.get(2).getQty());
            }
            if(bidList.size() > 3){
                depthData.setBid_4_price(bidList.get(3).getPrice());
                depthData.setBid_4_qty(bidList.get(3).getQty());
            }
            if(bidList.size() > 4){
                depthData.setBid_5_price(bidList.get(4).getPrice());
                depthData.setBid_5_qty(bidList.get(4).getQty());
            }
        }

        List<OrderBookEntry> askList= depthEvent.getAsks();
        if(askList!=null){
            if(askList.size() > 0){
                depthData.setAsk_1_price(askList.get(0).getPrice());
                depthData.setAsk_1_qty(askList.get(0).getQty());
            }
            if(askList.size() > 1){
                depthData.setAsk_2_price(askList.get(1).getPrice());
                depthData.setAsk_2_qty(askList.get(1).getQty());
            }
            if(askList.size() > 2){
                depthData.setAsk_3_price(askList.get(2).getPrice());
                depthData.setAsk_3_qty(askList.get(2).getQty());
            }
            if(askList.size() > 3){
                depthData.setAsk_4_price(askList.get(3).getPrice());
                depthData.setAsk_4_qty(askList.get(3).getQty());
            }
            if(askList.size() > 4){
                depthData.setAsk_5_price(askList.get(4).getPrice());
                depthData.setAsk_5_qty(askList.get(4).getQty());
            }
        }
        DepthData depthDataTmp = dataService.queryDepthData(depthData.getEventTime(),depthData.getUpdateId());
        if(depthDataTmp == null){
            dataService.saveDepthData(depthData);
        }
    }


}
