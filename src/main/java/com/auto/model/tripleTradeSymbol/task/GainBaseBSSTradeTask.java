package com.auto.model.tripleTradeSymbol.task;

import com.auto.model.common.Api;
import com.auto.model.entity.*;
import com.auto.trade.entity.DepthData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by gof on 18/6/30.
 * 获取base币策略

 * FK FT TK
 * 用K买－－卖F－－卖T，最后得K
 */
public class GainBaseBSSTradeTask extends GainBaseTradeTask {

    private static final Logger log = LoggerFactory.getLogger(GainBaseBSSTradeTask.class);

    public GainBaseBSSTradeTask(List<Element> tradingList_A, List<Element> tradingList_B, List<Element> tradingList_C, Api api,
                                Object listLockA, Object listLockB, Object listLockC, TradeSymbol symbolA, TradeSymbol symbolB, TradeSymbol symbolC,
                                ExecutorService executor){
        super(tradingList_A, tradingList_B,tradingList_C,  api,
                 listLockA,  listLockB,  listLockC, symbolA, symbolB, symbolC,
                 executor);
    }

    @Override
    public String getTaskName() {
        return "GainBaseBSSTradeTask";
    }

    @Override
    public TradeContext buildTradeContextInner(DepthData depthDataA, DepthData depthDataB, DepthData depthDataC){
        log.info("buildTradeContextInner start >>>>>>");

        TradeContext tradeContext = new TradeContext();
        tradeContext.canTrade=false;
        BigDecimal left = new BigDecimal(depthDataA.getAsk_1_price()).divide(new BigDecimal(depthDataB.getBid_1_price()),Config.priceScale, RoundingMode.UP);
        BigDecimal right = (new BigDecimal(depthDataC.bid_1_price));
        // todo 判断公式
        if(left.compareTo(right) < 0){
           // tradeContext.canTrade=true;
            log.warn("buildTradeContextInner match formula,proportion:{},A amount:{},B amount:{},C amount:{} >>>>>>",
                    left.divide(right,Config.priceScale, RoundingMode.UP).setScale(Config.priceScale).toString(),
                    depthDataA.ask_1_qty,depthDataB.bid_1_qty,depthDataC.bid_1_qty);
            // 计算最小量。全部折算成F，估算：
            BigDecimal cAmountForF = new BigDecimal(depthDataC.bid_1_qty)
                    .divide(new BigDecimal(depthDataB.getBid_1_price()),Config.priceScale, RoundingMode.UP);
            BigDecimal aAmountForF = new BigDecimal(depthDataA.ask_1_qty);
            BigDecimal bAmountForF = new BigDecimal(depthDataB.bid_1_qty);
            BigDecimal minAmountForF = aAmountForF;
            // 取最小值
            if(aAmountForF.compareTo(bAmountForF)<0){
                if(aAmountForF.compareTo(cAmountForF)<0){
                    minAmountForF = aAmountForF;
                }else{
                    minAmountForF = cAmountForF;

                }
            }else{
                if(bAmountForF.compareTo(cAmountForF)<0){
                    minAmountForF = bAmountForF;
                }else{
                    minAmountForF = cAmountForF;
                }

            }
            log.warn("buildTradeContextInner match formula:proportion:{}:deal amount:{}: >>>>>>",
                    left.divide(right,Config.priceScale, RoundingMode.UP).setScale(Config.priceScale).toString(),
                    minAmountForF);
        }
        Order orderA=new Order(depthDataA.tradeSymbol,TradeType.buy,"","");
        Order orderB=new Order(depthDataB.tradeSymbol,TradeType.sell,"","");
        Order orderC=new Order(depthDataC.tradeSymbol,TradeType.sell,"","");
        tradeContext.orderA=orderA;
        tradeContext.orderB=orderB;
        tradeContext.orderC=orderC;
        return tradeContext;
    }

}
