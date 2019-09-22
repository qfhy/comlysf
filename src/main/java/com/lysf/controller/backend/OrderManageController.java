package com.lysf.controller.backend;

import com.github.pagehelper.PageInfo;
import com.lysf.Vo.OrderVo;
import com.lysf.common.Const;
import com.lysf.dto.Result;
import com.lysf.entity.User;
import com.lysf.enums.ResponseCode;
import com.lysf.service.OrderService;
import com.lysf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private UserService iUserService;
    @Autowired
    private OrderService iOrderService;

    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public Result<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iOrderService.manageList(pageNum,pageSize);
        }else{
            return Result.createErrorMessage("无权限操作");
        }
    }

    @RequestMapping(value = "detail",method = RequestMethod.GET)
    @ResponseBody
    public Result<OrderVo> orderDetail(HttpSession session, Long orderNo){

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑

            return iOrderService.manageDetail(orderNo);
        }else{
            return Result.createErrorMessage("无权限操作");
        }
    }



    @RequestMapping(value = "search",method = RequestMethod.GET)
    @ResponseBody
    public Result<PageInfo> orderSearch(HttpSession session, @RequestParam("orderNo")Long orderNo, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iOrderService.manageSearch(orderNo,pageNum,pageSize);
        }else{
            return Result.createErrorMessage("无权限操作");
        }
    }



    @RequestMapping(value = "send_goods",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> orderSendGoods(HttpSession session, Long orderNo){

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iOrderService.manageSendGoods(orderNo);
        }else{
            return Result.createErrorMessage("无权限操作");
        }
    }


}
