package com.imooc.project.mapper;

import com.imooc.project.entity.mmall_cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface mmall_cartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(mmall_cart record);

    int insertSelective(mmall_cart record);

    mmall_cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(mmall_cart record);

    int updateByPrimaryKey(mmall_cart record);

    mmall_cart selectProductExit(@Param("userId") Integer userId,@Param("productId") Integer productId);

    List<mmall_cart> selectProductByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);
}