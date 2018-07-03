package com.auto.model.common;

import com.auto.model.entity.Order;
import com.auto.model.entity.TradeSymbol;
import com.auto.model.entity.TradeType;
import com.auto.trade.entity.DepthData;

import java.util.concurrent.Callable;

/**
 * Created by gof on 18/6/30.
 * 查询任务
 */
public class QueryDepthTask implements Callable<DepthData> {

    private Api api;
    private TradeSymbol symbol;

    public QueryDepthTask(Api api, TradeSymbol symbol) {
        this.api = api;
        this.symbol=symbol;
    }

    // 返回异步任务的执行结果
    @Override
    public DepthData call() throws Exception {
        return api.getDepthData(symbol);
    }
}
