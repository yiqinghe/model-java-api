package com.fcoin;

import com.alibaba.fastjson.JSON;
import com.auto.model.common.Api;
import com.auto.model.entity.*;
import com.auto.trade.common.Constants;
import com.auto.trade.common.HmacSha1Util;
import com.auto.trade.common.HttpUtilForFcoin;
import com.auto.trade.common.SignUtil;
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
import java.util.List;
import java.util.Map;

/**
 * Created by gof on 18/6/18.
 *
 * 用于对冲套利
 */
public class ApiFcoinForTriple extends ApiFcoin {
    private static final Logger log = LoggerFactory.getLogger(ApiFcoinForTriple.class);


    @Override
    public DepthData getDepthData(TradeSymbol symbol) {
        DepthData depthData = new DepthData();
        depthData.tradeSymbol=symbol;
        long start = System.currentTimeMillis();
        Map<String,String> paras =new HashMap<>();

        // todo 获取深度数据
        String sign = buildSign("GET",url+"market/depth/L20/"+getSymbol(symbol),paras,start);
        String response = HttpUtilForFcoin.doGetRequest(url+"market/depth/L20/"+getSymbol(symbol),sign,start);
        long end = System.currentTimeMillis();
        log.info("getDepthData cost:{},depthData:{}",end-start,response);

        if(response!=null) {

            DepthResponse depthResponse = JSON.parseObject(response, DepthResponse.class);
            if(depthResponse.getStatus()!=null && depthResponse.getStatus().equals("0")
                    && depthResponse.getData()!=null){

                String[] asks = depthResponse.getData().getAsks();
                String[] bids = depthResponse.getData().getBids();
                // fixme
                depthData.eventTime=end;
                depthData.bid_1_price=bids[0];
                depthData.bid_1_qty=bids[1];
                depthData.ask_1_price=asks[0];
                depthData.ask_1_qty=asks[1];
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
