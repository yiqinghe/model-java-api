package com.auto.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "depth_data",indexes={@Index(name="d_created_date_index",columnList="dateCreated")})
public class DepthData{

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  public Integer id;

  public String exchange;

  // 事件时间
  public long eventTime;

  // 成交对
  public String symbol;

  public long updateId;

  public String bid_1_price;

  public String bid_1_qty;

  public String bid_2_price;

  public String bid_2_qty;

  public String bid_3_price;

  public String bid_3_qty;

  public String bid_4_price;

  public String bid_4_qty;

  public String bid_5_price;

  public String bid_5_qty;

  public String ask_1_price;

  public String ask_1_qty;

  public String ask_2_price;

  public String ask_2_qty;

  public String ask_3_price;

  public String ask_3_qty;

  public String ask_4_price;

  public String ask_4_qty;

  public String ask_5_price;

  public String ask_5_qty;
  // 创建时间
  @org.hibernate.annotations.CreationTimestamp
  @Column(updatable = false)
  public Date dateCreated;

  // 更新时间
  @org.hibernate.annotations.UpdateTimestamp
  public Date dateUpdated;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public Long getUpdateId() {
    return updateId;
  }

  public void setUpdateId(long updateId) {
    this.updateId = updateId;
  }

  public String getBid_1_price() {
    return bid_1_price;
  }

  public void setBid_1_price(String bid_1_price) {
    this.bid_1_price = bid_1_price;
  }

  public String getBid_1_qty() {
    return bid_1_qty;
  }

  public void setBid_1_qty(String bid_1_qty) {
    this.bid_1_qty = bid_1_qty;
  }

  public String getBid_2_price() {
    return bid_2_price;
  }

  public void setBid_2_price(String bid_2_price) {
    this.bid_2_price = bid_2_price;
  }

  public String getBid_2_qty() {
    return bid_2_qty;
  }

  public void setBid_2_qty(String bid_2_qty) {
    this.bid_2_qty = bid_2_qty;
  }

  public String getBid_3_price() {
    return bid_3_price;
  }

  public void setBid_3_price(String bid_3_price) {
    this.bid_3_price = bid_3_price;
  }

  public String getBid_3_qty() {
    return bid_3_qty;
  }

  public void setBid_3_qty(String bid_3_qty) {
    this.bid_3_qty = bid_3_qty;
  }

  public String getBid_4_price() {
    return bid_4_price;
  }

  public void setBid_4_price(String bid_4_price) {
    this.bid_4_price = bid_4_price;
  }

  public String getBid_4_qty() {
    return bid_4_qty;
  }

  public void setBid_4_qty(String bid_4_qty) {
    this.bid_4_qty = bid_4_qty;
  }

  public String getBid_5_price() {
    return bid_5_price;
  }

  public void setBid_5_price(String bid_5_price) {
    this.bid_5_price = bid_5_price;
  }

  public String getBid_5_qty() {
    return bid_5_qty;
  }

  public void setBid_5_qty(String bid_5_qty) {
    this.bid_5_qty = bid_5_qty;
  }

  public String getAsk_1_price() {
    return ask_1_price;
  }

  public void setAsk_1_price(String ask_1_price) {
    this.ask_1_price = ask_1_price;
  }

  public String getAsk_1_qty() {
    return ask_1_qty;
  }

  public void setAsk_1_qty(String ask_1_qty) {
    this.ask_1_qty = ask_1_qty;
  }

  public String getAsk_2_price() {
    return ask_2_price;
  }

  public void setAsk_2_price(String ask_2_price) {
    this.ask_2_price = ask_2_price;
  }

  public String getAsk_2_qty() {
    return ask_2_qty;
  }

  public void setAsk_2_qty(String ask_2_qty) {
    this.ask_2_qty = ask_2_qty;
  }

  public String getAsk_3_price() {
    return ask_3_price;
  }

  public void setAsk_3_price(String ask_3_price) {
    this.ask_3_price = ask_3_price;
  }

  public String getAsk_3_qty() {
    return ask_3_qty;
  }

  public void setAsk_3_qty(String ask_3_qty) {
    this.ask_3_qty = ask_3_qty;
  }

  public String getAsk_4_price() {
    return ask_4_price;
  }

  public void setAsk_4_price(String ask_4_price) {
    this.ask_4_price = ask_4_price;
  }

  public String getAsk_4_qty() {
    return ask_4_qty;
  }

  public void setAsk_4_qty(String ask_4_qty) {
    this.ask_4_qty = ask_4_qty;
  }

  public String getAsk_5_price() {
    return ask_5_price;
  }

  public void setAsk_5_price(String ask_5_price) {
    this.ask_5_price = ask_5_price;
  }

  public String getAsk_5_qty() {
    return ask_5_qty;
  }

  public void setAsk_5_qty(String ask_5_qty) {
    this.ask_5_qty = ask_5_qty;
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

  public String getExchange() {
    return exchange;
  }

  public void setExchange(String exchange) {
    this.exchange = exchange;
  }

  @Override
  public String toString() {
    return "DepthData{" +
            "id=" + id +
            ", exchange='" + exchange + '\'' +
            ", eventTime=" + eventTime +
            ", symbol='" + symbol + '\'' +
            ", updateId=" + updateId +
            ", bid_1_price='" + bid_1_price + '\'' +
            ", bid_1_qty='" + bid_1_qty + '\'' +
            ", bid_2_price='" + bid_2_price + '\'' +
            ", bid_2_qty='" + bid_2_qty + '\'' +
            ", bid_3_price='" + bid_3_price + '\'' +
            ", bid_3_qty='" + bid_3_qty + '\'' +
            ", bid_4_price='" + bid_4_price + '\'' +
            ", bid_4_qty='" + bid_4_qty + '\'' +
            ", bid_5_price='" + bid_5_price + '\'' +
            ", bid_5_qty='" + bid_5_qty + '\'' +
            ", ask_1_price='" + ask_1_price + '\'' +
            ", ask_1_qty='" + ask_1_qty + '\'' +
            ", ask_2_price='" + ask_2_price + '\'' +
            ", ask_2_qty='" + ask_2_qty + '\'' +
            ", ask_3_price='" + ask_3_price + '\'' +
            ", ask_3_qty='" + ask_3_qty + '\'' +
            ", ask_4_price='" + ask_4_price + '\'' +
            ", ask_4_qty='" + ask_4_qty + '\'' +
            ", ask_5_price='" + ask_5_price + '\'' +
            ", ask_5_qty='" + ask_5_qty + '\'' +
            ", dateCreated=" + dateCreated +
            ", dateUpdated=" + dateUpdated +
            '}';
  }
}