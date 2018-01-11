package com.imooc.project.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Administrator on 2017/11/2.
 */
public class Const {
    public static final String CURRENT_USER = "CURRENT_USER";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    //接口类
    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    //创建枚举类，注意类就的首字母大写，并且类是没有参数的，只有方法有参数来进行判断商品是否上下架
    //枚举类三步骤：定义属性，属性有对应的构造函数，属性的get方法编写
    public enum ProductStatus {
        ON_SALE(1, "商品在线");
        private String value;
        private Integer code;

        ProductStatus(Integer code, String value) {
            this.code = code;
            this.value = value;
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
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");

    }

    //购物车商品常用属性定义
    //向购物车中添加商品时，需要判断商品的库存量是否足够，如果库存不够，则提示库存不够
    public interface CartProperty {
        int CARTCHECKED = 1;
        int UN_CHECKED = 0;
        String TT_CART = "TT_CART";
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    //订单状态枚举类
    public enum OrderStatus {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");

        private int code;
        private String value;

         OrderStatus(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public Integer getCode() {
            return code;
        }
    }

    //支付宝支付的交易状态
    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    //支付平台
    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");

        PayPlatformEnum(int code, String value) {

            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum PaymentType{
        PAYONLINE(1,"在线支付"),
        DAOPAY(2,"货到付款")
        ;

        private Integer code;
        private String desc;

        PaymentType(Integer code,String desc){
            this.code=code;
            this.desc=desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
