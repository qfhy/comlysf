package com.lysf.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lysf.dao.CategoryMapper;
import com.lysf.dto.Result;
import com.lysf.entity.Category;
import com.lysf.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;


@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result addCategory(String categoryName, Integer parentId){
        if (parentId == null || StringUtils.isBlank(categoryName)){
            return Result.createErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int i = categoryMapper.insertSelective(category);
        if (i>0){
            return Result.createSuccessMessage("添加商品种类成功");
        }
        return Result.createErrorMessage("添加商品种类失败");
    }

     public Result updateCategoryName(Integer categoryId, String categoryName){
        if (categoryId == null || StringUtils.isBlank(categoryName)){
            return Result.createErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int i = categoryMapper.updateByPrimaryKeySelective(category);
        if(i>0){
            return Result.createSuccessMessage("更新商品种类名称成功");
        }
         return Result.createErrorMessage("更新商品种类名称失败");
     }


    public Result getChildParallelCategory(Integer categoryId){
         List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
         if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
         }
         return Result.createSuccess(categoryList);
    }

    /**
     * 查询本节点的id以及子节点的id
     * @param categoryId
     * @return
     */
    public Result<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> categoryList = Lists.newArrayList();
        if (categoryId != null){
            for (Category categoryItem :categorySet){
                categoryList.add(categoryItem.getId());
            }
        }
        return Result.createSuccess(categoryList);

    }

    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }















}
