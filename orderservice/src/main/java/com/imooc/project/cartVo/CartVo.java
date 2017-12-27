package com.imooc.project.cartVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 一个大的vo对象，用于装载购物车功能模块的显示信息
 */
public class CartVo {

    private List<CartProductVoList>  cartProductVoLists;
    private boolean allChecked;
    private BigDecimal cartTotalPrice;

    public List<CartProductVoList> getCartProductVoLists() {
        return cartProductVoLists;
    }

    public void setCartProductVoLists(List<CartProductVoList> cartProductVoLists) {
        this.cartProductVoLists = cartProductVoLists;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
