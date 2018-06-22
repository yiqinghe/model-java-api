package com.auto.trade.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单实时价格
 */
@Entity
@Table(name = "order_price",indexes={@Index(name="o_created_date_index",columnList="dateCreated")})
public class OrderPrice {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  public Integer id;

  public String exchange;

  // 事件时间
  public long eventTime;

  // 成交对
  public String symbol;

  public String price;

  public String qty;
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

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getQty() {
    return qty;
  }

  public void setQty(String qty) {
    this.qty = qty;
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
    return "OrderPrice{" +
            "id=" + id +
            ", exchange='" + exchange + '\'' +
            ", eventTime=" + eventTime +
            ", symbol='" + symbol + '\'' +
            ", price='" + price + '\'' +
            ", qty='" + qty + '\'' +
            ", dateCreated=" + dateCreated +
            ", dateUpdated=" + dateUpdated +
            '}';
  }
}