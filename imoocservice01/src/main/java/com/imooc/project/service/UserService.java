package com.imooc.project.service;

import com.imooc.project.entity.Imooc_user;

import java.util.Map;

/**
 * Created by Administrator on 2017/10/25.
 */

public interface UserService {
     void insertUser(Imooc_user imooc_user);
     Imooc_user selectById(Integer id);
     Map<String,Object> saveUser(Imooc_user user);
     Map<String,Object> testifyUser(String userName,String userPassword);
     Imooc_user selectByUsername(String userName);
     int updateUser(Imooc_user user);

}
