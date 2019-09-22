package com.lysf.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Purchase {
    private Integer id;

    private Integer categoryId;

    private String name;

    private Integer buyNum;

    private BigDecimal price;

    private BigDecimal sumPrice;

    private Integer status;

    private Date purchaseTime;

    private Date createTime;

    private Date updateTime;

    public Purchase(Integer id, Integer categoryId, String name, Integer buyNum, BigDecimal price, BigDecimal sumPrice, Integer status, Date purchaseTime, Date createTime, Date updateTime) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.buyNum = buyNum;
        this.price = price;
        this.sumPrice = sumPrice;
        this.status = status;
        this.purchaseTime = purchaseTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Purchase() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(BigDecimal sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Date purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}