package com.auto.model;

import com.auto.model.entity.*;
import com.auto.trade.Application;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;

/**
 * Created by gof on 18/6/19.
 */
public class ApplicationTest {

    Balance balanceAtStart_Target = null;
    Balance balanceAtStart_Base = null;
    Balance balanceAtEnd_Target = null;
    Balance balanceAtEnd_Base = null;
    Currency targetCurrency = Currency.bnb;
    Currency baseCurrency = Currency.usdt;
    TradeSymbol tradeSymbol = new TradeSymbol(targetCurrency,baseCurrency);

    @Test
    public void testCaculate00(){
        List<Element> tradingList_Buy = new ArrayList<>();
        List<Element> tradingList_Sell = new ArrayList<>();

        Element elementB00 = new Element(0,tradeSymbol,TradeType.buy);
        elementB00.targetAmount= new BigDecimal("0.01");
        elementB00.excutedAmount= new BigDecimal("0.01");
        tradingList_Buy.add(elementB00);

        Element elementS00 = new Element(0,tradeSymbol,TradeType.sell);
        elementS00.targetAmount= new BigDecimal("0.01");
        elementS00.excutedAmount= new BigDecimal("0.01");
        tradingList_Sell.add(elementS00);


        QuantitativeResult quantitativeResult = new QuantitativeResult( balanceAtStart_Target,  balanceAtStart_Base,  balanceAtEnd_Target,  balanceAtEnd_Base, tradingList_Buy,tradingList_Sell);
        Application application = new Application();

        Application.Result result =application.caculate(quantitativeResult);
        Assert.assertTrue(result.allAmount.compareTo(new BigDecimal("0.01"))==0);
        Assert.assertTrue(result.diff.compareTo(new BigDecimal(0))==0);
        Assert.assertTrue(result.totalPairDone.compareTo(new BigDecimal("0.01"))==0);
        Assert.assertTrue(result.pairDone.compareTo(new BigDecimal("0.01"))==0);
        Assert.assertTrue(result.partPairDone.compareTo(new BigDecimal(0))==0);
        Assert.assertTrue(result.lossRate.compareTo(new BigDecimal(0))==0);
        Assert.assertTrue(result.successRate.compareTo(new BigDecimal(1))==0);
    }
    @Test
    public void testCaculate01(){
        List<Element> tradingList_Buy = new ArrayList<>();
        List<Element> tradingList_Sell = new ArrayList<>();

        Element elementB00 = new Element(0,tradeSymbol,TradeType.buy);
        elementB00.targetAmount= new BigDecimal("0.01");
        elementB00.excutedAmount= new BigDecimal("0.01");
        Element elementS00 = new Element(0,tradeSymbol,TradeType.sell);
        elementS00.targetAmount= new BigDecimal("0.01");
        elementS00.excutedAmount= new BigDecimal("0.01");
        tradingList_Sell.add(elementS00);


        tradingList_Buy.add(elementB00);
        Element elementB01 = new Element(0,tradeSymbol,TradeType.buy);
        elementB01.targetAmount= new BigDecimal("0.01");
        elementB01.excutedAmount= new BigDecimal("0.01");
        tradingList_Buy.add(elementB01);
        Element elementS01 = new Element(0,tradeSymbol,TradeType.sell);
        elementS01.targetAmount= new BigDecimal("0.01");
        elementS01.excutedAmount= new BigDecimal("0");
        tradingList_Sell.add(elementS01);


        QuantitativeResult quantitativeResult = new QuantitativeResult( balanceAtStart_Target,  balanceAtStart_Base,  balanceAtEnd_Target,  balanceAtEnd_Base, tradingList_Buy,tradingList_Sell);
        Application application = new Application();

        Application.Result result =application.caculate(quantitativeResult);
        Assert.assertTrue(result.allAmount.compareTo(new BigDecimal("0.02"))==0);
        Assert.assertTrue(result.diff.compareTo(new BigDecimal("0.01"))==0);
        Assert.assertTrue(result.totalPairDone.compareTo(new BigDecimal("0.01"))==0);
        Assert.assertTrue(result.pairDone.compareTo(new BigDecimal("0.01"))==0);
        Assert.assertTrue(result.partPairDone.compareTo(new BigDecimal(0))==0);
        Assert.assertTrue(result.lossRate.compareTo(new BigDecimal(0.5))==0);
        Assert.assertTrue(result.successRate.compareTo(new BigDecimal(0.5))==0);
    }

    @Test
    public void testCaculate02(){
        List<Element> tradingList_Buy = new ArrayList<>();
        List<Element> tradingList_Sell = new ArrayList<>();

        Element elementB00 = new Element(0,tradeSymbol,TradeType.buy);
        elementB00.targetAmount= new BigDecimal("0.01");
        elementB00.excutedAmount= new BigDecimal("0.01");
        Element elementS00 = new Element(0,tradeSymbol,TradeType.sell);
        elementS00.targetAmount= new BigDecimal("0.01");
        elementS00.excutedAmount= new BigDecimal("0.01");
        tradingList_Sell.add(elementS00);


        tradingList_Buy.add(elementB00);
        Element elementB01 = new Element(0,tradeSymbol,TradeType.buy);
        elementB01.targetAmount= new BigDecimal("0.01");
        elementB01.excutedAmount= new BigDecimal("0");
        tradingList_Buy.add(elementB01);
        Element elementS01 = new Element(0,tradeSymbol,TradeType.sell);
        elementS01.targetAmount= new BigDecimal("0.01");
        elementS01.excutedAmount= new BigDecimal("0");
        tradingList_Sell.add(elementS01);


        QuantitativeResult quantitativeResult = new QuantitativeResult( balanceAtStart_Target,  balanceAtStart_Base,  balanceAtEnd_Target,  balanceAtEnd_Base, tradingList_Buy,tradingList_Sell);
        Application application = new Application();

        Application.Result result =application.caculate(quantitativeResult);
        Assert.assertTrue(result.allAmount.compareTo(new BigDecimal("0.02"))==0);
        Assert.assertTrue(result.diff.compareTo(new BigDecimal("0"))==0);
        Assert.assertTrue(result.totalPairDone.compareTo(new BigDecimal("0.01"))==0);
        Assert.assertTrue(result.pairDone.compareTo(new BigDecimal("0.01"))==0);
        Assert.assertTrue(result.partPairDone.compareTo(new BigDecimal(0))==0);
        Assert.assertTrue(result.lossRate.compareTo(new BigDecimal(0))==0);
        Assert.assertTrue(result.successRate.compareTo(new BigDecimal("0.5"))==0);
    }
    @Test
    public void testCaculate03(){
        List<Element> tradingList_Buy = new ArrayList<>();
        List<Element> tradingList_Sell = new ArrayList<>();

        Element elementB00 = new Element(0,tradeSymbol,TradeType.buy);
        elementB00.targetAmount= new BigDecimal("0.01");
        elementB00.excutedAmount= new BigDecimal("0.01");
        Element elementS00 = new Element(0,tradeSymbol,TradeType.sell);
        elementS00.targetAmount= new BigDecimal("0.01");
        elementS00.excutedAmount= new BigDecimal("0.005");
        tradingList_Sell.add(elementS00);


        tradingList_Buy.add(elementB00);
        Element elementB01 = new Element(0,tradeSymbol,TradeType.buy);
        elementB01.targetAmount= new BigDecimal("0.01");
        elementB01.excutedAmount= new BigDecimal("0.01");
        tradingList_Buy.add(elementB01);
        Element elementS01 = new Element(0,tradeSymbol,TradeType.sell);
        elementS01.targetAmount= new BigDecimal("0.01");
        elementS01.excutedAmount= new BigDecimal("0");
        tradingList_Sell.add(elementS01);


        QuantitativeResult quantitativeResult = new QuantitativeResult( balanceAtStart_Target,  balanceAtStart_Base,  balanceAtEnd_Target,  balanceAtEnd_Base, tradingList_Buy,tradingList_Sell);
        Application application = new Application();

        Application.Result result =application.caculate(quantitativeResult);
        Assert.assertTrue(result.allAmount.compareTo(new BigDecimal("0.02"))==0);
        Assert.assertTrue(result.diff.compareTo(new BigDecimal("0.015"))==0);
        Assert.assertTrue(result.totalPairDone.compareTo(new BigDecimal(0))==0);
        Assert.assertTrue(result.pairDone.compareTo(new BigDecimal("0.005"))==0);
        Assert.assertTrue(result.partPairDone.compareTo(new BigDecimal("0.005"))==0);
        Assert.assertTrue(result.lossRate.compareTo(new BigDecimal("0.75"))==0);
        Assert.assertTrue(result.successRate.compareTo(new BigDecimal("0.25"))==0);
    }

    @Test
    public void testCaculate04(){
        List<Element> tradingList_Buy = new ArrayList<>();
        List<Element> tradingList_Sell = new ArrayList<>();

        Element elementB00 = new Element(0,tradeSymbol,TradeType.buy);
        elementB00.targetAmount= new BigDecimal("0.01");
        elementB00.excutedAmount= new BigDecimal("0.01");
        Element elementS00 = new Element(0,tradeSymbol,TradeType.sell);
        elementS00.targetAmount= new BigDecimal("0.01");
        elementS00.excutedAmount= new BigDecimal("0.005");
        tradingList_Sell.add(elementS00);


        tradingList_Buy.add(elementB00);
        Element elementB01 = new Element(0,tradeSymbol,TradeType.buy);
        elementB01.targetAmount= new BigDecimal("0.01");
        elementB01.excutedAmount= new BigDecimal("0.01");
        tradingList_Buy.add(elementB01);
        Element elementS01 = new Element(0,tradeSymbol,TradeType.sell);
        elementS01.targetAmount= new BigDecimal("0.01");
        elementS01.excutedAmount= new BigDecimal("0.004");
        tradingList_Sell.add(elementS01);


        QuantitativeResult quantitativeResult = new QuantitativeResult( balanceAtStart_Target,  balanceAtStart_Base,  balanceAtEnd_Target,  balanceAtEnd_Base, tradingList_Buy,tradingList_Sell);
        Application application = new Application();

        Application.Result result =application.caculate(quantitativeResult);
        Assert.assertTrue(result.allAmount.compareTo(new BigDecimal("0.02"))==0);
        Assert.assertTrue(result.diff.compareTo(new BigDecimal("0.011"))==0);
        Assert.assertTrue(result.totalPairDone.compareTo(new BigDecimal(0))==0);
        Assert.assertTrue(result.pairDone.compareTo(new BigDecimal("0.009"))==0);
        Assert.assertTrue(result.partPairDone.compareTo(new BigDecimal("0.009"))==0);
        Assert.assertTrue(result.lossRate.compareTo(new BigDecimal("0.55"))==0);
        Assert.assertTrue(result.successRate.compareTo(new BigDecimal("0.45"))==0);
    }


    @Test
    public void testcaculateFreeRateFromLossRate02(){
        BigDecimal baseFreeRate=new BigDecimal("0.0005");
        BigDecimal lossRate=new BigDecimal(0.25);
        BigDecimal successRate=new BigDecimal(0.5);

        Application application = new Application();
        BigDecimal totalFreeRate = application.caculateFreeRateFromLossRate( baseFreeRate, lossRate, successRate);
        Assert.assertTrue(new BigDecimal("0.0015").compareTo(totalFreeRate)==0);
    }

    @Test
    public void testcaculateFreeRateFromLossRate03(){
        BigDecimal baseFreeRate=new BigDecimal("0.0005");
        BigDecimal lossRate=new BigDecimal(0.4);
        BigDecimal successRate=new BigDecimal(0.3);

        Application application = new Application();
        BigDecimal totalFreeRate = application.caculateFreeRateFromLossRate( baseFreeRate, lossRate, successRate);
        Assert.assertTrue(new BigDecimal("0.002333334").subtract(totalFreeRate).abs().compareTo(new BigDecimal("0.000001"))<0);
    }
}
