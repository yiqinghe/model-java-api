package com.auto.model.singleTradeSymbol;

import com.auto.model.common.Api;
import com.auto.model.common.ApiDemo;
import com.auto.model.entity.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by caigaonian870 on 18/6/30.
 */
public class AbstractModelTest {
    private AbstractModel model;

    @Before
    public void buildModel(){
        Currency targetCurrency = Currency.eth;
        Currency baseCurrency = Currency.usdt;
        Config.quota="0.01";
        Config.concurrency=1;
        Config.totalFeeRate =new BigDecimal("0.0015");
        Config.baseFeeRate = new BigDecimal("0.0005");
        Config.waitOrderDoneMillSec=5000;


        TradeSymbol tradeSymbol = new TradeSymbol(targetCurrency,baseCurrency);
        Api api = new ApiDemo();
        model = new LazyCancelModel(api,tradeSymbol);
        //Assert.assertFalse(model.isFinish);
    }
    @Test
    public void init(){
        Api api = mock(Api.class);
        model.api=api;

        Balance balance = new Balance(Currency.eth);
        balance.amount="2";
        when(api.getBalance(Currency.eth)).thenReturn(balance);

        Balance balanceUsdt = new Balance(Currency.usdt);
        balanceUsdt.amount="10000";
        when(api.getBalance(Currency.usdt)).thenReturn(balanceUsdt);
        when(api.getTargetAmountForBuy(new BigDecimal("0.01"))).thenReturn(new BigDecimal("0.01"));
        when(api.getTargetAmountForSell(new BigDecimal("0.01"))).thenReturn(new BigDecimal("0.01"));



        model.init();
        Assert.assertTrue(model.tradingList_Buy.size()==model.tradingList_Sell.size());

        model.init();
        Assert.assertTrue(model.tradingList_Buy.size()==200);
        Element elementBuy =model.tradingList_Buy.get(0);
        Assert.assertTrue(elementBuy.tradeStatus== TradeStatus.init);
        Assert.assertTrue(elementBuy.tradeType== TradeType.buy);

        Assert.assertTrue(elementBuy.targetAmount.equals(new BigDecimal("0.01")));
        Assert.assertTrue(elementBuy.targetAmount.toString().equals("0.01"));
        Assert.assertFalse(elementBuy.targetAmount.toString().equals("0.010"));



    }

    @Test
    public void periodStart() throws Exception {
    }

    @Test
    public void buildTask() throws Exception {
    }

}