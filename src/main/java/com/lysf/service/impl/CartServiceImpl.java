package com.lysf.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.lysf.Vo.CartProductVo;
import com.lysf.Vo.CartVo;
import com.lysf.common.Const;
import com.lysf.dao.CartMapper;
import com.lysf.dao.ProductMapper;
import com.lysf.dto.Result;
import com.lysf.entity.Cart;
import com.lysf.entity.Product;
import com.lysf.enums.ResponseCode;
import com.lysf.service.CartService;
import com.lysf.util.BigDecimalUtil;
import com.lysf.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements CartService{

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public Result<CartVo> add(Integer userId, Integer productId, Integer count){
        if (productId == null || count == null){
            return Result.createErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        //如果这个物品没有在这个订单中，则需要添加一个产品的记录
        if (cart == null){
            Cart cartItem = new Cart();
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
        }else {
            //如果已经存在则数量相加
            cart.setQuantity(cart.getQuantity()+count);
            //更新产品选中数量
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return list(userId);
    }

    //更新产品数量值，直接将传过来的数据进行更新
    @Override
    public Result<CartVo> update(Integer userId, Integer productId, Integer count){
        if (productId == null || count == null){
            return Result.createErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if (cart != null){
            //如果没有该订单，添加该产品
            cart.setQuantity(count);
        }
        //向购物车表中更新产品数量信息
        cartMapper.updateByPrimaryKeySelective(cart);
        return list(userId);
    }

    //记住在数据库条件语句的地方int型的字段可以使用字符串进行赋值
    @Override
    public Result<CartVo> delete(Integer userId, String productIds){
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)){
            return Result.createErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteCartProductsByUserIdProductId(userId,productIdList);
        //重新获取购物车产品信息
        return list(userId);
    }

    //查询用户的所有购物车内的产品清单
    @Override
    public Result<CartVo> list(Integer userId){
        CartVo cartVo = getCartVoLimit(userId);
        return Result.createSuccess(cartVo);
    }


    //产品勾选
    @Override
    public Result<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer cheched){
        cartMapper.checkedOrUnCheckedProduct(userId,productId,cheched);
        System.out.println(productId);
        return list(userId);
    }

    @Override
    public Result getAllProductNumOfCart(Integer userId) {
        if (userId == null){
            return Result.createSuccess(0);
        }
        int i = cartMapper.selectCartProductCount(userId);
        return Result.createSuccess(i);
    }

    //向购物车VO封装对象中添加用户所选的所有产品信息，并且要审查能够购买的产品数量
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        //购物车商品总价
        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (!CollectionUtils.isEmpty(cartList)){
            for (Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null){
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductSubitile(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());//产品库存量
                    cartProductVo.setProductPrice(product.getPrice());

                    //判断库存
                    int  buyLimitCount = 0;
                    if (product.getStock()>= cartItem.getQuantity()){
                        buyLimitCount = cartItem.getQuantity();
                        //设置标志
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FALL);
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.insertSelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(buyLimitCount,product.getPrice().doubleValue()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if (cartItem.getChecked() == Const.Cart.CHECKED){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setAllChecked(getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("lysf.server.http.prefix","http://localhost:8080/comlysf/"));
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if (userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0 ? true : false;
    }
}
