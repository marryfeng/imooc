package com.imooc.project.mapper;


import com.imooc.project.entity.mmall_shipping;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface mmall_shippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_shipping record);

    int insertSelective(mmall_shipping record);

    mmall_shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_shipping record);

    int updateByPrimaryKey(mmall_shipping record);
}