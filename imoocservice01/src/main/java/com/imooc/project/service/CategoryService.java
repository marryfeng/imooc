package com.imooc.project.service;

import com.imooc.project.common.ServerResponse;

/**
 * Created by Administrator on 2017/11/18.
 */
public interface CategoryService {

    ServerResponse getCategory(int categoryId);
    ServerResponse<String> addCategory(int parentId,String categoryName);
    ServerResponse<String> updateCategoryName(int categoryId,String categoryName);
    ServerResponse getDeepCategory(int categoryId);

}
