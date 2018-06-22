package com.auto.trade.entity;

import java.util.Date;

/**
 * Created by caigaonian870 on 18/1/20.
 */
 public interface Kline {
    Integer getId();
    String getIntervalId();

    String getSymbol();

    long getEventTime();

    Long getOpenTime();

     String getOpen();

     String getHigh() ;


     String getLow();


     String getClose();


     String getVolume();


     Long getCloseTime();

    String getXeventTime();


    String getTransInfo();

    Date getDateCreated();

    Date getDateUpdated();
}
