package com.lysf.controller.backend;

import com.lysf.common.Const;
import com.lysf.dto.Result;
import com.lysf.entity.User;
import com.lysf.enums.ResponseCode;
import com.lysf.service.SaleInfoService;
import com.lysf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/sale")
public class SaleInfoManageController {

    @Autowired
    private UserService userService;
    @Autowired
    private SaleInfoService saleInfoService;

    @ResponseBody
    @RequestMapping(value = "/selectOneSaleInfo",method = RequestMethod.GET)
    public Result selectOneSaleInfo(HttpSession session, @RequestParam(value = "productName")String productName,
                                    @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //todo 后台的产品搜索
            return saleInfoService.selectOneSaleInfo(productName,pageNum,pageSize);
        }else {
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

    //做成查询每天的所有的商品销售总额
    @ResponseBody
    @RequestMapping(value = "/selectAllSaleInfo",method = RequestMethod.GET)
    public Result selectAllSaleInfo(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            return saleInfoService.selectAllSaleInfo(pageNum,pageSize);
        }else {
            return Result.createErrorMessage("没有权限进行操作");
        }
    }
}
