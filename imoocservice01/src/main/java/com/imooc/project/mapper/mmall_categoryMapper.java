package com.imooc.project.mapper;

import com.imooc.project.entity.mmall_category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface mmall_categoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_category record);

    int insertSelective(mmall_category record);

    mmall_category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_category record);

    int updateByPrimaryKey(mmall_category record);
}