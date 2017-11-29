package com.imooc.project.service;

import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_product;

/**
 * Created by Administrator on 2017/11/21.
 */
public  interface  ProductManageService {
    ServerResponse<String> updateOrAdd(mmall_product product);
    ServerResponse<String> setProductStatus(Integer productId, Integer status);

    ServerResponse getProductList(Integer pageNum, Integer pageSize);

    ServerResponse searchProduct(Integer productId,String productName,Integer pageNum, Integer pageSize);


    ServerResponse getProductDetail(Integer productId);
}
