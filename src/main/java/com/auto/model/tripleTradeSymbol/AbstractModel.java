package com.auto.model.tripleTradeSymbol;

import com.auto.model.common.AbstractTask;
import com.auto.model.common.Api;
import com.auto.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by gof on 18/6/29.
 */
public abstract class AbstractModel {

    private static final Logger log = LoggerFactory.getLogger(AbstractModel.class);


    public Api api;

    public TradeSymbol symbolA;
    public TradeSymbol symbolB;
    public TradeSymbol symbolC;

    //量化周期开始B1、B2、B3余额快照
    public Balance balanceAtStart_B1;
    public Balance balanceAtStart_B2;
    public Balance balanceAtStart_B3;
    //量化周期结束B1、B2、B3余额快照
    public Balance balanceAtEnd_B1;
    public Balance balanceAtEnd_B2;
    public Balance balanceAtEnd_B3;

    // 	量化周期交易对A、B、C
    public List<Element> tradingList_A = new ArrayList<>();
    public List<Element> tradingList_B = new ArrayList<>();
    public List<Element> tradingList_C = new ArrayList<>();

    protected final Object listLockA = new Object();
    protected final Object listLockB = new Object();
    protected final Object listLockC = new Object();


    protected ExecutorService executor = new ThreadPoolExecutor(6, 20,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());


    public AbstractModel(Api api, TradeSymbol symbolA,TradeSymbol symbolB
            ,TradeSymbol symbolC) {
        this.api = api;
        this.symbolA=symbolA;
        this.symbolB=symbolB;
        this.symbolC=symbolC;
    }

    /**
     * 初始化
     */
    public boolean init(){
        tradingList_Buy.clear();;
        tradingList_Sell.clear();

        balanceAtStart_Target = api.getBalance(symbol.targetCurrency);
        balanceAtStart_Base = api.getBalance(symbol.baseCurrency);

        log.info("balanceAtStart_Target:{}",balanceAtStart_Target);
        log.info("balanceAtStart_Base:{}",balanceAtStart_Base);

        if(new BigDecimal(balanceAtStart_Target.amount).compareTo(new BigDecimal(Config.quota )) < 0){
            log.error("init not enough balance");
            return false;
        }

        BigDecimal each = new BigDecimal(Config.quota).setScale(Config.amountScale, RoundingMode.HALF_UP);

        int num = new BigDecimal(balanceAtStart_Target.amount).divide(new BigDecimal(Config.quota),Config.amountScale, RoundingMode.HALF_UP)
                .setScale(Config.amountScale, RoundingMode.HALF_UP).intValue();
        log.info("array num:{} >>>>>>>>>>>>>>>>>>>>>>>>",num);
        for(int i = 0;i< num;i++){
            Element element = new Element(i,symbol,TradeType.buy);
            element.targetAmount = each;
            // 手续费抵扣
            element.targetAmount = api.getTargetAmountForBuy(element.targetAmount);
            tradingList_Buy.add(element);

            Element elementS = new Element(i,symbol,TradeType.sell);
            elementS.targetAmount = each;
            elementS.targetAmount = api.getTargetAmountForSell(elementS.targetAmount);
            tradingList_Sell.add(elementS);
        }
        return true;
    }
    /**
     * 周期开始 todo 根据一轮周期结束后的数据判断是否开启下一轮，或者自动调整参数
     */
    public QuantitativeResult periodStart() throws InterruptedException {
        while (true){
            try{
                boolean flag = init();
                if(flag){
                    break;
                }else{zd
                    log.warn("init fail >>>");
                    Thread.sleep(60000);
                }aa4e

            }catch (Exception e){
                log.warn("init fail {}",e);
                Thread.sleep(60000);
            }

        }

        List<AbstractTask> taskList = new ArrayList<>();
        if(tradingList_Buy.size()>0 && tradingList_Sell.size() >0){
            // 组装具体实现
            buildTask(taskList);

            boolean finish = false;
            // 等待结束
            while(!finish){
                for(AbstractTask task:taskList){
                    finish = true;
                    if(!task.isFinish()){
                        log.warn("this task :{} not finish.",task.getTaskName());
                        finish =false;
                        break;
                    }
                    log.warn("wait period total done");
                }
                Thread.sleep(5000);
            }
        }
        balanceAtEnd_Target = api.getBalance(symbol.targetCurrency);
        balanceAtEnd_Base = api.getBalance(symbol.baseCurrency);


        QuantitativeResult quantitativeResult =
                new QuantitativeResult(balanceAtStart_Target,balanceAtStart_Base,
                balanceAtEnd_Target,balanceAtEnd_Base,
                tradingList_Buy,tradingList_Sell);

        return quantitativeResult;
    }

    abstract public void buildTask(List<AbstractTask> taskList);
}