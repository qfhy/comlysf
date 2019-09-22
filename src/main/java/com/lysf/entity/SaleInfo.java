package com.lysf.entity;

import java.util.Date;

public class SaleInfo {
    private Integer id;

    private Integer categoryId;

    private String productName;

    private Integer saleNum;

    private Date saleTime;

    public SaleInfo(Integer id, Integer categoryId, String productName, Integer saleNum, Date saleTime) {
        this.id = id;
        this.categoryId = categoryId;
        this.productName = productName;
        this.saleNum = saleNum;
        this.saleTime = saleTime;
    }

    public SaleInfo() {
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Date getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Date saleTime) {
        this.saleTime = saleTime;
    }
}