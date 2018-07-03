package com.auto.model.tripleTradeSymbol.task;

import com.auto.model.common.Api;
import com.auto.model.entity.*;
import com.auto.trade.entity.DepthData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by gof on 18/6/30.
 * 获取base币策略
 */
public abstract class GainBaseTradeTask extends LazyCancelTradeTask {

    private static final Logger log = LoggerFactory.getLogger(GainBaseTradeTask.class);

    public GainBaseTradeTask(List<Element> tradingList_A, List<Element> tradingList_B, List<Element> tradingList_C, Api api,
                             Object listLockA, Object listLockB, Object listLockC, TradeSymbol symbolA, TradeSymbol symbolB, TradeSymbol symbolC,
                             ExecutorService executor){
        super(tradingList_A, tradingList_B,tradingList_C,  api,
                 listLockA,  listLockB,  listLockC, symbolA, symbolB, symbolC,
                 executor);
    }

    @Override
    public String getTaskName() {
        return "GainBaseTradeTask";
    }

    @Override
    TradeContext buildTradeContext(DepthData depthDataA, DepthData depthDataB, DepthData depthDataC) {
        // 选出中间过渡交易对,将顺序定好,FK FT TK;
        // 那么FT就为B交易对，A的目标币为B的target币、C的目标币为B的base币
        Currency aBase = depthDataA.tradeSymbol.baseCurrency;
        Currency bBase = depthDataB.tradeSymbol.baseCurrency;
        Currency cBase = depthDataC.tradeSymbol.baseCurrency;
        TradeContext tradeContext= null;
        if(aBase.getCurrency().equals(bBase.getCurrency())){
            DepthData middle = depthDataC;
            if(middle.tradeSymbol.targetCurrency==depthDataA.tradeSymbol.targetCurrency){
                tradeContext=buildTradeContextInner(depthDataA,middle,depthDataB);
            }else{
                tradeContext=buildTradeContextInner(depthDataB,middle,depthDataA);
            }

        }
        else if(aBase.getCurrency().equals(cBase.getCurrency())){
            DepthData middle = depthDataB;
            if(middle.tradeSymbol.targetCurrency==depthDataA.tradeSymbol.targetCurrency) {
                tradeContext=buildTradeContextInner(depthDataA,middle,depthDataC);
            }else{
                tradeContext=buildTradeContextInner(depthDataC,middle,depthDataA);
            }
        }
        else if(bBase.getCurrency().equals(cBase.getCurrency())){
            DepthData middle = depthDataA;
            if(middle.tradeSymbol.targetCurrency==depthDataB.tradeSymbol.targetCurrency) {
                tradeContext=buildTradeContextInner(depthDataB,middle,depthDataC);
            }else{
                tradeContext=buildTradeContextInner(depthDataC,middle,depthDataB);
            }
        }

        return tradeContext;
    }
    abstract TradeContext buildTradeContextInner(DepthData depthDataA, DepthData depthDataB, DepthData depthDataC);

}
