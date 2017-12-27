package com.imooc.project.orderService;

import com.imooc.project.common.ServerResponse;

/**
 * Created by Administrator on 2017/12/22.
 */
public interface IorderService {
    ServerResponse showOrderList(Integer userId, Integer pageSize, Integer pageNum);

    ServerResponse searchOrder(Integer userId, Integer pageSize, Integer pageNum, Long orderNo);

    ServerResponse showDetail(Integer userId, Integer pageSize, Integer pageNum, Long orderNo);

    ServerResponse cancelOrder(Integer userId, Long orderNo);

    ServerResponse getOrderProduct(Integer userId, Long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);
}
