package com.lysf.service;

import com.lysf.dto.Result;

import java.util.List;

public interface CategoryService {

    Result addCategory(String categoryName, Integer parentId);

    Result updateCategoryName(Integer categoryId, String categoryName);

    Result getChildParallelCategory(Integer categoryId);

    Result<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
