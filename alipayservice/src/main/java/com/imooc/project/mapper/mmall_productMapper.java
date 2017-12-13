package com.imooc.project.mapper;


import com.imooc.project.entity.mmall_product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface mmall_productMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_product record);

    int insertSelective(mmall_product record);

    mmall_product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_product record);

    int updateByPrimaryKey(mmall_product record);

   List<mmall_product> selectProductList();

    List<mmall_product> searchProduct(@Param(value = "productId") Integer productId, @Param(value = "productName") String productName);

    List<mmall_product> selectProtalProduct(@Param(value = "productName") String productName, @Param(value = "categoryList") List<Integer> categoryList);
}