package com.lysf.controller.backend;

import com.lysf.common.Const;
import com.lysf.dto.Result;
import com.lysf.entity.User;
import com.lysf.enums.ResponseCode;
import com.lysf.service.CategoryService;
import com.lysf.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;

    @ResponseBody
    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    public Result addCategory(HttpSession session,@RequestParam("categoryName") String categoryName,@RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //添加商品种类
            return categoryService.addCategory(categoryName, parentId);
        }else{
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateCategoryName", method = RequestMethod.POST)
    public Result updateCategoryName(HttpSession session,@RequestParam("categoryId") Integer categoryId,@RequestParam("categoryName") String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //更新商品种类名称
            return categoryService.updateCategoryName(categoryId, categoryName);
        }else{
            return Result.createErrorMessage("没有权限进行操作");
        }
    }


    //有一点，就是这个时候的categoryId,我们的是不需要在service中进行检验是否为空的
    @ResponseBody
    @ApiOperation(value = "查询子节点的category信息,并且不递归,保持平级", notes = "查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/get_category", method = RequestMethod.POST)
    public Result getChildParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //查询子节点的category信息，并且不递归，保持平级
            return categoryService.getChildParallelCategory(categoryId);
        }else{
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/get_deep_category", method = RequestMethod.POST)
    public Result getCategoryAndChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //查询子节点的category信息，并且不递归，保持平级
            return categoryService.selectCategoryAndChildrenById(categoryId);
        }else{
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

}















