package com.lysf.service;

import com.github.pagehelper.PageInfo;
import com.lysf.Vo.OrderVo;
import com.lysf.dto.Result;

import java.util.Map;

public interface OrderService {

    Result pay(Long orderNo, Integer userId, String path);
    Result aliCallback(Map<String,String> params);
    Result queryOrderPayStatus(Integer userId,Long orderNo);
    Result createOrder(Integer userId,Integer shippingId);
    Result<String> cancel(Integer userId,Long orderNo);
    Result getOrderCartProduct(Integer userId);
    Result<OrderVo> getOrderDetail(Integer userId, Long orderNo);
    Result<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);



    //backend
    Result<PageInfo> manageList(int pageNum,int pageSize);
    Result<OrderVo> manageDetail(Long orderNo);
    Result<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);
    Result<String> manageSendGoods(Long orderNo);

}
