package com.auto.model;

import com.auto.model.entity.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

/**
 * Created by caigaonian870 on 18/6/17.
 */
public class ModelTest {

    private Model model;


    @Before
    public void buildModel(){
        Currency targetCurrency = Currency.eth;
        Currency baseCurrency = Currency.usdt;
        Config.quota=100;
        Config.tagetCurrencyPoolSize=1;
        Config.baseCurrencyPoolSize=5000;
        Config.concurrency=1;
        Config.rateSec=1;
        Config.tradeFee=new BigDecimal("0.001");
        Config.waitOrderDoneMillSec=5000;


        TradeSymbol tradeSymbol = new TradeSymbol(targetCurrency,baseCurrency);
        Api api = new ApiDemo();
        model = new Model(api,tradeSymbol);
        Assert.assertFalse(model.isFinish);
    }
    @Test
    public void testInit(){
        model.init();
        Assert.assertTrue(model.tradingList_Buy.size()==model.tradingList_Sell.size());

        model.init();
        Assert.assertTrue(model.tradingList_Buy.size()==100);
        Element elementBuy =model.tradingList_Buy.get(0);
        Assert.assertTrue(elementBuy.tradeStatus== TradeStatus.init);
        Assert.assertTrue(elementBuy.tradeType== TradeType.buy);
        Assert.assertTrue(elementBuy.targetAmount.equals(new BigDecimal("0.01")));

        Assert.assertTrue(elementBuy.targetAmount.toString().equals("0.01"));
        Assert.assertFalse(elementBuy.targetAmount.toString().equals("0.010"));

        // mock
        Api api = mock(Api.class);
        Balance balance = new Balance(Currency.eth);
        balance.amount="2";
        when(api.getBalance(Currency.eth)).thenReturn(balance);

        Balance balanceUsdt = new Balance(Currency.usdt);
        balanceUsdt.amount="10000";
        when(api.getBalance(Currency.usdt)).thenReturn(balance);

        model.api=api;
        model.init();
        Assert.assertTrue(model.balanceAtStart_Target.amount.equals("2"));
        //Assert.assertTrue(model.balanceAtStart_Base.amount.equals("10000"));

    }

    @Test
    public void testApi() throws InterruptedException {
        Api api = new ApiDemo();
        DepthData depthData = new DepthData();
        depthData.setAsk_1_price("3000");
        depthData.setBid_1_price("2900");
        TradeContext tradeContext = api.buildTradeContext(depthData);
        Assert.assertTrue(tradeContext.canTrade==true);
        Assert.assertTrue(tradeContext.buyPrice.equals("2900") && tradeContext.sellPrice.equals("3000"));

        depthData.setAsk_1_price("3000");
        depthData.setBid_1_price("2999");
        tradeContext = api.buildTradeContext(depthData);
        Assert.assertTrue(tradeContext.canTrade==false);

        depthData.setAsk_1_price("3000");
        depthData.setBid_1_price("199");
        tradeContext = api.buildTradeContext(depthData);
        Assert.assertTrue(tradeContext.canTrade==true);

        depthData.setAsk_1_price("3000");
        depthData.setBid_1_price("3000.1");
        tradeContext = api.buildTradeContext(depthData);
        Assert.assertTrue(tradeContext.canTrade==false);

        Config.tradeFee=new BigDecimal("0.1");

        depthData.setAsk_1_price("3000");
        depthData.setBid_1_price("2900");
        tradeContext = api.buildTradeContext(depthData);
        Assert.assertTrue(tradeContext.canTrade==false);

    }

    @Test
    public void testQueryBuyOrderStatus(){
        Api api = mock(Api.class);
        model.api=api;

        Order orderInput = new Order(model.symbol,TradeType.buy,"3000","0.01");
        Order order = new Order(model.symbol,TradeType.buy,"3000","0.01");
        order.orderId="00000001";
        order.tradeStatus=TradeStatus.trading;
        when(api.queryOrder(orderInput)).thenReturn(order);

        model.taskForTestHook.tradeTime=10000;
        model.taskForTestHook.orderBuy=orderInput;
        model.taskForTestHook.elementB=new Element(0,model.symbol,TradeType.buy);
        model.taskForTestHook.queryBuyOrderStatus();
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("isCancelingForBuy").equals("true"));
        Assert.assertTrue(model.taskForTestHook.orderBuy.orderId.equals("00000001"));
        Assert.assertTrue(model.taskForTestHook.elementB.isCanceling());

        model.taskForTestHook.orderBuy=orderInput;
        model.taskForTestHook.elementB=new Element(0,model.symbol,TradeType.buy);
        model.taskForTestHook.elementB.setCanceling(true);
        model.taskForTestHook.queryBuyOrderStatus();
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("isCancelingForBuy").equals("false"));
        Assert.assertTrue(model.taskForTestHook.elementB.isCanceling());


        model.taskForTestHook.tradeTime=System.currentTimeMillis()+100000000;
        model.taskForTestHook.orderBuy=orderInput;
        model.taskForTestHook.elementB=new Element(0,model.symbol,TradeType.buy);
        model.taskForTestHook.queryBuyOrderStatus();
        Assert.assertTrue(model.taskForTestHook.orderBuy.orderId==null);
        Assert.assertFalse(model.taskForTestHook.elementB.isCanceling());


    }
    @Test
    public void testQueryBuyOrderStatus2(){
        Api api = mock(Api.class);
        model.api=api;

        Order orderInput = new Order(model.symbol,TradeType.buy,"3000","0.01");
        Order order = new Order(model.symbol,TradeType.buy,"3000","0.01");
        order.orderId="00000001";
        order.tradeStatus=TradeStatus.trading;
        when(api.queryOrder(orderInput)).thenReturn(order);

        model.taskForTestHook.tradeTime=System.currentTimeMillis()-10;
        model.taskForTestHook.orderBuy=orderInput;
        model.taskForTestHook.elementB=new Element(0,model.symbol,TradeType.buy);
        model.taskForTestHook.queryBuyOrderStatus();
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("isCancelingForBuy")==null);

        order.tradeStatus=TradeStatus.done;
        order.excutedAmount="0.01";
        when(api.queryOrder(orderInput)).thenReturn(order);

        model.taskForTestHook.tradeTime=10000;
        model.taskForTestHook.orderBuy=orderInput;
        model.taskForTestHook.elementB=new Element(0,model.symbol,TradeType.buy);
        model.taskForTestHook.queryBuyOrderStatus();
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("isCancelingForBuy")==null);
        Assert.assertTrue(model.taskForTestHook.orderBuy==null);
        Assert.assertTrue(model.taskForTestHook.elementB==null);
    }


    @Test
    public void testQuerySellOrderStatus(){
        Api api = mock(Api.class);
        model.api=api;

        Order orderInput = new Order(model.symbol,TradeType.sell,"3000","0.01");
        Order order = new Order(model.symbol,TradeType.sell,"3000","0.01");
        order.orderId="00000001";
        order.tradeStatus=TradeStatus.trading;
        when(api.queryOrder(orderInput)).thenReturn(order);

        model.taskForTestHook.tradeTime=10000;
        model.taskForTestHook.orderSell=orderInput;
        model.taskForTestHook.elementS=new Element(0,model.symbol,TradeType.sell);
        model.taskForTestHook.querySellOrderStatus();
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("isCancelingForSell").equals("true"));
        Assert.assertTrue(model.taskForTestHook.orderSell.orderId.equals("00000001"));
        Assert.assertTrue(model.taskForTestHook.elementS.isCanceling());

        model.taskForTestHook.orderSell=orderInput;
        model.taskForTestHook.elementS=new Element(0,model.symbol,TradeType.sell);
        model.taskForTestHook.elementS.setCanceling(true);
        model.taskForTestHook.querySellOrderStatus();
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("isCancelingForSell").equals("false"));
        Assert.assertTrue(model.taskForTestHook.elementS.isCanceling());


        model.taskForTestHook.tradeTime=System.currentTimeMillis()+100000000;
        model.taskForTestHook.orderSell=orderInput;
        model.taskForTestHook.elementS=new Element(0,model.symbol,TradeType.sell);
        model.taskForTestHook.querySellOrderStatus();
        Assert.assertTrue(model.taskForTestHook.orderSell.orderId==null);
        Assert.assertFalse(model.taskForTestHook.elementS.isCanceling());


    }
    @Test
    public void testQuerySellOrderStatus2(){
        Api api = mock(Api.class);
        model.api=api;

        Order orderInput = new Order(model.symbol,TradeType.buy,"3000","0.01");
        Order order = new Order(model.symbol,TradeType.buy,"3000","0.01");
        order.orderId="00000001";
        order.tradeStatus=TradeStatus.trading;
        when(api.queryOrder(orderInput)).thenReturn(order);

        model.taskForTestHook.tradeTime=System.currentTimeMillis()-10;
        model.taskForTestHook.orderSell=orderInput;
        model.taskForTestHook.elementS=new Element(0,model.symbol,TradeType.sell);
        model.taskForTestHook.querySellOrderStatus();
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("isCancelingForSell")==null);

        order.tradeStatus=TradeStatus.done;
        order.excutedAmount="0.01";
        when(api.queryOrder(orderInput)).thenReturn(order);

        model.taskForTestHook.tradeTime=10000;
        model.taskForTestHook.orderSell=orderInput;
        model.taskForTestHook.elementS=new Element(0,model.symbol,TradeType.sell);
        model.taskForTestHook.querySellOrderStatus();
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("isCancelingForSell")==null);
        Assert.assertTrue(model.taskForTestHook.orderSell==null);
        Assert.assertTrue(model.taskForTestHook.elementS==null);
    }

    @Test
    public void testTaskCreateOrder() throws InterruptedException {

        Order orderInputBuy = new Order(model.symbol,TradeType.buy,"3000","0.01");
        Order orderInputSell = new Order(model.symbol,TradeType.sell,"3000","0.01");
        model.taskForTestHook.orderBuy=orderInputBuy;
        model.taskForTestHook.orderSell=orderInputSell;
        model.taskForTestHook.createOrder();
        Assert.assertTrue(model.taskForTestHook.orderBuy!=null);
        Assert.assertTrue(model.taskForTestHook.orderSell!=null);
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("buyFuture.done").equals("true"));
        Assert.assertTrue(model.taskForTestHook.mapForTestHook.get("sellFuture.done").equals("true"));
    }

}
