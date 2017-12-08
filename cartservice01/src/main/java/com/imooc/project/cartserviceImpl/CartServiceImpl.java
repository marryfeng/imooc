package com.imooc.project.cartserviceImpl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.imooc.project.cartVo.CartItmCookieVo;
import com.imooc.project.cartVo.CartProductVoList;
import com.imooc.project.cartVo.CartVo;
import com.imooc.project.cartservice.ICartService;
import com.imooc.project.common.Const;
import com.imooc.project.common.ResponseCode;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_cart;
import com.imooc.project.entity.mmall_product;
import com.imooc.project.mapper.mmall_cartMapper;
import com.imooc.project.mapper.mmall_productMapper;
import com.imooc.project.util.BigDecimalUtil;
import com.imooc.project.util.CookieUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车功能模块实现
 */
@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private mmall_cartMapper cartMapper;
    @Autowired
    private mmall_productMapper productMapper;
    //购物车功能流程：
    //当用户未登录的状态下，加入购物车，此时商品是保存在cookie中的，用户换台电脑购物车就失效。当用户结算的时候需要用户的登录，这一块的处理也是计算价格库存
    // 在用户登录的前提下，查询用户购物车中是否有该商品，如果没有，则将商品添加到购物车中（这中间牵扯到库存和商品价格的处理，该商品的总结，该用户购物车最终的总价），如果有该商品，则增加商品的数量，更新用户的购物车，计算价格
    //这种情况是淘宝网站使用的，只有用户的登录的状态下商品才可以加入购物车，保证了数据的同步
    @Override
    public ServerResponse<CartVo> addProductCart(Integer userId,Integer productId,Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //查询该用户的购物车中是否有该商品
        mmall_cart cart = cartMapper.selectProductExit(userId, productId);
        mmall_product product = productMapper.selectByPrimaryKey(productId);
        if (cart == null) {
            //如果购物车为空，则购物车没有此商品，需要插入到购物车
            mmall_cart cartItem = new mmall_cart();
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.CartProperty.CARTCHECKED);
            int i = cartMapper.insertSelective(cartItem);
        } else {
            //如果购物车不为空，则已有此商品，需要更新购物车商品的数量
            int stock = product.getStock();
                cart.setQuantity(cart.getQuantity() + count);
                cartMapper.updateByPrimaryKeySelective(cart);
        }
          return this.list(userId);
    }


    public ServerResponse<CartVo> list (Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }
    private CartVo getCartVoLimit(Integer userId){
        //封装vo展示给前台,查询用户购物车中的商品展示
        CartVo cartVo=new CartVo();
        BigDecimal cartTotalPrice=new BigDecimal("0");
        List<mmall_cart> cartList=cartMapper.selectProductByUserId(userId);
        List<CartProductVoList> list= Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cartList)) {
            for (mmall_cart cartItem : cartList) {
                //根据购物车的商品id来查询该商品的信息

                //开始封装这个包装显示类
                CartProductVoList cartProductVoList = new CartProductVoList();
                cartProductVoList.setId(cartItem.getId());
                cartProductVoList.setUserId(userId);
                cartProductVoList.setProductId(cartItem.getProductId());
                mmall_product productItem = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (productItem!=null){
                    cartProductVoList.setProductMainImage(productItem.getMainImage());
                    cartProductVoList.setProductName(productItem.getName());
                    cartProductVoList.setProductStatus(productItem.getStatus());
                    cartProductVoList.setProductStock(productItem.getStock());
                    cartProductVoList.setProductSubtitle(productItem.getSubtitle());
                    cartProductVoList.setProductPrice(productItem.getPrice());
                    //商品库存限制这个功能
                    int buyLimitCount = 0;
                    if (cartItem.getQuantity()<= productItem.getStock()) {
                        buyLimitCount=cartItem.getQuantity();
                        cartProductVoList.setLimitQuantity(Const.CartProperty.LIMIT_NUM_SUCCESS);
                    } else {
                        //这一步需要注意，当库存不足时，需要更新购物车库存
                        buyLimitCount = productItem.getStock();
                        cartProductVoList.setLimitQuantity(Const.CartProperty.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存,
                        mmall_cart cartForQuantity = new mmall_cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVoList.setQuantity(buyLimitCount);
                    //购物车总价格的问题：一个是该产品的总价，一个是购物车中最后的商品总价
                    //这个是该商品的总价格：商品价格*商品的数量
                    cartProductVoList.setProductTotalPrice(BigDecimalUtil.mul(productItem.getPrice().doubleValue(),cartItem.getQuantity().doubleValue()));
                    cartProductVoList.setProductChecked(cartItem.getChecked());
                }
                //这里的总价格默认为购物车商品全部选中的状态下计算的价格
                if (cartItem.getChecked()==Const.CartProperty.CARTCHECKED){
                    cartTotalPrice=BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVoList.getProductTotalPrice().doubleValue());
                }
                list.add(cartProductVoList);
            }
        }
        cartVo.setCartProductVoLists(list);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));//如果全选则返回true，非全选则返回false
        cartVo.setCartTotalPrice(cartTotalPrice);
        return cartVo;
    }
    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        //查询购物车中该用户下选中的状态，checked=0，即未被选中状态，如果返回0，则表明购物车中全部选中的状态，返回true
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;

    }
//用户未登录的情况下购物车保存到cookie中，京东网站使用的方法
    @Override
    public ServerResponse addProductCookie(HttpServletRequest request, HttpServletResponse response, Integer productId, Integer count) {
         /*添加购物车商品，首先购物车商品是保存在cookie中的，因为我们只要不付款是没有什么作用的。
         * 如何从cookie中读取购物车列表呢，是利用request来实现的。
         * 第一步：首先判断cookie中是否存在该商品，如果存在，则商品数量加1，
         * 如果没有则根据商品id从rest工程中获取该商品，将商品写入cookie。
         */
        CartItmCookieVo cartItmCookieVo=null;
        //从cookie中读取商品
      List<CartItmCookieVo> cookieList=this.getProductByCookie(request);
      List<CartItmCookieVo> list=Lists.newArrayList();
      //遍历这个列表，查询购物车中是否存在此商品，如果存在则更新，如果不存在则写入cookie中
        for (CartItmCookieVo cartItem: cookieList) {
            if (cartItem.getProductId()==productId){
                cartItem.setQuantity(cartItem.getQuantity()+count);
                cartItmCookieVo=cartItem;
                break;
            }else{
                cartItmCookieVo=new CartItmCookieVo();
               mmall_product product=productMapper.selectByPrimaryKey(productId);
                cartItmCookieVo.setId(cartItem.getId());
                cartItmCookieVo.setProductId(productId);
                cartItmCookieVo.setProductName(product.getName());
                if (product.getStock()>=cartItem.getQuantity()) {
                    cartItmCookieVo.setQuantity(cartItem.getQuantity());
                }else{
                    cartItmCookieVo.setQuantity(product.getStock());
                }
                cartItmCookieVo.setProductPrice(product.getPrice());
                cartItmCookieVo.setProductMainImage(product.getMainImage());
                list.add(cartItmCookieVo);
                CookieUtils.setCookie(request,response,"TT_CART",JSON.toJSONString(list),true);
            }
        }
        return ServerResponse.createBySuccess(list);
    }
//从cookie中读取商品列表
    private List<CartItmCookieVo> getProductByCookie(HttpServletRequest request) {
       String cookie=CookieUtils.getCookieValue(request,"TT_CART",true);
       //因为cookie中存放的是json格式的数据，所以如果需要转换成list形式
        if (cookie==null){
            return new ArrayList<>();
        }else{
            //这里用到了使用阿里巴巴的fastjson将json转为list集合的形式
            List<CartItmCookieVo> cartcookieList = JSON.parseArray(cookie, CartItmCookieVo.class);
            return cartcookieList;
        }
    }
}
