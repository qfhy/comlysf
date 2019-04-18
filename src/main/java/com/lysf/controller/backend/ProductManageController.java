package com.lysf.controller.backend;

import com.google.common.collect.Maps;
import com.lysf.common.Const;
import com.lysf.dto.Result;
import com.lysf.entity.Product;
import com.lysf.entity.User;
import com.lysf.enums.ResponseCode;
import com.lysf.service.FileService;
import com.lysf.service.ProductService;
import com.lysf.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @ResponseBody
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public Result productSave(HttpSession session,@RequestBody Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //保存商品
            return productService.saveOrUpdateProduct(product);
        }else {
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/set_sale_status",method = RequestMethod.POST)
    public Result setSaleStatus(HttpSession session, @RequestParam("productId") Integer productId,@RequestParam("status") Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //设置商品销售状态
            return productService.setSaleStatus(productId, status);
        }else {
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

    //获取商品详情
    @ResponseBody
    @RequestMapping(value = "/getDetail",method = RequestMethod.POST)
    public Result getDetail(HttpSession session, @RequestParam("productId") Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //设置商品详情
            return productService.manageProductDetai(productId);
        }else {
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

    //获取商品详情
    @ResponseBody
    @RequestMapping(value = "/List",method = RequestMethod.POST)
    public Result getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //设置商品详情
            return productService.getProductList(pageNum,pageSize);
        }else {
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

    //后台的产品搜索
    @ResponseBody
    @RequestMapping(value = "/productSearch",method = RequestMethod.POST)
    public Result productSearch(HttpSession session,@RequestParam(value = "productName")String productName,@RequestParam(value = "productId")Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            //todo 后台的产品搜索
            return productService.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile multipartFile, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return Result.createErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (userService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(multipartFile,path);
            Map<String,String> map = Maps.newHashMap();
            map.put("url",targetFileName);
            return Result.createSuccess(map);
        }else {
            return Result.createErrorMessage("没有权限进行操作");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/richTextImgUpload",method = RequestMethod.POST)
    public Map richTextImgUpload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile multipartFile, HttpServletRequest request,HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        if (userService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(multipartFile,path);
            if (StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",targetFileName);
            //我也不知道为什么需要这个，需要学习
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }
}
