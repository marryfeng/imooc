package com.imooc.project.mapper;

import com.imooc.project.entity.mmall_product;
import com.imooc.project.entity.mmall_productWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface mmall_productMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_productWithBLOBs record);

    int insertSelective(mmall_productWithBLOBs record);

    mmall_productWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_productWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(mmall_productWithBLOBs record);

    int updateByPrimaryKey(mmall_product record);
}