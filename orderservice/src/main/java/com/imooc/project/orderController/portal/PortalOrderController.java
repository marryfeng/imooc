package com.imooc.project.orderController.portal;

import com.imooc.project.common.ServerResponse;
import com.imooc.project.orderService.IorderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/12/25.
 */
@RestController
@Api(value = "订单功能模块",tags = {"imoocProject项目的前台订单功能模块"})
@RequestMapping("/order/")
public class PortalOrderController {

        @Autowired
        private IorderService orderService;

        @ApiOperation(value = "展示订单列表")
        @GetMapping("list.do")
        @ApiImplicitParams(
                {@ApiImplicitParam(name = "pageSize",value = "每页显示的数量",paramType = "path",dataType = "Integer",defaultValue = "10"),
                        @ApiImplicitParam(name = "pageNum",value = "默认显示的页码",paramType = "path",dataType = "Integer",defaultValue = "1")
                }
        )
        public ServerResponse showOrderList(Integer pageSize,Integer pageNum){
            Integer userId=21;
            return  orderService.showOrderList(userId,pageSize,pageNum);
        }

        @ApiOperation(value = "创建订单")
        @GetMapping("create.do")
        @ApiImplicitParams(
                {
                        @ApiImplicitParam(name = "shippingId",value = "收获id",paramType = "path",dataType = "Integer")
                }
        )
        public ServerResponse createOrder(Integer shippingId){
            Integer userId=21;
          return   orderService.createOrder(userId,shippingId);
        }
        @ApiOperation(value = "取消订单")
        @GetMapping("cancel.do")
        @ApiImplicitParams(
                {
                        @ApiImplicitParam(name = "shippingId",value = "收获id",paramType = "path",dataType = "Integer")
                }
        )
        public ServerResponse cancelOrder( Long orderNo){
            Integer userId=21;
            return  orderService.cancelOrder(userId,orderNo);
        }

        @ApiOperation(value = "获取订单的明细信息")
        @GetMapping("get_order_cart_product.do")
        @ApiImplicitParams(
                {
                        @ApiImplicitParam(name = "shippingId",value = "收获id",paramType = "path",dataType = "Integer")
                }
        )
        public ServerResponse getOrderProduct(Long orderNo){
            Integer userId=21;
            return  orderService.getOrderProduct(userId,orderNo);

        }


        @ApiOperation(value = "订单详情")
        @GetMapping("detail.do")
        @ApiImplicitParams(
                {@ApiImplicitParam(name = "pageSize",value = "每页显示的数量",paramType = "path",dataType = "Integer",defaultValue = "10"),
                        @ApiImplicitParam(name = "pageNum",value = "默认显示的页码",paramType = "path",dataType = "Integer",defaultValue = "1"),
                        @ApiImplicitParam(name = "orderNo",value = "订单号",paramType = "path",dataType = "Long")
                }
        )
        public ServerResponse showDetail(Integer pageSize,Integer pageNum,Long orderNo){
            Integer userId=21;
            return  orderService.showDetail(userId,pageSize,pageNum,orderNo);
        }
    }


