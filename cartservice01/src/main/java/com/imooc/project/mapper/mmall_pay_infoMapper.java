package com.imooc.project.mapper;

import com.imooc.project.entity.mmall_pay_info;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface mmall_pay_infoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_pay_info record);

    int insertSelective(mmall_pay_info record);

    mmall_pay_info selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_pay_info record);

    int updateByPrimaryKey(mmall_pay_info record);
}