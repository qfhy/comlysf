package com.lysf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lysf.Vo.ProductListVo;
import com.lysf.Vo.SaleInfoVo;
import com.lysf.dao.ProductMapper;
import com.lysf.dao.SaleInfoMapper;
import com.lysf.dto.Result;
import com.lysf.entity.Product;
import com.lysf.entity.SaleInfo;
import com.lysf.service.SaleInfoService;
import com.lysf.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("saleInfoService")
public class SaleInfoServiceImpl implements SaleInfoService {

    @Autowired
    private SaleInfoMapper saleInfoMapper;
    @Override
    public Result selectOneSaleInfo(String productName, int pageNum, int pageSize){
        int i = saleInfoMapper.selectByName(productName);
        if (i==0){
            return Result.createErrorMessage("此商品不存在");
        }
        //获取从当前日期推迟七天的日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.MONTH, -1);
        Date dt1=rightNow.getTime();
        String datebefore = df.format(dt1);
        String dateNow = df.format(new Date());
        PageHelper.startPage(pageNum, pageSize);
        List<SaleInfoVo> productList = saleInfoMapper.selectByNameAndDate(productName,datebefore,dateNow);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productList);
        return Result.createSuccess(pageInfo);
    }

    @Override
    public Result selectAllSaleInfo(int pageNum, int pageSize) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date());
        rightNow.add(Calendar.MONTH, -1);
        Date dt1=rightNow.getTime();
        String datebefore = df.format(dt1);
        String dateNow = df.format(new Date());
        PageHelper.startPage(pageNum, pageSize);
        List<SaleInfoVo> productList = saleInfoMapper.selectAllByDate(datebefore,dateNow);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productList);
        return Result.createSuccess(pageInfo);
    }
}
