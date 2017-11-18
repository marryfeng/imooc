package com.imooc.project.serviceImpl;

import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_category;
import com.imooc.project.mapper.mmall_categoryMapper;
import com.imooc.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/11/18.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private mmall_categoryMapper  mmall_categoryMapper;

    @Override
    public ServerResponse getCategory(int categoryId) {
        List<mmall_category> list= mmall_categoryMapper.selectCategoryByParentId(categoryId);
        if (list!=null&&list.size()>0){
            return ServerResponse.createBySuccess(list);
        } else{
            return ServerResponse.createByErrorMessage("无该商品的列表");
        }
    }

    @Override
    public ServerResponse<String> addCategory(int parentId, String categoryName) {
        mmall_category mc=new mmall_category();
        mc.setParentId(parentId);
        mc.setName(categoryName);
       int i=mmall_categoryMapper.insertSelective(mc);
        if (i!=0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse<String> updateCategoryName(int categoryId, String categoryName) {
       mmall_category mc= mmall_categoryMapper.selectByPrimaryKey(categoryId);
        mc.setName(categoryName);
        int i=mmall_categoryMapper.updateByPrimaryKey(mc);
        if (i!=0){
            return ServerResponse.createBySuccessMessage("修改名字成功");
        }
        return ServerResponse.createByErrorMessage("修改名字失败");
    }

    @Override
    public ServerResponse getDeepCategory(int categoryId) {
       List<Integer> list= mmall_categoryMapper.getDeepCategory(categoryId);
       if (list!=null&&list.size()>0){
           return ServerResponse.createBySuccess(list);
       }else{
           return ServerResponse.createByErrorMessage("无权限");
       }

    }
}
