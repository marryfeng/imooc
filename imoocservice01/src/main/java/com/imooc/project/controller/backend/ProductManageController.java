package com.imooc.project.controller.backend;

import com.imooc.project.common.Const;
import com.imooc.project.common.ResponseCode;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_product;
import com.imooc.project.entity.mmall_user;
import com.imooc.project.service.IUserService;
import com.imooc.project.service.ProductManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/11/21.
 */
@Api(value = "imoocProduct后台商品管理的Controller",tags = {"imoocProduct后台商品管理的Controller"})
@RestController
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private ProductManageService productManageService;
    @Autowired
    private IUserService iUserService;

    @ApiOperation(value = "新增OR更新产品")
    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    public ServerResponse<String> updateOrSaveProduct(HttpSession session,@RequestBody mmall_product product){
    mmall_user user=(mmall_user) session.getAttribute(Const.CURRENT_USER);
    if (user==null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"用户未登录，请登录");
    }
    if (iUserService.checkUserRole(user).isSuccess()){
        return productManageService.updateOrAdd(product);
    }
    return ServerResponse.createByErrorMessage("用户无权限操作");
    }

    @ApiOperation(value = "产品上下架")
    @RequestMapping(value = "set_sale_status.do",method = RequestMethod.POST)
    public ServerResponse<String> setSaleStatus(HttpSession session, @RequestParam  Integer productId,@RequestParam  Integer status){
        mmall_user user=(mmall_user) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"用户未登录，请登录");
        }
        if (iUserService.checkUserRole(user).isSuccess()){
            return productManageService.setProductStatus(productId,status);
        }
        return ServerResponse.createByErrorMessage("用户无权限操作");
    }
    }







