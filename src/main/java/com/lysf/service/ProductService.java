package com.lysf.service;

import com.lysf.dto.Result;
import com.lysf.entity.Product;

public interface ProductService {

    Result saveOrUpdateProduct(Product product);

    Result setSaleStatus(Integer productId, Integer status);

    Result manageProductDetai(Integer productId);

    Result getProductList(int pageNum,int pageSize);

    Result searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    Result getProductDetail(Integer productId);

    Result getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
