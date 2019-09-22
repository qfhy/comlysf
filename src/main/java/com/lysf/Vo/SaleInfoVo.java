package com.lysf.Vo;

import java.util.Date;

public class SaleInfoVo {

    //总数量
    private Integer sumPrice;

    private String productName;

    private Date saleDate;


    public Integer getSumPrice() {
        return sumPrice;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public void setSumPrice(Integer sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
