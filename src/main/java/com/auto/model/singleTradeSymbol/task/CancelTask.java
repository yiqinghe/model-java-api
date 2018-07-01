package com.auto.model.singleTradeSymbol.task;

import com.auto.model.common.Api;
import com.auto.model.common.Util;
import com.auto.model.entity.Config;
import com.auto.model.entity.Element;
import com.auto.model.entity.TradeStatus;
import com.auto.model.common.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gof on 18/6/30.
 * 取消订单任务
 */
public class CancelTask extends Thread implements AbstractTask {
    private static final Logger log = LoggerFactory.getLogger(CancelTask.class);

    //任务是否结束
    private boolean isFinish=false;


    private List<Element> tradingList_Buy;
    private List<Element> tradingList_Sell;
    private Api api;
    private Object listLockB;
    private Object listLockS;

    public CancelTask(List<Element> tradingList_Buy,List<Element> tradingList_Sell,Api api,
                                Object listLockB,Object listLockS ){
        this.tradingList_Buy=tradingList_Buy;
        this.tradingList_Sell=tradingList_Sell;
        this.api=api;
        this.listLockB=listLockB;
        this.listLockS=listLockS;
    }

    @Override
    public void run() {

        while (true){
            try{
                int buyDoneCount =0;
                int sellDoneCount =0;
                for (Element element : tradingList_Buy) {
                    if (element.tradeStatus == TradeStatus.done) {
                        buyDoneCount++;
                    }
                    // fixme 是否要降低锁粒度
                    synchronized (listLockB) {
                        if (element.tradeStatus == TradeStatus.waitCancel) {
                            // 超时 todo 判断价格趋势
                            if(element.getOrder().lastUpdateTime + Config.maxCancelWaitTimeMillSec < System.currentTimeMillis()){
                                // 取消订单
                                log.warn("cancel buy order *********");
                                if(api.cancel(element.getOrder())!=null){
                                    element.tradeStatus = TradeStatus.canceling;
                                }
                            }
                        }
                    }
                }
                for (Element element : tradingList_Sell) {
                    if (element.tradeStatus == TradeStatus.done) {
                        sellDoneCount++;
                    }
                    synchronized (listLockS) {
                        if (element.tradeStatus == TradeStatus.waitCancel) {
                            // 超时
                            if(element.getOrder().lastUpdateTime + Config.maxCancelWaitTimeMillSec < System.currentTimeMillis()) {
                                // 取消订单
                                log.warn("cancel sell order *********");
                                if(api.cancel(element.getOrder())!=null){
                                    element.tradeStatus = TradeStatus.canceling;
                                }
                            }
                        }
                    }
                }
                if(buyDoneCount==tradingList_Buy.size() && sellDoneCount==tradingList_Sell.size()){
                    log.warn("CancelTask isfinish >>>>>>>>>>>>>>>>>>>>>>>>>");
                    isFinish = true;
                    break;
                }
                Util.threadSleep(100);

            }catch (Exception e){
                log.error("CancelTask exception,{}",e);
                Util.threadSleep(10000);
            }
        }
    }

    @Override
    public boolean isFinish() {
        return isFinish;
    }

    @Override
    public String getTaskName() {
        return "cancelTask";
    }
}
