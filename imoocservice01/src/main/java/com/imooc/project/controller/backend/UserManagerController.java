package com.imooc.project.controller.backend;

import com.imooc.project.common.Const;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_user;
import com.imooc.project.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/11/10.
 */
@Api(value = "imoocProject后台用户模块Controller",tags = {"imoocProject后台用户模块Controller"})
@RestController
@RequestMapping("/manage/user/")
public class UserManagerController {

    @Autowired
    private IUserService iUserService;

    @ApiOperation(value = "后台用户登录")
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public ServerResponse  manageUserLogin(@RequestParam String username, @RequestParam String password, HttpSession session){
        ServerResponse<mmall_user> response=iUserService.userLogin(username,password);
        if (response.isSuccess()){
            if (response.getData().getRole()==Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENT_USER,response.getData());
                return  response;
            }else{
                return ServerResponse.createByErrorMessage("您不是管理员无权登录");
            }
        }
        return response;

    }






}
