package com.lysf.service;

import com.lysf.dto.Result;

public interface SaleInfoService {

    Result selectOneSaleInfo(String productName,int pageNum,int pageSize);


    Result selectAllSaleInfo(int pageNum,int pageSize);

}
