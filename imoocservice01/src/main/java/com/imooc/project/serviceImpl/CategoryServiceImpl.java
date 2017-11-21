package com.imooc.project.serviceImpl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_category;
import com.imooc.project.mapper.mmall_categoryMapper;
import com.imooc.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/11/18.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private mmall_categoryMapper  mmall_categoryMapper;

    @Override
    public ServerResponse getCategory(Integer categoryId) {
        List<mmall_category> list= mmall_categoryMapper.selectCategoryByParentId(categoryId);
       //CollectionUtils工具的使用
        if (!CollectionUtils.isEmpty(list)){
            return ServerResponse.createBySuccess(list);
        } else{
            return ServerResponse.createByErrorMessage("无该商品的列表");
        }
    }

    @Override
    public ServerResponse<String> addCategory(Integer parentId, String categoryName) {
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
    public ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName) {
       mmall_category mc= mmall_categoryMapper.selectByPrimaryKey(categoryId);
        mc.setName(categoryName);
        int i=mmall_categoryMapper.updateByPrimaryKey(mc);
        if (i!=0){
            return ServerResponse.createBySuccessMessage("修改名字成功");
        }
        return ServerResponse.createByErrorMessage("修改名字失败");
    }
   //这里递归获取子节点，即当前节点下的所以子节点以及子节点的节点都要列出
    @Override
    public ServerResponse getDeepCategory(Integer categoryId) {
       Set<mmall_category> categorySet= Sets.newHashSet();//这是guava缓存的技巧
      //在这里进行初始化Set集合
       findChildrenCategory(categorySet,categoryId);
       List<Integer> list= Lists.newArrayList();
       if (categoryId!=null){
           for (mmall_category categoryItem:categorySet) {
               list.add(categoryItem.getId());
           }
       }
       return ServerResponse.createBySuccess(list);
    }
    //递归代码的实现
    private Set<mmall_category> findChildrenCategory(Set<mmall_category> categorySet,Integer categoryId){
      mmall_category category=mmall_categoryMapper.selectByPrimaryKey(categoryId);
      if (category!=null){
          categorySet.add(category);
      }
      //categorySet其实是用来存储这些列表数据的
      //查找子节点递归函数必须有一个终止条件
       List<mmall_category> categoryList=mmall_categoryMapper.selectCategoryByParentId(categoryId);
        for (mmall_category categoryItem: categoryList) {
            findChildrenCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
