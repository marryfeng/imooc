package com.imooc.project.common;

/**
 * 定义返回类型的code数据表示
 */
public enum ResponseCode {
    //枚举类：系统为这两个对象默认的加上了public static final
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");
    // 1.枚举类对象的属性不应允许被改动，所以应该使用private final 修饰
    private final int code;
    private final String desc;
    // 2.枚举类的使用private final 修饰的属性因该在构造器中为其赋值
    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
   //供外界调用开放入口
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }



}
