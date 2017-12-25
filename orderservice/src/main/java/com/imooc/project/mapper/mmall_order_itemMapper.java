package com.imooc.project.mapper;


import com.imooc.project.entity.mmall_order_item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface mmall_order_itemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_order_item record);

    int insertSelective(mmall_order_item record);

    mmall_order_item selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_order_item record);

    int updateByPrimaryKey(mmall_order_item record);

    List<mmall_order_item> selectItem(@Param(value = "userId") Integer userId, @Param(value = "orderNo") Long orderNo);


}