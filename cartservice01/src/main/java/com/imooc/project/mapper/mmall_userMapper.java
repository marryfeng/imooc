package com.imooc.project.mapper;

import com.imooc.project.entity.mmall_user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface mmall_userMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_user record);

    int insertSelective(mmall_user record);

    mmall_user selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_user record);

    int updateByPrimaryKey(mmall_user record);

    int checkUser(String username);
    int checkEmail(String email);
   mmall_user checkpassword(@Param("username") String username, @Param("password") String password);

    String getQuestion(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePassword(@Param("username") String username, @Param("passwordNew") String passwordNew);

    int checkExitEmail(@Param("id") Integer id, @Param("email") String email);
}