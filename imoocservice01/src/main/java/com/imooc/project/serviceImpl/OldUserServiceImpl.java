package com.imooc.project.serviceImpl;

import com.imooc.project.entity.Imooc_user;
import com.imooc.project.mapper.Imooc_userMapper;
import com.imooc.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/25.
 */
@Service
public class OldUserServiceImpl implements UserService {
    @Autowired
    private Imooc_userMapper userMapper;

    public void insertUser(Imooc_user imooc_user) {
        userMapper.insert(imooc_user);
    }

    @Override
    public Imooc_user selectById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Map saveUser(Imooc_user user) {
        Map<String, Object> model = new HashMap<String, Object>();
        int i = userMapper.insert(user);
        if (i != 0) {
            model.put("status", "0");
            model.put("msg", "success");
        } else {
            model.put("status", "1");
            model.put("msg", "用户注册失败");
        }

        return model;
    }

    @Override
    public Map<String,Object> testifyUser(String userName, String userPassword) {
        Map<String,Object> model=new HashMap<String,Object>();
        //先验证用户名存在不，如果存在，则验证用户名和密码，如果不存在，则提示该用户不存在，需要注册
        Imooc_user user=userMapper.selectByName(userName);
        if (user!=null){
            //若用户名正确，则查询该用户是否存在，加密后与数据库的密码比对
            String mpassword=DigestUtils.md5DigestAsHex(userPassword.getBytes());
            Imooc_user u=userMapper.selectUser(userName,mpassword);
            if (u!=null){
                model.put("status","0");
                model.put("msg","u");
            }else{
                model.put("status","1");
                model.put("msg","登录密码错误，请重新登录");
            }
        }else{
            model.put("status","1");
            model.put("msg","该用户不存在，请注册");
        }
        return model;
    }

    @Override
    public Imooc_user selectByUsername(String userName) {
        return userMapper.selectByName(userName);
    }

    @Override
    public int updateUser(Imooc_user user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

}
