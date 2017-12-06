package com.imooc.project.mapper;

import com.imooc.project.entity.mmall_category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface mmall_categoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_category record);

    int insertSelective(mmall_category record);

    mmall_category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_category record);

    int updateByPrimaryKey(mmall_category record);

    //注意这里传递的类型：从controller传递过来的是Integer则，这个mapper中的parameterType必须是Integer
    //若从Controller传递过来的是int，则mapper中parameterType必须是int。两者不能互用。虽然在拆箱装箱的时候可以用。

    List<mmall_category> selectCategoryByParentId(Integer parentId);

    List<Integer> getDeepCategory(Integer categoryId);
}