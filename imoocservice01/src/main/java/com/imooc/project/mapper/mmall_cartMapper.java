package com.imooc.project.mapper;

import com.imooc.project.entity.mmall_cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface mmall_cartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_cart record);

    int insertSelective(mmall_cart record);

    mmall_cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_cart record);

    int updateByPrimaryKey(mmall_cart record);
}