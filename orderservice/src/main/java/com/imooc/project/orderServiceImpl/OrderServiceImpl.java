package com.imooc.project.orderServiceImpl;

import com.imooc.project.mapper.mmall_orderMapper;
import com.imooc.project.orderService.IorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/22.
 */
@Service
public class OrderServiceImpl implements IorderService{
    @Autowired
    private mmall_orderMapper orderMapper;
}
