package com.lysf.controller.portal;

import com.lysf.common.Const;
import com.lysf.dto.Result;
import com.lysf.entity.Shipping;
import com.lysf.entity.User;
import com.lysf.enums.ResponseCode;
import com.lysf.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


/**
 * 重点考虑横向越权问题
 */
@Controller
@RequestMapping("/shipping")
public class ShippingConterller {


    @Autowired
    private ShippingService shippingService;

    //添加收货地址，将信息封装成对象作为参数传过来
    @ResponseBody
    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    public Result add(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.add(user.getId(), shipping);
    }

    //删除收货地址
    @ResponseBody
    @RequestMapping(value = "/delete" ,method = RequestMethod.GET)
    public Result delete(HttpSession session, @RequestParam("shippingId") Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.delete(user.getId(), shippingId);
    }

    //更改收货地址
    @ResponseBody
    @RequestMapping(value = "/update" ,method = RequestMethod.GET)
    public Result update(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.update(user.getId(), shipping);
    }

    //查询具体的某一个订单的收货地址
    @ResponseBody
    @RequestMapping(value = "/select" ,method = RequestMethod.GET)
    public Result select(HttpSession session, @RequestParam("shippingId") Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.select(user.getId(), shippingId);
    }

    @ResponseBody
    @RequestMapping(value = "/list" ,method = RequestMethod.GET)
    public Result list(HttpSession session,@RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "10", required = false)int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.list(user.getId(), pageNum,pageSize);
    }
}