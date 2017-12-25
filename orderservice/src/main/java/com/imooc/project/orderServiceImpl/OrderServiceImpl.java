package com.imooc.project.orderServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.imooc.project.common.Const;
import com.imooc.project.common.ResponseCode;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_order;
import com.imooc.project.entity.mmall_order_item;
import com.imooc.project.entity.mmall_shipping;
import com.imooc.project.mapper.mmall_orderMapper;
import com.imooc.project.mapper.mmall_order_itemMapper;
import com.imooc.project.mapper.mmall_shippingMapper;
import com.imooc.project.orderService.IorderService;
import com.imooc.project.orderVo.OrderItemVoList;
import com.imooc.project.orderVo.OrderVO;
import com.imooc.project.orderVo.PortalOrderItemVo;
import com.imooc.project.orderVo.ShippingVo;
import com.imooc.project.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/12/22.
 */
@Service
public class OrderServiceImpl implements IorderService{
    @Autowired
    private mmall_orderMapper orderMapper;
    @Autowired
    private mmall_order_itemMapper orderItemMapper;
    @Autowired
    private mmall_shippingMapper shippingMapper;

    //展示订单列表
    @Override
    public ServerResponse showOrderList(Integer userId, Integer pageSize, Integer pageNum) {
        if (userId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        PageHelper.startPage(pageNum, pageSize);
        //封装展示给前台的vo对象
        //根据用户的id查询用户的订单
            List<OrderVO> list=this.convertOrderVO(userId);
            PageInfo info = new PageInfo(list);
            return ServerResponse.createBySuccess(info);
        }
    //封装orderVO对象
    private List<OrderVO> convertOrderVO(Integer userId) {
        List<mmall_order> orderList = orderMapper.selectByUserId(userId);
        //封装VO对象给前台
        List<OrderVO> list = Lists.newArrayList();
        for (mmall_order order : orderList) {
            OrderVO orderVO=this.getOrderVo(userId,order.getOrderNo());
            list.add(orderVO);
        }
        return list;
    }
    private OrderVO getOrderVo(Integer userId,Long orderNo){
       mmall_order order= orderMapper.selectOrder(userId,orderNo);
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        if (order.getPaymentType() == Const.PaymentType.PAYONLINE.getCode()) {
            orderVO.setPaymentTypeDesc(Const.PaymentType.PAYONLINE.getDesc());
        } else {
            orderVO.setPaymentTypeDesc(Const.PaymentType.DAOPAY.getDesc());
        }
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        switch (order.getStatus()) {
            case 0:
                orderVO.setStatusDesc(Const.OrderStatus.CANCELED.getValue());
                break;
            case 10:
                orderVO.setStatusDesc(Const.OrderStatus.NO_PAY.getValue());
                break;
            case 20:
                orderVO.setStatusDesc(Const.OrderStatus.PAID.getValue());
                break;
            case 40:
                orderVO.setStatusDesc(Const.OrderStatus.SHIPPED.getValue());
                break;
            case 50:
                orderVO.setStatusDesc(Const.OrderStatus.ORDER_SUCCESS.getValue());
                break;
            case 60:
                orderVO.setStatusDesc(Const.OrderStatus.ORDER_CLOSE.getValue());
                break;
        }
        orderVO.setPaymentTime(DateUtil.dateToStr(order.getPaymentTime()));
        orderVO.setSendTime(DateUtil.dateToStr(order.getSendTime()));
        orderVO.setEndTime(DateUtil.dateToStr(order.getEndTime()));
        orderVO.setCloseTime(DateUtil.dateToStr(order.getCloseTime()));
        orderVO.setCreateTime(DateUtil.dateToStr(order.getCreateTime()));
        //封装订单明细
        orderVO.setOrderItemVoList(this.orderItem(userId, order.getOrderNo()));
        orderVO.setImageHost("");
        orderVO.setShippingId(order.getShippingId());
        //根据货运id查询收货人信息
        mmall_shipping shipping = shippingMapper.selectShipping(order.getShippingId(), userId);
        orderVO.setReceiverName(shipping.getReceiverName());
        orderVO.setShippingVo(this.getShipping(userId,order.getShippingId()));
        return orderVO;
    }
    //封装收货人信息的VO
    private ShippingVo getShipping(Integer userId,Integer shippingId){
        mmall_shipping shipping = shippingMapper.selectShipping(shippingId, userId);
        ShippingVo vo=new ShippingVo();
        vo.setReceiverName(shipping.getReceiverName());
        vo.setReceiverMobile(shipping.getReceiverMobile());
        vo.setReceiverProvince(shipping.getReceiverProvince());
        vo.setReceiverCity(shipping.getReceiverCity());
        vo.setReceiverDistrict(shipping.getReceiverDistrict());
        vo.setReceiverAddress(shipping.getReceiverAddress());
        vo.setReceiverZip(shipping.getReceiverZip());
        return vo;

    }
//封装明细的vo对象
    private List<OrderItemVoList> orderItem(Integer userId, Long orderNo) {
       List<mmall_order_item> itemList=orderItemMapper.selectItem(userId,orderNo);
       List<OrderItemVoList> lists=Lists.newArrayList();
        for (mmall_order_item item: itemList) {
            OrderItemVoList itemVoList=new OrderItemVoList();
            itemVoList.setOrderNo(orderNo);
            itemVoList.setProductId(item.getProductId());
            itemVoList.setCurrentUnitPrice(item.getCurrentUnitPrice());
            itemVoList.setProductImage(item.getProductImage());
            itemVoList.setProductName(item.getProductName());
            itemVoList.setQuantity(item.getQuantity());
            itemVoList.setTotalPrice(item.getTotalPrice());
            itemVoList.setCreateTime(DateUtil.dateToStr(item.getCreateTime()));
            lists.add(itemVoList);
        }
        return lists;
    }


//根据订单号查询订单
    @Override
    public ServerResponse searchOrder(Integer userId, Integer pageSize, Integer pageNum, Long orderNo) {
        if (userId==null && orderNo==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        PageHelper.startPage(pageNum,pageSize);
        //根据订单号查询订单
        mmall_order order=orderMapper.selectOrder(userId,orderNo);
        if (order==null){
            return  ServerResponse.createByErrorMessage("无此订单");
        }
       OrderVO orderVO=this.getOrderVo(userId,orderNo);
        List<OrderVO> list=Lists.newArrayList();
        list.add(orderVO);
        PageInfo info=new PageInfo(list);
        return ServerResponse.createBySuccess(info);
    }
//展示订单详情
    @Override
    public ServerResponse showDetail(Integer userId, Integer pageSize, Integer pageNum, Long orderNo) {
        if (userId==null && orderNo==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<OrderVO> list=Lists.newArrayList();
        OrderVO orderVO=this.getOrderVo(userId,orderNo);
        list.add(orderVO);
        PageInfo info=new PageInfo(list);
        return ServerResponse.createBySuccess(info);
    }

    @Override
    public ServerResponse cancelOrder(Integer userId, Long orderNo) {
        if (userId==null && orderNo==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        mmall_order order=orderMapper.selectOrder(userId,orderNo);
        if (order==null){
            return  ServerResponse.createByErrorMessage("无此订单");
        }
       int i=orderMapper.deleteByPrimaryKey(order.getId());
        if (i==0){
            return  ServerResponse.createByErrorMessage("删除失败");
        }

        return ServerResponse.createBySuccessMessage("删除成功");
    }
//获取订单的商品信息，即订单明细
    @Override
    public ServerResponse getOrderProduct(Integer userId, Long orderNo) {
        if (userId==null && orderNo==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
     List<OrderItemVoList> list=this.orderItem(userId,orderNo);
        //封装portalvo
        List<PortalOrderItemVo> portalList=Lists.newArrayList();
        for (OrderItemVoList  orderItem:list) {
            PortalOrderItemVo portalVO=new PortalOrderItemVo();
            portalVO.setPortalItemList(list);
            portalVO.setImageHost("'");
            portalVO.setProductTotalPrice(orderItem.getTotalPrice());
            portalList.add(portalVO);
        }
        return ServerResponse.createBySuccess(portalList);
    }

}
