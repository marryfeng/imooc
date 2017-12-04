package com.imooc.project.service;

import com.imooc.project.common.ServerResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/11/18.
 */
public interface CategoryService {

    ServerResponse getCategory(Integer categoryId);
    ServerResponse<String> addCategory(Integer parentId,String categoryName);
    ServerResponse<String> updateCategoryName(Integer categoryId,String categoryName);
    ServerResponse<List<Integer>> getDeepCategory(Integer categoryId);

}
