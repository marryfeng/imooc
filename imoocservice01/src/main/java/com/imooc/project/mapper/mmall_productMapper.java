package com.imooc.project.mapper;

import com.imooc.project.entity.mmall_product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface mmall_productMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_product record);

    int insertSelective(mmall_product record);

    mmall_product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_product record);

    int updateByPrimaryKeyWithBLOBs(mmall_product record);

    int updateByPrimaryKey(mmall_product record);
}