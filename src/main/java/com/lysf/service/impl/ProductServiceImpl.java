package com.lysf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lysf.Vo.ProductDetailVo;
import com.lysf.Vo.ProductListVo;
import com.lysf.common.Const;
import com.lysf.dao.CategoryMapper;
import com.lysf.dao.ProductMapper;
import com.lysf.dto.Result;
import com.lysf.entity.Category;
import com.lysf.entity.Product;
import com.lysf.enums.ResponseCode;
import com.lysf.service.CategoryService;
import com.lysf.service.ProductService;
import com.lysf.util.DateTimeUtil;
import com.lysf.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public Result saveOrUpdateProduct(Product product) {
        if (product == null){
            if (StringUtils.isNotBlank(product.getMainImage())){
                String [] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null){
                int i = productMapper.updateByPrimaryKeySelective(product);
                if (i>0){
                    return Result.createSuccessMessage("更新产品成功");
                }
                return Result.createErrorMessage("更新产品失败");
            }else{
                int i = productMapper.insert(product);
                if (i>0){
                    return Result.createSuccessMessage("添加产品成功");
                }
                return Result.createErrorMessage("添加产品失败");
            }
        }else {
            return Result.createErrorMessage("新增或更新产品参数不正确");
        }
    }

    @Override
    public Result setSaleStatus(Integer productId, Integer status){
        if (productId == null || status == null){
            return Result.createErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int i = productMapper.updateByPrimaryKeySelective(product);
        if (i>0){
            return Result.createSuccessMessage("修改产品状态成功");
        }
        return Result.createErrorMessage("修改产品状态失败");
    }


    @Override
    public Result manageProductDetai(Integer productId){
        if (productId == null){
            return Result.createErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return Result.createErrorMessage("产品不存在或者已经被删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return Result.createSuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //设置imageHost
        //todo imageHost的路径没有正确设值
        productDetailVo.setImageHost(PropertiesUtil.getProperty("lysf.server.http.prefix","http://localhost:8080/comlysf/upload/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null){
            productDetailVo.setParentCategoryId(0);
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    @Override
    public Result getProductList(int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = setProductListVo(product);
            productListVoList.add(productListVo);
        }
        // todo 去查看一下这个分页插件到底都是怎么个意思啊
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return Result.createSuccess(pageInfo);
    }

    private ProductListVo setProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubitle(product.getSubtitle());
        //todo 设置主图服务器根路径还没弄呢，所以暂时都使用空值
        productListVo.setImageHost(PropertiesUtil.getProperty("lysf.server.http.prefix","http://localhost:8080/comlysf/upload/"));
        return productListVo;
    }

    @Override
    public Result searchProduct(String productName, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName.trim().toString()).append("%").toString();
        }
        System.out.println(productName);
        List<Product> productList = productMapper.selectByNameAndProductId(productName);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            System.out.println(product.getName());
            ProductListVo productListVo = setProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo result = new PageInfo(productList);
        result.setList(productListVoList);
        return Result.createSuccess(result);
    }

    @Override
    public Result getProductDetail(Integer productId){
        if (productId == null){
            return Result.createErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return Result.createErrorMessage("产品已下架或已被删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return Result.createSuccess(productDetailVo);
    }

    @Override
    public Result getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy){
        if (StringUtils.isBlank(keyword) && Objects.equals(categoryId, categoryId)){
            return Result.createErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();
        if (categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)){
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo result = new PageInfo(productListVoList);
                return Result.createSuccess(result);
            }
            categoryIdList = categoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        //排序处理(动态排序) mybatis
        if(StringUtils.isNotBlank(orderBy)){
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String []orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryId(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = setProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return Result.createSuccess(pageInfo);
    }
}
