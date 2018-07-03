package com.coinex;

import com.alibaba.fastjson.JSON;
import com.auto.model.entity.TradeSymbol;
import com.auto.trade.common.HttpUtilForFcoin;
import com.auto.trade.entity.DepthData;
import com.coinex.entity.DepthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by caigaonian870 on 18/7/2.
 */
public class ApiCoinexForTriple extends ApiCoinex{
    private static final Logger log = LoggerFactory.getLogger(ApiCoinexForTriple.class);
    @Override
    public DepthData getDepthData(TradeSymbol symbol) {
        DepthData depthData = new DepthData();
        depthData.tradeSymbol=symbol;
        long start = System.currentTimeMillis();

        // todo 获取深度数据
        String response = HttpUtilForFcoin.doGetRequestPublic(url+"market/depth?market="+getSymbol(symbol)+"&merge=0.00000001"+"&limit=5");
        long end = System.currentTimeMillis();
        log.info("getDepthData cost:{},depthData:{}",end-start,response);

        if(response!=null) {

            DepthResponse depthResponse = JSON.parseObject(response, DepthResponse.class);
            if(depthResponse.getCode()!=null && depthResponse.getCode().equals("0")
                    && depthResponse.getData()!=null){

                List<String[]> asks = depthResponse.getData().getAsks();
                List<String[]> bids = depthResponse.getData().getBids();
                // fixme
                depthData.eventTime=end;
                depthData.bid_1_price=bids.get(0)[0];
                depthData.bid_1_qty=bids.get(0)[1];
                depthData.ask_1_price=asks.get(0)[0];
                depthData.ask_1_qty=asks.get(0)[1];
            }else{
                log.error("ticketResponse.getStatus() null");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }else{
            log.error("ticketResponse null");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return depthData;

    }

}
