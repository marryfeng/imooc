package com.imooc.project.util;

import java.math.BigDecimal;

/**
 *商业计算中，如何防止精度的丢失，使用Bigdecimal来解决这个问题
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * 确的浮点数运算，包括加减乘除和四舍五入。
 */
public class BigDecimalUtil {
//工具类(utility class)，实例化对它们没有意义的工具类。这时候，就要做到不让该类被实例化.方法是使用私有构造器
private BigDecimalUtil(){}
//私有构造器这样子就表明这个工具类不能被实例化
// 重写BigDecimal的加减乘除
    public static BigDecimal add(Double v1,Double v2){
    BigDecimal b1=new BigDecimal(Double.toString(v1));
    BigDecimal b2=new BigDecimal(Double.toString(v2));
    return b1.add(b2);
    }
    public static BigDecimal sub(Double v1,Double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }
    public static BigDecimal mul(Double v1,Double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }
    public static BigDecimal div(Double v1,Double v2){
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2=new BigDecimal(Double.toString(v2));
        //<p>The new {@link #divide(BigDecimal, int, RoundingMode)}
        return b1.divide(b2,2,BigDecimal.ROUND_FLOOR);
    }
    //除法的时候需要注意除不尽的情况，重写BigDecimal的除法保留两位小数的方法,四舍五入。

}
