package com.imooc.project.mapper;

import com.imooc.project.entity.mmall_order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface mmall_orderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_order record);

    int insertSelective(mmall_order record);

    mmall_order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_order record);

    int updateByPrimaryKey(mmall_order record);
}