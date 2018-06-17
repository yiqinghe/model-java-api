package com.auto.model.entity;

import java.math.BigDecimal;

/**
 * Created by caigaonian870 on 18/6/17.
 */
public class Config {
    // 目标币的交易池大小
    public static int tagetCurrencyPoolSize;

    // 基本币的交易池大小
    public static int baseCurrencyPoolSize;

    // 量化粒度(多少份)
    public static int quota;

    // 量化交易频率
    public static int rateSec;

    // 并发线程
    public static int concurrency;

    // 最多waitOrderDoneMillSec毫秒后，买卖订单都必须要有终态
    public static int waitOrderDoneMillSec;

    public static BigDecimal tradeFee;


}
