package com.lysf.service;

import com.lysf.dto.Result;
import com.lysf.entity.Shipping;

public interface ShippingService {

    Result add(Integer userId, Shipping shipping);

    Result delete(Integer userId,Integer shippingId);

    Result update(Integer userId,Shipping shipping);

    Result select(Integer userId,Integer shippingId);

    Result list(Integer userId,Integer pageNum,Integer pageSize);

}
