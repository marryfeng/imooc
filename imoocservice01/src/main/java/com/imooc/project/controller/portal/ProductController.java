package com.imooc.project.controller.portal;

import com.github.pagehelper.PageInfo;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.service.ProductManageService;
import com.imooc.project.vo.ProductDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *前台商品的功能模块实现
 */
@Api(value = "门户商品的功能实现",tags = {"imoocProject项目Portal门户商品的功能实现"})
@RestController
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private ProductManageService productService;

    @RequestMapping(value="list.do",method = RequestMethod.GET)
    @ApiOperation(value = "产品搜索及动态排序List")
    @ApiImplicitParams({
            @ApiImplicitParam(name="categoryId",value="商品分类id",dataType="Integer", paramType = "query",example="26"),
            @ApiImplicitParam(name="keyword",value="搜索关键字",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="pageNum",value="当前页",dataType="Integer",paramType = "query",defaultValue = "1",required = true),
            @ApiImplicitParam(name="pageSize",value="当前多少页",dataType="Integer", paramType = "query",defaultValue = "10",required = true),
            @ApiImplicitParam(name="orderBy",value="排序",dataType="String", paramType = "query",defaultValue = "price_desc")
    })
    public ServerResponse<PageInfo> getProtalProductList(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy){
      return productService.getPortalProductList(categoryId,keyword,orderBy,pageNum,pageSize);
    }
    //展示商品详情,这里注意判断商品是否是上架的状态，如果没有则提示上面已经下架
    @RequestMapping(value = "detail.do",method = RequestMethod.GET)
    @ApiOperation(value = "产品detail")
    @ApiImplicitParam(name="productId",value="商品id",dataType="Integer", paramType = "query",example="26")
    public ServerResponse<ProductDetail> getPortalDetail(Integer productId){
        return productService.getPortalProductDetail(productId);

    }



}
