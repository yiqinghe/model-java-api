package com.auto.trade.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "kline_1minute",indexes={@Index(name="created_date_index",columnList="dateCreated")})
public class Kline1minute implements Kline {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  public Integer id;

  // 间隔Kline1minute
  public String intervalId = "1m";
  // 事件时间
  public long eventTime;

  @Transient // x轴图表显示
  public String xeventTime;

  // 成交对
  public String symbol;

  // 开盘时间
  public Long openTime;

  // 开盘价
  public String open;

  // 最高价
  public String high;

  // 最低价
  public String low;

  // 收盘价
  public String close;

  // 成交量
  public String volume;

  // 交易信息：
  // B:0.5:100.23 含义：以100.23的价格买入0.5个。
  // S:0.5:100.23 含义：以100.23的价格卖出0.5个。
  public String transInfo;

  // 收盘时间
  public Long closeTime;

  @Transient
  public String diff="NA";
  @Transient
  public String dev="NA";
  @Transient
  public String bar="NA";
  @Transient
  public String tradeLabel="0";

  // 创建时间
  @org.hibernate.annotations.CreationTimestamp
  @Column(updatable = false)
  public Date dateCreated;

  // 更新时间
  @org.hibernate.annotations.UpdateTimestamp
  public Date dateUpdated;

  public String getIntervalId() {
    return intervalId;
  }

  public void setIntervalId(String intervalId) {
    this.intervalId = intervalId;
  }

  public long getEventTime() {
    return eventTime;
  }

  public void setEventTime(long eventTime) {
    this.eventTime = eventTime;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Long getOpenTime() {
    return openTime;
  }

  public void setOpenTime(Long openTime) {
    this.openTime = openTime;
  }

  public String getOpen() {
    return open;
  }

  public void setOpen(String open) {
    this.open = open;
  }

  public String getHigh() {
    return high;
  }

  public void setHigh(String high) {
    this.high = high;
  }

  public String getLow() {
    return low;
  }

  public void setLow(String low) {
    this.low = low;
  }

  public String getClose() {
    return close;
  }

  public void setClose(String close) {
    this.close = close;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public Long getCloseTime() {
    return closeTime;
  }

  public void setCloseTime(Long closeTime) {
    this.closeTime = closeTime;
  }

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Date getDateUpdated() {
    return dateUpdated;
  }

  public void setDateUpdated(Date dateUpdated) {
    this.dateUpdated = dateUpdated;
  }

  public String getTradeLabel() {
    return tradeLabel;
  }

  public void setTradeLabel(String tradeLabel) {
    this.tradeLabel = tradeLabel;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
  public String getXeventTime() {
    return xeventTime;
  }

  public void setXeventTime(String xeventTime) {
    this.xeventTime = xeventTime;
  }

  public String getTransInfo() {
    return transInfo;
  }

  public void setTransInfo(String transInfo) {
    this.transInfo = transInfo;
  }

  public String getDiff() {
    return diff;
  }

  public void setDiff(String diff) {
    this.diff = diff;
  }

  public String getDev() {
    return dev;
  }

  public void setDev(String dev) {
    this.dev = dev;
  }

  public String getBar() {
    return bar;
  }

  public void setBar(String bar) {
    this.bar = bar;
  }

  public String buildFlushContentForSvm(){
    String content = "" + tradeLabel +
            " 1:" + volume +
            " 2:" + close +
            " 3:" + diff +
            " 4:" + dev +
            " 5:" + bar ;
    return content;

  }
  @Override
  public String toString() {
    return "Kline1minute{" +
            "id=" + id +
            ", intervalId='" + intervalId + '\'' +
            ", eventTime=" + eventTime +
            ", xeventTime='" + xeventTime + '\'' +
            ", symbol='" + symbol + '\'' +
            ", openTime=" + openTime +
            ", open='" + open + '\'' +
            ", high='" + high + '\'' +
            ", low='" + low + '\'' +
            ", close='" + close + '\'' +
            ", volume='" + volume + '\'' +
            ", transInfo='" + transInfo + '\'' +
            ", closeTime=" + closeTime +
            ", diff='" + diff + '\'' +
            ", dev='" + dev + '\'' +
            ", bar='" + bar + '\'' +
            ", dateCreated=" + dateCreated +
            ", dateUpdated=" + dateUpdated +
            '}';
  }
}