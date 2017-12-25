package com.imooc.project.orderVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */
public class PortalOrderItemVo {
   private  List<OrderItemVoList> portalItemList;
   private String  imageHost;
   private BigDecimal productTotalPrice;

    public List<OrderItemVoList> getPortalItemList() {
        return portalItemList;
    }

    public void setPortalItemList(List<OrderItemVoList> portalItemList) {
        this.portalItemList = portalItemList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}
