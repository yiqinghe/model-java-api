package com.auto.model.entity;

/**
 * Created by gof on 18/6/17.
 * 交易状态
 */
public enum TradeStatus {
    init,
    trading,// 已提交、部分成交
    done //取消订单、全部成交
}
