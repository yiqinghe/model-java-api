package com.auto.model.tripleTradeSymbol;

import com.auto.model.common.AbstractTask;
import com.auto.model.common.Api;
import com.auto.model.entity.Config;
import com.auto.model.entity.Element;
import com.auto.model.entity.TradeSymbol;
import com.auto.model.tripleTradeSymbol.AbstractModel;
import com.auto.model.singleTradeSymbol.task.CancelTask;
import com.auto.model.singleTradeSymbol.task.LazyCancelTradeTask;
import com.auto.model.singleTradeSymbol.task.QueryAndWaitDoneTask;
import com.auto.model.tripleTradeSymbol.task.GainBaseBSSTradeTask;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by gof on 18/6/30.
 * 独立线程、延时取消订单
 */
public class TripleGrainBase1Model extends AbstractModel {

    public TripleGrainBase1Model(Api api,  TradeSymbol symbolA,TradeSymbol symbolB
            ,TradeSymbol symbolC){
        super(api,   symbolA, symbolB
                , symbolC);
    }

    @Override
    public void buildTask(List<AbstractTask> taskList ) {
        for(int i = 0; i< Config.concurrency; i++){
            GainBaseBSSTradeTask gainBaseBSSTradeTask = new GainBaseBSSTradeTask( tradingList_A, tradingList_B,  tradingList_C,  api,
                     listLockA,  listLockB,  listLockC,  symbolA,  symbolB,  symbolC,
                     executor);
            taskList.add(gainBaseBSSTradeTask);
            gainBaseBSSTradeTask.start();
        }

    }
}
