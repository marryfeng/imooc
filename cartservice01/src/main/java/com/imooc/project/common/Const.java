package com.imooc.project.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Administrator on 2017/11/2.
 */
public class Const {
    public static final String CURRENT_USER="CURRENT_USER";
    public static final String EMAIL="email";
    public static final String USERNAME="username";
//接口类
    public interface  Role{
        int ROLE_CUSTOMER=0;
        int ROLE_ADMIN=1;
    }
    //创建枚举类，注意类就的首字母大写，并且类是没有参数的，只有方法有参数来进行判断商品是否上下架
    //枚举类三步骤：定义属性，属性有对应的构造函数，属性的get方法编写
    public enum ProductStatus{
        ON_SALE(1,"商品在线");
        private String value;
        private Integer code;
        ProductStatus(Integer code,String value){
            this.code=code;
            this.value=value;
        }
        public String getValue() {
            return value;
        }
        public Integer getCode() {
            return code;
        }

    }
    //前台商品搜索页面的排序
    public interface ProductListOrderBy {
        //这里为什么用set集合，因为set集合的contains的时间复杂度是O(1)，List的是O(n)
        Set<String> PRICE_ASC_DESC= Sets.newHashSet("price_desc","price_asc");

    }

}
