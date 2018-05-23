package com.tradergateway.model;

import javax.persistence.*;

/**
 * Created by homepppp on 2018/5/22.
 */
@Entity
@Table(name="blotter",catalog = "")
public class Blotter {
    private int tradeId;
    private String broker;
    private String product;
    private String period;
    private double price;
    private int quantity;
    private String initiatorTrader;
    private String initiatorCompany;
    private int initiatorSide;
    private String completionCompany;
    private int completionSide;
    private String dealTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tradeId",nullable = false)
    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    @Basic
    @Column(name = "broker",nullable = false)
    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    @Basic
    @Column(name = "product",nullable = false)
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Basic
    @Column(name = "period",nullable = false)
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Basic
    @Column(name = "price",nullable = false)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "quantity",nullable = false,length = 31)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "initiatorTrader",nullable = true)
    public String getInitiatorTrader() {
        return initiatorTrader;
    }

    public void setInitiatorTrader(String initiatorTrader) {
        this.initiatorTrader = initiatorTrader;
    }

    @Basic
    @Column(name = "initiatorCompany",nullable = true)
    public String getInitiatorCompany() {
        return initiatorCompany;
    }

    public void setInitiatorCompany(String initiatorCompany) {
        this.initiatorCompany = initiatorCompany;
    }

    @Basic
    @Column(name = "initiatorSide",nullable = true)
    public int getInitiatorSide() {
        return initiatorSide;
    }

    public void setInitiatorSide(int initiatorSide) {
        this.initiatorSide = initiatorSide;
    }

    @Basic
    @Column(name = "completionCompany",nullable = true)
    public String getCompletionCompany() {
        return completionCompany;
    }

    public void setCompletionCompany(String completionCompany) {
        this.completionCompany = completionCompany;
    }

    @Basic
    @Column(name = "completionSide")
    public int getCompletionSide() {
        return completionSide;
    }

    public void setCompletionSide(int completionSide) {
        this.completionSide = completionSide;
    }

    @Basic
    @Column(name = "dealTime")
    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }
}
