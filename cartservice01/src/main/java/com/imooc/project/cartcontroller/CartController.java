package com.imooc.project.cartcontroller;

import com.imooc.project.cartservice.ICartService;
import com.imooc.project.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 购物车功能模块实现
 */
@Api(value = "购物车功能实现",tags = {"imoocProject项目的购物车功能实现"})
@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @ApiOperation(value = "向购物车内添加商品")
    @PostMapping("add.do")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",dataType ="Integer",paramType = "query"),
            @ApiImplicitParam(name = "productId",value = "商品id",dataType ="Integer",paramType = "query"),
            @ApiImplicitParam(name = "count",value ="商品数量",dataType ="Integer",paramType = "query")
    }
    )
    //这里应该是先判断用户是否登录的，登录之后获取用户的id，然后查询该用户的购物车，在这里模拟用户
    //购物车功能流程：
    //当用户未登录的状态下，加入购物车，此时商品是保存在cookie中的，用户换台电脑购物车就失效。当用户结算的时候需要用户的登录，这一块的处理也是计算价格库存
    // 在用户登录的前提下，查询用户购物车中是否有该商品，如果没有，则将商品添加到购物车中（这中间牵扯到库存和商品价格的处理，该商品的总结，该用户购物车最终的总价），如果有该商品，则增加商品的数量，更新用户的购物车，计算价格
    public ServerResponse addProductCart(HttpServletRequest request,HttpServletResponse response,Integer userId, Integer productId, Integer count){
       ServerResponse serverResponse;
        if (userId!=null && userId==21) {
             serverResponse = iCartService.addProductCart(userId, productId, count);
        }else{
            //用户未登录状态下的加入购物车功能
            serverResponse= iCartService.addProductCookie(request,response,productId,count);
        }
    return serverResponse;
    }






}
