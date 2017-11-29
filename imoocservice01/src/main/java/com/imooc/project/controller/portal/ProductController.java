package com.imooc.project.controller.portal;

import com.imooc.project.common.ServerResponse;
import com.imooc.project.service.ProductManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *前台商品的功能模块实现
 */
@Api(value = "门户商品的功能实现",tags = {"门户商品的功能实现"})
@RestController
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private ProductManageService productService;

    @RequestMapping("list.do")
    @ApiOperation(value = "产品搜索及动态排序List")
    @ApiImplicitParams({
            @ApiImplicitParam(name="categoryId",value="商品分类id",dataType="Integer", paramType = "query",example="26"),
            @ApiImplicitParam(name="keyword",value="搜索关键字",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="pageNum",value="当前页",dataType="Integer",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name="pageSize",value="当前多少页",dataType="Integer", paramType = "query",defaultValue = "10"),
            @ApiImplicitParam(name="orderBy",value="排序",dataType="String", paramType = "query",defaultValue = "price_desc")
    }
    )
    public ServerResponse getProductList(Integer categoryId,String keyword,Integer pageNum,Integer pageSize,String orderBy){
     return null;
    }


}
