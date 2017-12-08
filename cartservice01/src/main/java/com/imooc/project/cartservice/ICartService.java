package com.imooc.project.cartservice;

import com.imooc.project.cartVo.CartVo;
import com.imooc.project.common.ServerResponse;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/12/8.
 */
public interface ICartService {


    ServerResponse<CartVo> addProductCart(Integer userId,Integer productId,Integer count);

    ServerResponse addProductCookie(HttpServletRequest request, HttpServletResponse response, Integer productId, Integer count);
}
