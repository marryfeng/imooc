package com.imooc.project.controller.backend;

import com.imooc.project.common.ServerResponse;
import com.imooc.project.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/11/18.
 */
@Api(value = "商品的分类Controller")
@RestController
@RequestMapping("/manage/category/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取品类子节点(平级)")
    @RequestMapping(value = "get_category.do",method = RequestMethod.GET)
    public ServerResponse get_category(@RequestParam(value = "categoryId",defaultValue ="0") int categoryId){
      return  categoryService.getCategory(categoryId);
    }

    @ApiOperation(value = "增加节点")
    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    public ServerResponse<String> add_category(@RequestParam(value = "parentId",defaultValue ="0") Integer parentId,@RequestParam String categoryName){
        return categoryService.addCategory(parentId,categoryName);
    }

    @ApiOperation(value = "修改品类名字")
    @RequestMapping(value = "set_category_name.do",method = RequestMethod.POST)
    public ServerResponse<String> updateCategoryName(@RequestParam Integer categoryId,@RequestParam String categoryName){
       return categoryService.updateCategoryName(categoryId,categoryName);

    }

    @ApiOperation(value = "获取当前分类id及递归子节点categoryId")
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.GET)
    public ServerResponse getDeepCategory(@RequestParam Integer categoryId){
        return categoryService.getDeepCategory(categoryId);
    }








}
