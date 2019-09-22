package com.lysf.service;

import com.lysf.Vo.CartVo;
import com.lysf.dto.Result;

public interface CartService {

    Result<CartVo> add(Integer userId, Integer productId, Integer count);

    Result<CartVo> update(Integer userId, Integer productId, Integer count);

    Result<CartVo> delete(Integer userId, String productIds);

    Result<CartVo> list(Integer userId);

    Result<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer cheched);

    Result getAllProductNumOfCart(Integer userId);


}
