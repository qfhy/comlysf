package com.lysf.dao;

import com.lysf.entity.Purchase;

public interface PurchaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Purchase record);

    int insertSelective(Purchase record);

    Purchase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Purchase record);

    int updateByPrimaryKey(Purchase record);
}