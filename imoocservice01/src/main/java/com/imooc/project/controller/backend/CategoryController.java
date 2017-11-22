package com.imooc.project.controller.backend;

import com.imooc.project.common.Const;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_user;
import com.imooc.project.service.CategoryService;
import com.imooc.project.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/11/18.
 */
@Api(value = "imoocProject商品的分类Controller",tags = {"imoocProject商品的分类Controller"})
@RestController
@RequestMapping("/manage/category/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private IUserService iUserService;

    @ApiOperation(value = "获取品类子节点(平级)")
    @RequestMapping(value = "get_category.do",method = RequestMethod.GET)
    public ServerResponse get_category(@RequestParam(value = "categoryId",defaultValue ="0") int categoryId){
      return  categoryService.getCategory(categoryId);
    }

    @ApiOperation(value = "增加节点")
    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    public ServerResponse<String> add_category(HttpSession session,@RequestParam(value = "parentId",defaultValue ="0") Integer parentId,@RequestParam String categoryName){
       mmall_user user=(mmall_user) session.getAttribute(Const.CURRENT_USER);
        //用户不为空了，则验证是否是后台管理员，验证角色
       if (user==null){
           return ServerResponse.createByErrorMessage("用户为空，请登录");
       }
       if (iUserService.checkUserRole(user).isSuccess()){
           return categoryService.addCategory(parentId,categoryName);
       }else{
           return ServerResponse.createByErrorMessage("非管理员，无权限");

       }
    }

    @ApiOperation(value = "修改品类名字")
    @RequestMapping(value = "set_category_name.do",method = RequestMethod.POST)
    public ServerResponse<String> updateCategoryName(HttpSession session, @RequestParam Integer categoryId, @RequestParam String categoryName){
        mmall_user user=(mmall_user) session.getAttribute(Const.CURRENT_USER);
        //用户不为空了，则验证是否是后台管理员，验证角色
        if (user==null){
            return ServerResponse.createByErrorMessage("用户为空，请登录");
        }
        if (iUserService.checkUserRole(user).isSuccess()){
            return categoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServerResponse.createByErrorMessage("非管理员，无权限");
        }
    }

    @ApiOperation(value = "获取当前分类id及递归子节点categoryId")
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.GET)
    public ServerResponse getDeepCategory(@RequestParam Integer categoryId){
      //这里理解的有偏差：意思是若当前节点是10,子节点是1000,1000的子节点是10000，则当输入10的时候，子节点1000和子节点10000的子节点都要输出，这就涉及到了递归
        return categoryService.getDeepCategory(categoryId);
    }








}
