package com.imooc.project.orderController.backend;

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
 * Created by Administrator on 2017/12/22.
 */
@RestController
@Api(value = "订单功能模块",tags = {"imoocProject项目的订单功能模块"})
@RequestMapping("/manage/order/")
public class OrderController {
    @Autowired
    private IorderService orderService;

    @ApiOperation(value = "展示订单列表")
    @GetMapping("list.do")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "pageSize",value = "每页显示的数量",paramType = "path",dataType = "Integer",defaultValue = "10"),
             @ApiImplicitParam(name = "pageNum",value = "默认显示的页码",paramType = "path",dataType = "Integer",defaultValue = "1")
            }
    )
    /*
    * path parameters, such as /users/{id}
      query parameters, such as /users?role=admin
      header parameters, such as X-MyHeader: Value
       cookie parameters, which are passed in the Cookie header, such as Cookie: debug=0; csrftoken=BUSe35dohU3O1MZvDCU
    * */
    public ServerResponse showOrderList(Integer pageSize,Integer pageNum){
        Integer userId=21;
        return  orderService.showOrderList(userId,pageSize,pageNum);
    }

    @ApiOperation(value = "按订单号查询订单")
    @GetMapping("search.do")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "pageSize",value = "每页显示的数量",paramType = "path",dataType = "Integer",defaultValue = "10"),
                    @ApiImplicitParam(name = "pageNum",value = "默认显示的页码",paramType = "path",dataType = "Integer",defaultValue = "1"),
                    @ApiImplicitParam(name = "orderNo",value = "订单号",paramType = "path",dataType = "Long")


            }
    )
    public ServerResponse searchOrderByOrderNo(Integer pageSize,Integer pageNum,Long orderNo){
        Integer userId=21;
        return  orderService.searchOrder(userId,pageSize,pageNum,orderNo);
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

