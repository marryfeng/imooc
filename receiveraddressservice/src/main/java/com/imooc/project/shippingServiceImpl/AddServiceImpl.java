package com.imooc.project.shippingServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_shipping;
import com.imooc.project.mapper.mmall_shippingMapper;
import com.imooc.project.shippingService.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */
@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private mmall_shippingMapper shippingMapper;
    @Override
    public ServerResponse showAddList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<mmall_shipping> list=shippingMapper.selectList();
        if (CollectionUtils.isEmpty(list)){
            return ServerResponse.createByErrorMessage("无收货人地址，请添加");
        }
        PageInfo info=new PageInfo(list);
        return ServerResponse.createBySuccess(info);
    }
}
