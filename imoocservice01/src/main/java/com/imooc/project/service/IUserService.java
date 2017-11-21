package com.imooc.project.service;

import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_user;

/**
 * Created by Administrator on 2017/11/2.
 */
public interface IUserService {
    ServerResponse<mmall_user> userLogin(String username,String password);
    ServerResponse<String> insertUser(mmall_user user);
    ServerResponse<String> checkValid(String str,String type);
    ServerResponse<String> forgetGetQuestion(String username);
    ServerResponse<String> checkAnswer(String username,String question,String answer);
    ServerResponse<String> resetPassword(String username,String passwordNew,String forgetToken);
    ServerResponse<String> resetPasswordLogin(String passwordOld,String passwordNew,String username);

    ServerResponse<mmall_user> updateUserInfo(mmall_user user);
    ServerResponse checkUserRole(mmall_user user);
}
