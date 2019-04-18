package com.lysf.controller;

import com.lysf.dto.Result;
import com.lysf.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ResponseBody
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public Result detail(Integer productId){
        return productService.getProductDetail(productId);
    }

    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result list(@RequestParam(value = "keyword",required = false)String keyword,
                       @RequestParam(value = "categoryId",required = false)Integer categoryId,
                       @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                       @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                       @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return productService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }

}
