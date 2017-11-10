package com.imooc.project.mapper;

import com.imooc.project.entity.Imooc_user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface Imooc_userMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(Imooc_user record);

    int insertSelective(Imooc_user record);

    Imooc_user selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(Imooc_user record);

    int updateByPrimaryKey(Imooc_user record);
    Imooc_user selectByName(String userName);
    Imooc_user selectUser(@Param("userName") String userName,@Param("userPassword") String userPassword);
}