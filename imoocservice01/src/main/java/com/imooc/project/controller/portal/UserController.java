package com.imooc.project.controller.portal;


import com.imooc.project.common.Const;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_user;
import com.imooc.project.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/11/2.
 */
@Api(value = "imoocProject用户模块Controller",tags = {"imoocProject用户模块Controller"})
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @ApiOperation(value = "用户登录接口")
    @RequestMapping(value = "login.do",method = RequestMethod.GET)
    public ServerResponse<mmall_user> login(@RequestParam String username, @RequestParam String password, HttpSession session){
        ServerResponse<mmall_user> serverResponse=iUserService.userLogin(username,password);
        if (serverResponse.isSuccess()){
            //这里session中存放的是用户的全部信息，但是这样是不安全的，一般都是存放用户名
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }
    @ApiOperation(value = "用户注册接口")
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    public ServerResponse<String> register(@RequestBody mmall_user user ){
       return iUserService.insertUser(user);
    }

    @ApiOperation(value = "退出用户登录")
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出成功");
    }
    @ApiOperation(value = "防止恶意用户之间通过接口调用注册功能的校验接口")
    @RequestMapping(value = "check_valid.do",method = RequestMethod.GET)
    public ServerResponse<String> checkValid(@RequestParam String str,@RequestParam String type ){
        return iUserService.checkValid(str,type);
    }
    @ApiOperation(value = "获取登录用户信息")
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
    public ServerResponse<mmall_user> getUserInfo(HttpSession session){
        mmall_user user=(mmall_user) session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return  ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户");
    }

    @ApiOperation(value = "忘记密码,获取用户的问题")
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.GET)
    public ServerResponse<String> forgetGetQuestion(@RequestParam String username){
        return iUserService.forgetGetQuestion(username);
    }
    @ApiOperation(value = "提交问题答案并验证答案是否正确")
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.GET)
    public ServerResponse<String> checkAnswer(@RequestParam String username,@RequestParam String question,@RequestParam String answer){
        return iUserService.checkAnswer(username,question,answer);
    }
    @ApiOperation(value = "忘记密码的重设密码")
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(@RequestParam String username,@RequestParam String passwordNew,@RequestParam String forgetToken){
        return iUserService.resetPassword(username,passwordNew,forgetToken);
    }
    @ApiOperation(value = ".登录中状态重置密码 ")
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    public ServerResponse<String> resetPasswordLogin(@RequestParam String passwordOld,@RequestParam String passwordNew,HttpSession session){
      mmall_user user=(mmall_user) session.getAttribute(Const.CURRENT_USER);
      if (user==null){
          return ServerResponse.createByErrorMessage("用户未登录，请登录");
      }else {
          return iUserService.resetPasswordLogin(passwordOld, passwordNew, user.getUsername());
        }
      }
    @ApiOperation(value = "登录状态更新个人信息 ")
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    public ServerResponse<mmall_user> updateInfomation(@RequestBody  mmall_user user,HttpSession session){
        mmall_user u=(mmall_user) session.getAttribute(Const.CURRENT_USER);
        if (u==null){
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
         user.setId(u.getId());
         ServerResponse<mmall_user> response=iUserService.updateUserInfo(user);
         if (response.isSuccess()){
             session.setAttribute(Const.CURRENT_USER,response.getData());
         }
         return response;

    }
    //这里注意一点：到底session中存放的是对象的详细信息还是对象名，我们是否需要从数据库中获取这个user。现在写的方法与视频不一样，需要验证下
    @ApiOperation(value = "获取当前登录用户的详细信息，并强制登录")
    @RequestMapping(value = "get_information.do",method = RequestMethod.GET)
    public ServerResponse getInfomation(HttpSession session){
        mmall_user u=(mmall_user) session.getAttribute(Const.CURRENT_USER);
        if (u==null){
            return ServerResponse.createByErrorCodeMessage(10,"用户未登录,无法获取当前用户信息,status=10,强制登录");
        }else {
            return ServerResponse.createBySuccess(u);
        }



    }











}
