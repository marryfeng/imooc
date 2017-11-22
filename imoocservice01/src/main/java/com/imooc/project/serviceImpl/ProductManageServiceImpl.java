package com.imooc.project.serviceImpl;

import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_product;
import com.imooc.project.mapper.mmall_productMapper;
import com.imooc.project.service.ProductManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/11/21.
 */
@Service
public class ProductManageServiceImpl implements ProductManageService {
    @Autowired
    private mmall_productMapper productMapper;
    @Override
    public ServerResponse<String> updateOrAdd(mmall_product product) {
       //根据商品id来判断是用来更新商品还是添加商品，若id为空，则是新增商品。否则是更新商品
       if (product!=null) {
           if (product.getId() != null) {
               //则是更新商品
               int i = productMapper.updateByPrimaryKey(product);
               if (i != 0) {
                   return ServerResponse.createBySuccessMessage("更新商品成功！");
               }
               return ServerResponse.createByErrorMessage("更新商品失败");
           } else {
               //若id为空，则是插入新商品
               int i = productMapper.insert(product);
               if (i != 0) {
                   return ServerResponse.createBySuccessMessage("插入商品成功！");
               }
               return ServerResponse.createByErrorMessage("插入商品失败");
           }
       }
       return ServerResponse.createByErrorMessage("商品不能为空！");
    }

    @Override
    public ServerResponse<String> setProductStatus(Integer productId, Integer status) {
        if (productId!=null){
           mmall_product product=productMapper.selectByPrimaryKey(productId);
           if (product!=null){
               product.setStatus(status);
              int i=productMapper.updateByPrimaryKeySelective(product);
               if (i != 0) {
                   return ServerResponse.createBySuccessMessage("更新商品上下架成功！");
               }
               return ServerResponse.createByErrorMessage("更新商品上下架失败");
            }
            return  ServerResponse.createByErrorMessage("无此商品！");
           }
           return  ServerResponse.createByErrorMessage("商品id不能为空！");
        }

    }

