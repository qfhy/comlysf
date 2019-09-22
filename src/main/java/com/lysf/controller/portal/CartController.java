package com.lysf.controller.portal;

import com.lysf.common.Const;
import com.lysf.dto.Result;
import com.lysf.entity.User;
import com.lysf.enums.ResponseCode;
import com.lysf.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @ResponseBody
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(HttpSession session, @RequestParam("count")Integer count,@RequestParam("productId") Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.add(user.getId(), productId, count) ;
    }

    //更改物品数量
    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Result update(HttpSession session,@RequestParam("count")Integer count,@RequestParam("productId")Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.update(user.getId(), productId, count) ;
    }

    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Result delete(HttpSession session,@RequestParam("productIds")String productIds){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.delete(user.getId(), productIds);
    }
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.list(user.getId());
    }
    //全选接口
    @ResponseBody
    @RequestMapping(value = "/select_all",method = RequestMethod.POST)
    public Result selectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }
    //全反选接口
    @ResponseBody
    @RequestMapping(value = "/un_select_all",method = RequestMethod.POST)
    public Result unSelectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }
    //单个选择
    @ResponseBody
    @RequestMapping(value = "/select",method = RequestMethod.POST)
    public Result select(HttpSession session,@RequestParam("productId")Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
    }
    //单个反选
    @ResponseBody
    @RequestMapping(value = "/unSelect",method = RequestMethod.POST)
    public Result unSelect(HttpSession session,@RequestParam("productId") Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }

    //查询购物车里的产品数量（总数量）
    @ResponseBody
    @RequestMapping(value = "/getAllProductNumOfCart",method = RequestMethod.POST)
    public Result getAllProductNumOfCart(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.getAllProductNumOfCart(user.getId()) ;
    }









}
