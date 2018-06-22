package com.auto.model;

import com.auto.trade.entity.DepthData;
import com.auto.model.entity.*;
import com.auto.trade.entity.OrderPrice;

import java.math.BigDecimal;

/**
 * Created by gof on 18/6/17.
 *
 * 子类实现这个接口，将数据喂给策略模型
 * 不管方式获取的，http也好，websocket也好，接口返回数据要够快。
 *
 */
public interface Api<T> {

    /**
     * 获取余额
     * @param currency
     * @return
     */
    Balance getBalance(Currency currency);

    /**
     * 获取交易深度
     * @param symbol
     * @return
     */
    DepthData getDepthData(TradeSymbol symbol);

    /**
     * 获取实时价格
     * @param symbol
     * @return
     */
    OrderPrice getOrderPrice(TradeSymbol symbol);

    /**
     * 发起交易，限价单
     * @param order
     * @return
     */
    Order buy(Order order);

    /**
     * 发起交易，限价单
     * @param order
     * @return
     */
    Order sell(Order order);

    /**
     * 发起交易，市场单GTC
     * @param order
     * @return
     */
    Order buyMarket(Order order);

    /**
     * 发起交易，市场单
     * @param order
     * @return
     */
    Order sellMarket(Order order);


    /**
     * 取消订单
     * @param order
     * @return
     */
    Order cancel(Order order);


    /**
     * 查询订单
     * @param order
     * @return
     */
    Order queryOrder(Order order);

    /**
     * 组装是否能发起交易的上下文，例如能深度数据中，买一卖一价差很大，并且返回发起交易的价格
     * @param depthData
     * @return
     */
    TradeContext buildTradeContext(DepthData depthData);

    /**
     * 获取每次买单的目标币的交易数量
     * @param originalTargetAmount
     * @return
     */
    BigDecimal getTargetAmountForBuy(BigDecimal originalTargetAmount);
    /**
     * 获取每次卖单的目标币的交易数量
     * @param originalTargetAmount
     * @return
     */
    BigDecimal getTargetAmountForSell(BigDecimal originalTargetAmount);
}
