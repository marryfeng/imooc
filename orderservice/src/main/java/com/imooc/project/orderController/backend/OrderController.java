package com.imooc.project.orderController.backend;

import com.imooc.project.orderService.IorderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/12/22.
 */
@RestController
@Api(value = "订单功能模块",tags = {"imoocProject项目的订单功能模块"})
public class OrderController {
    @Autowired
    private IorderService orderService;


}

