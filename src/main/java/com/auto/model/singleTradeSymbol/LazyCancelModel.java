package com.auto.model.singleTradeSymbol;

import com.auto.model.common.Api;
import com.auto.model.entity.Config;
import com.auto.model.entity.TradeSymbol;
import com.auto.model.common.AbstractTask;
import com.auto.model.singleTradeSymbol.task.CancelTask;
import com.auto.model.singleTradeSymbol.task.QueryAndWaitDoneTask;
import com.auto.model.singleTradeSymbol.task.LazyCancelTradeTask;

import java.util.List;

/**
 * Created by gof on 18/6/30.
 * 独立线程、延时取消订单
 */
public class LazyCancelModel extends AbstractModel {

    public LazyCancelModel(Api api, TradeSymbol symbol){
        super(api, symbol);
    }

    @Override
    public void buildTask(List<AbstractTask> taskList ) {
        for(int i = 0; i< Config.concurrency; i++){
            LazyCancelTradeTask lazyCancelTradeTask = new LazyCancelTradeTask(tradingList_Buy,tradingList_Sell, api,
                    listLockB, listLockS, symbol,
                    executor);
            taskList.add(lazyCancelTradeTask);
            lazyCancelTradeTask.start();
        }

        CancelTask cancelTask = new CancelTask(tradingList_Buy,tradingList_Sell, api,
                listLockB, listLockS);
        taskList.add(cancelTask);
        cancelTask.start();

        QueryAndWaitDoneTask queryAndWaitDoneTask = new QueryAndWaitDoneTask(tradingList_Buy,tradingList_Sell, api,
                listLockB, listLockS);
        taskList.add(queryAndWaitDoneTask);
        queryAndWaitDoneTask.start();
    }
}
