package com.lysf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.lysf.dao.ShippingMapper;
import com.lysf.dto.Result;
import com.lysf.entity.Shipping;
import com.lysf.service.ShippingService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("shippingService")
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public Result add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        //在insert标签上添加两个配置useGeneratedKeys="true" keyProperty="id"，插入之后立刻拿到插入的id，
        // 系统会自动将插入的id放到shippingId中
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount>0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return Result.createSuccess(result);
        }
        return Result.createErrorMessage("增加收货地址失败");
    }

    @Override
    public Result delete(Integer userId, Integer shippingId){
        int rowCount = shippingMapper.deleteByUserIdAndShippingId(userId,shippingId);
        if (rowCount>0){
            return Result.createSuccessMessage("删除地址成功");
        }
        return Result.createErrorMessage("删除地址失败");
    }

    @Override
    public Result update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        //记住在写sql语句的时候不能更新userId，要把那个更新语句删除掉
        int rowCount = shippingMapper.updateShipping(shipping);
        if (rowCount>0){
            return Result.createSuccessMessage("更新收货地址成功");
        }
        return Result.createErrorMessage("更新收货地址失败");
    }

    @Override
    public Result select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId,shippingId);
        if (shipping==null){
            return Result.createErrorMessage("无法查询到地址");
        }
        return Result.createSuccess("查询地址成功",shipping);
    }

    //分页
    @Override
    public Result list(Integer userId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return Result.createSuccess(pageInfo);
    }



}
