package com.brokergateway.model;

import javax.persistence.*;

/**
 * Created by homepppp on 2018/5/23.
 */
@Entity
@Table(name = "orders", catalog = "")
public class Order {
    private int orderId;
    private String orderType;
    private String product;
    private String period;
    private String broker;
    private int quantity;
    private double price;
    private int side;
    private String orderTime;
    private String trader;
    private String tradeCompany;
    private int status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId", nullable = false)
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "orderType", nullable = true)
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Basic
    @Column(name = "product", nullable = true)
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Basic
    @Column(name = "period", nullable = true)
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Basic
    @Column(name = "quantity", nullable = true, length = 31)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "price", nullable = true)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "side", nullable = true, length = 31)
    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    @Basic
    @Column(name = "orderTime", nullable = true)
    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @Basic
    @Column(name = "trader", nullable = true)
    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    @Basic
    @Column(name = "traderCompany", nullable = true)
    public String getTradeCompany() {
        return tradeCompany;
    }

    public void setTradeCompany(String tradeCompany) {
        this.tradeCompany = tradeCompany;
    }

    @Basic
    @Column(name = "broker", nullable = true)
    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
