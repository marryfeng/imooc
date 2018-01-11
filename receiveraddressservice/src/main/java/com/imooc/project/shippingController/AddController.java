package com.imooc.project.shippingController;

import com.imooc.project.common.ServerResponse;
import com.imooc.project.shippingService.AddService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/1/11.
 */
@RestController
@Api(value = "收货人地址controller",tags = {"imoocProject项目的收货人地址模块功能实现"})
@RequestMapping("/shipping/")
public class AddController {

    @Autowired
    private AddService addService;

    @ApiOperation(value = "展示收货人信息")
    @RequestMapping("list.do")
    public ServerResponse showList(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
          return   addService.showAddList(pageNum,pageSize);
    }







}
