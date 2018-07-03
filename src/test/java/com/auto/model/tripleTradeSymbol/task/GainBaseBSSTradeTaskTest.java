package com.auto.model.tripleTradeSymbol.task;

import com.auto.model.entity.*;
import com.auto.trade.entity.DepthData;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by caigaonian870 on 18/7/1.
 */
public class GainBaseBSSTradeTaskTest {

    @Test
    public void buildTradeContext() throws Exception {
        Currency ftCurrency = Currency.ft;
        Currency usdtCurrency = Currency.usdt;
        Currency ethCurrency = Currency.eth;

        TradeSymbol tradeSymbolA = new TradeSymbol(ftCurrency,usdtCurrency);
        TradeSymbol tradeSymbolB = new TradeSymbol(ftCurrency,ethCurrency);
        TradeSymbol tradeSymbolC = new TradeSymbol(ethCurrency,usdtCurrency);
        DepthData depthDataA=new  DepthData();
        depthDataA.setAsk_1_price("100");
        depthDataA.setBid_1_price("100");
        depthDataA.setAsk_1_qty("100");
        depthDataA.setBid_1_qty("100");
        depthDataA.tradeSymbol=tradeSymbolA;
        DepthData depthDataB=new  DepthData();
        depthDataB.tradeSymbol=tradeSymbolB;
        depthDataB.setAsk_1_price("100");
        depthDataB.setBid_1_price("100");
        depthDataB.setAsk_1_qty("100");
        depthDataB.setBid_1_qty("100");
        DepthData depthDataC=new  DepthData();
        depthDataC.tradeSymbol=tradeSymbolC;
        depthDataC.setAsk_1_price("100");
        depthDataC.setBid_1_price("100");
        depthDataC.setAsk_1_qty("100");
        depthDataC.setBid_1_qty("100");


        GainBaseTradeTask GainBaseTradeTask = new GainBaseBSSTradeTask(null,null,null,
                null,null,null,null,null,null,null,null);
        TradeContext tradeContext = GainBaseTradeTask.buildTradeContext(depthDataA,depthDataB,depthDataC);
        Assert.assertTrue(tradeContext.orderA.symbol==tradeSymbolA);
        Assert.assertTrue(tradeContext.orderB.symbol==tradeSymbolB);
        Assert.assertTrue(tradeContext.orderC.symbol==tradeSymbolC);
        Assert.assertTrue(tradeContext.orderA.tradeType== TradeType.buy);
        Assert.assertTrue(tradeContext.orderB.tradeType== TradeType.sell);
        Assert.assertTrue(tradeContext.orderC.tradeType== TradeType.sell);
    }
    @Test
    public void buildTradeContext2() throws Exception {
        Currency ftCurrency = Currency.ft;
        Currency usdtCurrency = Currency.usdt;
        Currency ethCurrency = Currency.eth;

        TradeSymbol tradeSymbolB = new TradeSymbol(ftCurrency,usdtCurrency);
        TradeSymbol tradeSymbolA = new TradeSymbol(ftCurrency,ethCurrency);
        TradeSymbol tradeSymbolC = new TradeSymbol(ethCurrency,usdtCurrency);
        DepthData depthDataA=new  DepthData();
        depthDataA.setAsk_1_price("100");
        depthDataA.setBid_1_price("100");
        depthDataA.setAsk_1_qty("100");
        depthDataA.setBid_1_qty("100");
        depthDataA.tradeSymbol=tradeSymbolA;
        DepthData depthDataB=new  DepthData();
        depthDataB.tradeSymbol=tradeSymbolB;
        depthDataB.setAsk_1_price("100");
        depthDataB.setBid_1_price("100");
        depthDataB.setAsk_1_qty("100");
        depthDataB.setBid_1_qty("100");
        DepthData depthDataC=new  DepthData();
        depthDataC.tradeSymbol=tradeSymbolC;
        depthDataC.setAsk_1_price("100");
        depthDataC.setBid_1_price("100");
        depthDataC.setAsk_1_qty("100");
        depthDataC.setBid_1_qty("100");


        GainBaseTradeTask GainBaseTradeTask = new GainBaseBSSTradeTask(null,null,null,
                null,null,null,null,null,null,null,null);
        TradeContext tradeContext = GainBaseTradeTask.buildTradeContext(depthDataA,depthDataB,depthDataC);
        Assert.assertTrue(tradeContext.orderA.symbol==tradeSymbolB);
        Assert.assertTrue(tradeContext.orderB.symbol==tradeSymbolA);
        Assert.assertTrue(tradeContext.orderC.symbol==tradeSymbolC);
        Assert.assertTrue(tradeContext.orderA.tradeType== TradeType.buy);
        Assert.assertTrue(tradeContext.orderB.tradeType== TradeType.sell);
        Assert.assertTrue(tradeContext.orderC.tradeType== TradeType.sell);
    }
    @Test
    public void buildTradeContext3() throws Exception {
        Currency ftCurrency = Currency.ft;
        Currency usdtCurrency = Currency.usdt;
        Currency ethCurrency = Currency.eth;

        TradeSymbol tradeSymbolB = new TradeSymbol(ftCurrency,usdtCurrency);
        TradeSymbol tradeSymbolC = new TradeSymbol(ftCurrency,ethCurrency);
        TradeSymbol tradeSymbolA = new TradeSymbol(ethCurrency,usdtCurrency);
        DepthData depthDataA=new  DepthData();
        depthDataA.setAsk_1_price("100");
        depthDataA.setBid_1_price("100");
        depthDataA.setAsk_1_qty("100");
        depthDataA.setBid_1_qty("100");
        depthDataA.tradeSymbol=tradeSymbolA;
        DepthData depthDataB=new  DepthData();
        depthDataB.tradeSymbol=tradeSymbolB;
        depthDataB.setAsk_1_price("100");
        depthDataB.setBid_1_price("100");
        depthDataB.setAsk_1_qty("100");
        depthDataB.setBid_1_qty("100");
        DepthData depthDataC=new  DepthData();
        depthDataC.tradeSymbol=tradeSymbolC;
        depthDataC.setAsk_1_price("100");
        depthDataC.setBid_1_price("100");
        depthDataC.setAsk_1_qty("100");
        depthDataC.setBid_1_qty("100");


        GainBaseTradeTask GainBaseTradeTask = new GainBaseBSSTradeTask(null,null,null,
                null,null,null,null,null,null,null,null);
        TradeContext tradeContext = GainBaseTradeTask.buildTradeContext(depthDataA,depthDataB,depthDataC);
        Assert.assertTrue(tradeContext.orderA.symbol==tradeSymbolB);
        Assert.assertTrue(tradeContext.orderB.symbol==tradeSymbolC);
        Assert.assertTrue(tradeContext.orderC.symbol==tradeSymbolA);
        Assert.assertTrue(tradeContext.orderA.tradeType== TradeType.buy);
        Assert.assertTrue(tradeContext.orderB.tradeType== TradeType.sell);
        Assert.assertTrue(tradeContext.orderC.tradeType== TradeType.sell);
    }
    @Test
    public void buildTradeContext4() throws Exception {
        Currency ftCurrency = Currency.ft;
        Currency usdtCurrency = Currency.usdt;
        Currency ethCurrency = Currency.eth;

        TradeSymbol tradeSymbolB = new TradeSymbol(ftCurrency,usdtCurrency);
        TradeSymbol tradeSymbolA = new TradeSymbol(ftCurrency,ethCurrency);
        TradeSymbol tradeSymbolC = new TradeSymbol(ethCurrency,usdtCurrency);
        DepthData depthDataA=new  DepthData();
        depthDataA.setAsk_1_price("100");
        depthDataA.setBid_1_price("100");
        depthDataA.setAsk_1_qty("100");
        depthDataA.setBid_1_qty("100");
        depthDataA.tradeSymbol=tradeSymbolA;
        DepthData depthDataB=new  DepthData();
        depthDataB.tradeSymbol=tradeSymbolB;
        depthDataB.setAsk_1_price("100");
        depthDataB.setBid_1_price("100");
        depthDataB.setAsk_1_qty("100");
        depthDataB.setBid_1_qty("100");
        DepthData depthDataC=new  DepthData();
        depthDataC.tradeSymbol=tradeSymbolC;
        depthDataC.setAsk_1_price("100");
        depthDataC.setBid_1_price("100");
        depthDataC.setAsk_1_qty("100");
        depthDataC.setBid_1_qty("100");


        GainBaseTradeTask GainBaseTradeTask = new GainBaseBSSTradeTask(null,null,null,
                null,null,null,null,null,null,null,null);
        TradeContext tradeContext = GainBaseTradeTask.buildTradeContext(depthDataA,depthDataB,depthDataC);
        Assert.assertTrue(tradeContext.orderA.symbol==tradeSymbolB);
        Assert.assertTrue(tradeContext.orderB.symbol==tradeSymbolA);
        Assert.assertTrue(tradeContext.orderC.symbol==tradeSymbolC);
        Assert.assertTrue(tradeContext.orderA.tradeType== TradeType.buy);
        Assert.assertTrue(tradeContext.orderB.tradeType== TradeType.sell);
        Assert.assertTrue(tradeContext.orderC.tradeType== TradeType.sell);
    }

    @Test
    public void buildTradeContextInner() throws Exception {
    }

}