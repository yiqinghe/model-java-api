package com.auto.model.entity;

import java.math.BigDecimal;

/**
 * Created by gof on 18/6/17.
 */
public class Config {
    // 目标币的交易池大小
    public static String tagetCurrencyPoolSize="1";

    // 基本币的交易池大小
    public static String baseCurrencyPoolSize="100";

    // 量化粒度(多少份)
    public static int quota=1;


    // 量化交易频率
    @Deprecated
    public static int rateSec=1;

    // 并发线程
    public static int concurrency=1;

    // 最多waitOrderDoneMillSec毫秒后，买卖订单都必须要有终态
    public static int waitOrderDoneMillSec=2;

    // 深度数据有效时间范围
    public static int depthDataValideSecs=1;


    public static TradeSymbol symbol = new TradeSymbol(Currency.bnb,Currency.usdt);

    // 总手续费率
    public static BigDecimal totalFeeRate=new BigDecimal(0.001);

    // 基础手续费率
    public static BigDecimal baseFeeRate=new BigDecimal(0.0005);

    // 预估允许最大loss率，与tradeFeeRate联动
    public static BigDecimal maxLossRate=new BigDecimal(0.5);

    public static BigDecimal minSuccessRate=new BigDecimal(0.5);

    // 查询间隔时间querySleepMillSec毫秒
    public static int querySleepMillSec=200;


}
