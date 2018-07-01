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


    protected Api api;

    protected TradeSymbol symbolA;
    protected TradeSymbol symbolB;
    protected TradeSymbol symbolC;

    // 	量化周期交易对A、B、C
    protected List<Element> tradingList_A = new ArrayList<>();
    protected List<Element> tradingList_B = new ArrayList<>();
    protected List<Element> tradingList_C = new ArrayList<>();

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
                }else{
                    log.warn("init fail >>>");
                    Thread.sleep(60000);
                }

            }catch (Exception e){
                log.warn("init fail {}",e);
                Thread.sleep(60000);
            }

        }

        List<AbstractTask> taskList = new ArrayList<>();
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


        QuantitativeResult quantitativeResult = new QuantitativeResult();

        return quantitativeResult;
    }

    abstract public void buildTask(List<AbstractTask> taskList);
}