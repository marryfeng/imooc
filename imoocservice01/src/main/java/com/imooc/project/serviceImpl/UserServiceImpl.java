package com.imooc.project.serviceImpl;

import com.imooc.project.common.Const;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.common.TokenCache;
import com.imooc.project.entity.mmall_user;
import com.imooc.project.mapper.mmall_userMapper;
import com.imooc.project.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * Created by Administrator on 2017/11/2.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private mmall_userMapper mmall_userMapper;

    @Override
    public ServerResponse<mmall_user> userLogin(String username, String password) {
       //首先校验用户是否正确，如果正确，则验证密码是否正确，如果密码正确，则将用户返回，保存到session中
        int i= mmall_userMapper.checkUser(username);
        if (i==0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //因为数据库中的密码是md5加密了的，所以需要加密后与数据库对比
        String md5password= DigestUtils.md5DigestAsHex(password.getBytes());
        mmall_user user= mmall_userMapper.checkpassword(username,md5password);
        if (user==null){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        return ServerResponse.createBySuccess(user);
    }


    @Override
    public ServerResponse<String> insertUser(mmall_user user) {
       ServerResponse response= this.checkValid(user.getUsername(),Const.USERNAME);
        if (!response.isSuccess()){
            return response;
        }
        response= this.checkValid(user.getEmail(),Const.EMAIL);
        if (!response.isSuccess()){
            return response;
        }
        if (user.getRole()==1){
            user.setRole(Const.Role.ROLE_ADMIN);
        }else{
             user.setRole(Const.Role.ROLE_CUSTOMER);
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        int n= mmall_userMapper.insert(user);
        if (n==0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
      }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(str)){
            if (Const.USERNAME.equals(type)){
                int i= mmall_userMapper.checkUser(str);
                if (i>0){
                    return  ServerResponse.createByErrorMessage("用户已存在");
                }
            }
            if (Const.EMAIL.equals(type)){
               int  i=mmall_userMapper.checkEmail(str);
                if (i>0){
                    return  ServerResponse.createByErrorMessage("邮箱已存在,请更换");
                }
            }

        }else{
            return ServerResponse.createByErrorMessage("参数不正确");
        }

        return ServerResponse.createBySuccessMessage("校验成功！");
    }

    @Override
    public ServerResponse forgetGetQuestion(String username) {
        ServerResponse<String> response=this.checkValid(username,Const.USERNAME);
        if (response.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在！");
        }
        String question=mmall_userMapper.getQuestion(username);
        if (question!=null){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("用户未设置问题");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        if (StringUtils.isNotBlank(question)){
           int i= mmall_userMapper.checkAnswer(username,question,answer);
            if (i==0){
                return ServerResponse.createByErrorMessage("问题答案错误");
            }else{
                String forgetToken=UUID.randomUUID().toString();
                TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
                return ServerResponse.createBySuccess(forgetToken);
            }
        }
        return ServerResponse.createByErrorMessage("用户问题不能为空");
    }

    @Override
    public ServerResponse<String> resetPassword(String username, String passwordNew, String forgetToken) {
        //校验参数是否为空
        if (StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("token失效，参数错误，token需传递");
        }
        //如果token存在的话，校验获取的token和从缓存中获取的token是否一致，如果一致则更新密码
       String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token失效");
        }
        if (StringUtils.equals(token,forgetToken)){
          String md5Password=DigestUtils.md5DigestAsHex(passwordNew.getBytes());
         int i=mmall_userMapper.updatePassword(username,md5Password);
         if (i==0){
             return  ServerResponse.createByErrorMessage("密码重置错误");
         }
         return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("token错误，请重新获取token");
    }

    @Override
    public ServerResponse<String> resetPasswordLogin(String passwordOld, String passwordNew,String username) {
        if (StringUtils.isNotBlank(passwordOld)){
            mmall_user user=mmall_userMapper.checkpassword(username,passwordOld);
            if (user!=null){
                //更新密码
               String md5Password=DigestUtils.md5DigestAsHex(passwordNew.getBytes());
                int i=mmall_userMapper.updatePassword(username,md5Password);
                if (i!=0){
                    return ServerResponse.createBySuccessMessage("更新密码成功");
                }else{
                    return ServerResponse.createByErrorMessage("更新密码失败");
                }
            }else{
                return ServerResponse.createByErrorMessage("旧密码输入错误");
            }
        }

        return ServerResponse.createByErrorMessage("请输入旧密码");
    }

    @Override
    public ServerResponse<mmall_user> updateUserInfo(mmall_user user) {
        //这里需要注意下：username是不能被更新的，email是需要校验，新的email是否已经存在，校验这个email是否是其他用户的email
        int count=mmall_userMapper.checkExitEmail(user.getId(),user.getEmail());
        if (count==0){
            return  ServerResponse.createByErrorMessage("邮箱已存在，请更换新的邮箱");
        }
        //这样子写的话可只更新一部分内容，不用全部更新，
        mmall_user updateUser=new mmall_user();
        updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        count=mmall_userMapper.updateByPrimaryKey(updateUser);
        if (count==0){
            return ServerResponse.createByErrorMessage("更新用户失败");
        }
        return ServerResponse.createBySuccess(updateUser);
    }

    @Override
    public ServerResponse checkUserRole(mmall_user user) {
        if (user.getRole()==(Const.Role.ROLE_ADMIN)){
            return ServerResponse.createBySuccess();
        }else{
            return ServerResponse.createByError();
        }

    }


}
