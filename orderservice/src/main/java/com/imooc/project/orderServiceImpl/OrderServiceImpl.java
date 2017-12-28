package com.imooc.project.orderServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.imooc.project.common.Const;
import com.imooc.project.common.ResponseCode;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.*;
import com.imooc.project.mapper.*;
import com.imooc.project.orderService.IorderService;
import com.imooc.project.orderVo.OrderItemVoList;
import com.imooc.project.orderVo.OrderVO;
import com.imooc.project.orderVo.PortalOrderItemVo;
import com.imooc.project.orderVo.ShippingVo;
import com.imooc.project.util.BigDecimalUtil;
import com.imooc.project.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

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

    @Autowired
    private mmall_cartMapper cartMapper;
    @Autowired
    private mmall_productMapper productMapper;

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
      //注意这里的错误：订单取消，不是订单的删除，是将该订单的状态更新给已取消
        //这里对接支付宝的退款，当退款成功后，才可以取消订单
        if (order.getStatus()!=Const.OrderStatus.NO_PAY.getCode()){
            return  ServerResponse.createByErrorMessage("已付款，无法取消订单");
        }
        mmall_order updateOrder=new mmall_order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatus.CANCELED.getCode());
        int i=orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (i==0){
            return  ServerResponse.createByErrorMessage("订单取消失败");
        }
        return  ServerResponse.createBySuccessMessage("订单取消成功");
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
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        if (userId==null && shippingId==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        if (shippingId==null){
            return ServerResponse.createByErrorMessage("请填写收货人信息");
        }
        //(1)从购物车中获取已勾选的商品，(从购物车勾选的商品中获取商品信息)然后计算商品的总价，然后创建订单成功，然后商品库存更新，购物车的该商品删除。封装订单信息给前台
         List<mmall_cart> cartCheckedList=cartMapper.selectCheckedProduct(userId);
        //计算总价，这里需要查询订单的明细有哪些，封装获取订单明细的方法
        ServerResponse response=this.getOrderItemList(userId,cartCheckedList);
        if (!response.isSuccess()){
            return response;
        }
        //计算总价
        List<mmall_order_item> list=(List<mmall_order_item>)response.getData();
        BigDecimal totalPrice=this.calculateTotalPrice(list);
        //生成订单,这里注意订单号的生成规则：
        mmall_order order=this.assembleOrder(userId,shippingId,totalPrice);
        if (order==null){
            return ServerResponse.createByErrorMessage("生成订单错误");
        }
        //这里订单号生成了之后，订单明细里面还没有赋值，将订单号赋给每个订单明细
        for (mmall_order_item orderItem: list) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        //关键一步：批量将订单插入：将订单明细批量插入
        orderItemMapper.batchInsert(list);
         //生成订单成功，减少产品的库存
        this.updateStock(list);

        //清空下购物车
        this.deleteProduct(cartCheckedList);
        //封装返回前台的数据，封装OrderVo，orderItemVO
        OrderItemVoList orderItem=new OrderItemVoList();
        //这里不写了
         return null;

    }
//生成订单
    private mmall_order assembleOrder(Integer userId, Integer shippingId,BigDecimal totalPrice) {
        mmall_order order=new mmall_order();
        long orderNo=this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatus.NO_PAY.getCode());
        order.setUserId(userId);
        order.setPaymentType(Const.PaymentType.PAYONLINE.getCode());
        order.setPayment(totalPrice);
        order.setShippingId(shippingId);
       int i=orderMapper.insert(order);
        if (i == 0) {
            return null;
        }
        return  order;
    }
    //生成订单号:分布式环境一般用Redis的incre命令
    private long generateOrderNo(){
        long currentTime=System.currentTimeMillis();
        return currentTime+new Random().nextInt(100);
    }
//计算总价
    private BigDecimal calculateTotalPrice(List<mmall_order_item> list) {
        BigDecimal totalPrice=new BigDecimal("0");
        if (CollectionUtils.isEmpty(list)){
            return totalPrice;
        }
        for (mmall_order_item orderItem: list) {
            totalPrice=BigDecimalUtil.add(totalPrice.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return totalPrice;
    }

    //获取订单明细
    private ServerResponse getOrderItemList(Integer userId, List<mmall_cart> cartCheckedList) {
        if (CollectionUtils.isEmpty(cartCheckedList)){
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        List<mmall_order_item> list=Lists.newArrayList();
        for (mmall_cart cartItem:cartCheckedList) {
           mmall_product product= productMapper.selectByPrimaryKey(cartItem.getProductId());
           //这里有两个校验，校验产品的库存和是否是在售状态
            //判断产品是否是在售状态
            if (Const.ProductStatus.ON_SALE.getCode()==product.getStatus()){
                //判断产品的库存
                if (cartItem.getQuantity()<=product.getStock()){
                    //封装产品的明细
                    mmall_order_item orderItem=new mmall_order_item();
                    orderItem.setUserId(userId);
                    orderItem.setProductId(product.getId());
                    orderItem.setProductName(product.getName());
                    orderItem.setCurrentUnitPrice(product.getPrice());
                    orderItem.setProductImage(product.getMainImage());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartItem.getQuantity().doubleValue()));
                    list.add(orderItem);
                }else{
                    return ServerResponse.createByErrorMessage("库存不足");
                }
            }else{
                return  ServerResponse.createByErrorMessage("商品非在线销售状态，请重新购买！");
            }
        }
        return ServerResponse.createBySuccess(list);
    }

    private void updateStock(List<mmall_order_item> list) {
        for (mmall_order_item orderItem:list) {
            mmall_product product=productMapper.selectByPrimaryKey(orderItem.getProductId());
             product.setStock(product.getStock()-orderItem.getQuantity());
             productMapper.updateByPrimaryKeySelective(product);
        }
    }

    //从购物车删除该商品信息
    private void deleteProduct(List<mmall_cart> list) {
        for (mmall_cart cart:list) {
           cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

}
