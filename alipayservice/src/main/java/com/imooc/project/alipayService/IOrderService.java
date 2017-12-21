package com.imooc.project.alipayService;

import com.imooc.project.common.ServerResponse;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/18.
 */
public interface IOrderService {
    ServerResponse paydo(Integer userId, Long orderNo,String path);

    ServerResponse payStatus(Integer userId, Long orderNo);
    ServerResponse aliCallback(Map<String,String> params);
}
