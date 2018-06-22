package com.auto.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gof on 18/6/17.
 * 周期量化结果
 */
public class QuantitativeResult {

    public Balance balanceAtStart_Target;
    //量化周期开始U余额快照
    public Balance balanceAtStart_Base;
    //量化周期结束A余额快照
    public Balance balanceAtEnd_Target;
    //量化周期结束U余额快照
    public Balance balanceAtEnd_Base;

    // 	量化周期待买数组
    public List<Element> tradingList_Buy = new ArrayList<>();
    // 	量化周期待卖数组
    public List<Element> tradingList_Sell = new ArrayList<>();


    public QuantitativeResult(Balance balanceAtStart_Target, Balance balanceAtStart_Base, Balance balanceAtEnd_Target, Balance balanceAtEnd_Base, List<Element> tradingList_Buy, List<Element> tradingList_Sell) {
        this.balanceAtStart_Target = balanceAtStart_Target;
        this.balanceAtStart_Base = balanceAtStart_Base;
        this.balanceAtEnd_Target = balanceAtEnd_Target;
        this.balanceAtEnd_Base = balanceAtEnd_Base;
        this.tradingList_Buy = tradingList_Buy;
        this.tradingList_Sell = tradingList_Sell;
    }

    @Override
    public String toString() {
        return "QuantitativeResult{" +
                "balanceAtStart_Target=" + balanceAtStart_Target +
                ", balanceAtStart_Base=" + balanceAtStart_Base +
                ", balanceAtEnd_Target=" + balanceAtEnd_Target +
                ", balanceAtEnd_Base=" + balanceAtEnd_Base +
                ", tradingList_Buy=" + tradingList_Buy +
                ", tradingList_Sell=" + tradingList_Sell +
                '}';
    }
}
