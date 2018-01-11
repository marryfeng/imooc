package com.imooc.project.shippingService;

import com.imooc.project.common.ServerResponse;

/**
 * Created by Administrator on 2018/1/11.
 */
public interface AddService {
    ServerResponse showAddList(Integer pageNum, Integer pageSize);
}
