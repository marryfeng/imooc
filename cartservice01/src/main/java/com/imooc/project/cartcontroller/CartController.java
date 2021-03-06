package com.imooc.project.cartcontroller;

import com.imooc.project.cartservice.ICartService;
import com.imooc.project.common.Const;
import com.imooc.project.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	
	 @ApiOperation(value = "更新购物车某个产品数量")
	 @PostMapping("update.do'")
	 @ApiImplicitParams({
            @ApiImplicitParam(name = "productId",value = "商品id",dataType ="Integer",paramType = "query"),
            @ApiImplicitParam(name = "count",value = "商品数量",dataType ="Integer",paramType = "query"),
    }
    )
	 public ServerResponse updateCartQuantity(Integer productId, Integer count){
		 Integer userId=21;
		 return iCartService.updateCartQuantity(userId,productId,count);
	 }
	 
	 @ApiOperation(value = "移除购物车某个产品")
	 @DeleteMapping("delete_product.do")
	 @ApiImplicitParam(name = "productIds",value = "商品id集合",dataType ="Integer",paramType = "query")
	 public ServerResponse deleteProduct(Integer[] productIds){
		 Integer userId=21;
		 return iCartService.deleteProductFromCart(userId,productIds);	 
	 }
	 
	 //购物车List列表
	 @ApiOperation(value = "展示购物车列表")
	 @GetMapping("list.do")
	 public ServerResponse deleteProduct(){
		 Integer userId=21;
		 return iCartService.showCartList(userId);	 
	 }

	 @ApiOperation(value = "购物车选中某个商品")
	 @GetMapping("select.do")
	 @ApiImplicitParam(name = "productId",value = "商品id",dataType ="Integer",paramType = "query")
	 public ServerResponse selectProduct(Integer productId){
	 	int userId=21;
		 return  iCartService.selectProduct(userId,productId,Const.CartProperty.CARTCHECKED);
	 }
	 
	 @ApiOperation(value = "购物车取消选中某个商品")
	 @GetMapping("un_select.do")
	 @ApiImplicitParam(name = "productId",value = "商品id",dataType ="Integer",paramType = "query")
	 public ServerResponse unselectProduct(Integer productId){
	 	Integer userId=21;
	 	return iCartService.unSelectProduct(userId,productId,Const.CartProperty.UN_CHECKED);
	 }
	 @ApiOperation(value = "查询在购物车里的产品数量")
	 @GetMapping("get_cart_product_count.do")
	 public ServerResponse getCartProductCount(){
		 Integer userId=21;
		 return iCartService.getCartProductCount(userId);
	 }
	 
	 @ApiOperation(value = "购物车全选")
	 @GetMapping("select_all.do")
	 public ServerResponse selectAllProduct(){
		 Integer userId=21;
		 return iCartService.selectAllProduct(userId, Const.CartProperty.CARTCHECKED);
	 }
	 @ApiOperation(value = "购物车取消全选")
	 @GetMapping("un_select_all.do")
	 public ServerResponse unselectAllProduct(){
		 Integer userId=21;
		 return iCartService.unselectAllProduct(userId,Const.CartProperty.UN_CHECKED);
	 }
	 
	 






}
