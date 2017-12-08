package com.imooc.project.controller.backend;

import com.imooc.project.common.Const;
import com.imooc.project.common.ResponseCode;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_product;
import com.imooc.project.entity.mmall_user;
import com.imooc.project.service.IUserService;
import com.imooc.project.service.ProductManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/11/21.
 */
@Api(value = "imoocProduct后台商品管理的Controller",tags = {"imoocProduct后台商品管理的Controller"})
@RestController
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private ProductManageService productManageService;
    @Autowired
    private IUserService iUserService;

    @ApiOperation(value = "新增OR更新产品")
    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    public ServerResponse<String> updateOrSaveProduct(HttpSession session,@RequestBody mmall_product product){
    mmall_user user=(mmall_user) session.getAttribute(Const.CURRENT_USER);
    if (user==null){
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"用户未登录，请登录");
    }
    if (iUserService.checkUserRole(user).isSuccess()){
        return productManageService.updateOrAdd(product);
    }
    return ServerResponse.createByErrorMessage("用户无权限操作");
    }

    @ApiOperation(value = "产品上下架")
    @RequestMapping(value = "set_sale_status.do",method = RequestMethod.POST)
    public ServerResponse<String> setSaleStatus(HttpSession session, @RequestParam  Integer productId,@RequestParam  Integer status){
        mmall_user user=(mmall_user) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"用户未登录，请登录");
        }
        if (iUserService.checkUserRole(user).isSuccess()){
            return productManageService.setProductStatus(productId,status);
        }
        return ServerResponse.createByErrorMessage("用户无权限操作");
    }
    @ApiOperation(value = "产品list")
    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    public ServerResponse getProductList(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
       ServerResponse response=productManageService.getProductList(pageNum,pageSize);
       return response;
    }
    //@ApiParam() 用于方法，参数，字段说明；表示对参数的添加元数据（说明或是否必填等）
    //@RequestParam这个注解是必须要写入的参数。如果使用swagger2，若想有的参数可以为空的话，可以用它的注解：@RequestParam
    @ApiOperation(value = "商品搜索")
    @RequestMapping(value = "search.do",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="productId",value="商品id",dataType="Integer", paramType = "query",example="26"),
            @ApiImplicitParam(name="productName",value="商品名称",dataType="String", paramType = "query")}
            )
    //利用这个注解就可以实现参数可以不必输入
    public ServerResponse productSearch(Integer productId, String productName, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
     return productManageService.searchProduct(productId,productName,pageNum,pageSize);
    }

    @ApiOperation(value = "商品详情")
    @RequestMapping(value = "detail.do",method = RequestMethod.GET)
    public ServerResponse getProductDetail(@RequestParam  Integer productId){
        return productManageService.getProductDetail(productId);

    }
    //使用springMVC的multipartFile
    @ApiOperation(value = "图片上传")
    @RequestMapping(value = "upload.do",method = RequestMethod.POST)
    public ServerResponse pictureUpload(MultipartFile file, HttpServletRequest request){

        return null;
    }
    @ApiOperation(value = "富文本上传图片")
    @RequestMapping(value = "richtext_img_upload.do",method = RequestMethod.POST)
    public ServerResponse richtextImgUpload(){
        return null;
    }








    }







