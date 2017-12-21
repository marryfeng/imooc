package com.imooc.project.alipayController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.imooc.project.alipayService.IOrderService;
import com.imooc.project.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝当面付功能模块
 */
@RestController
@Api(value = "订单支付功能模块编码",tags = {"imoocProject项目订单支付功能模块编码"})
public class OrderController {
    private static final Logger logger= LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private IOrderService orderService;


    @RequestMapping(value = "pay.do",method = RequestMethod.GET)
    @ApiOperation("支付接口，获取生成的二维码链接")
    @ApiImplicitParam(name = "orderNo",value = "订单号",dataType = "Long",paramType = "query")
    public ServerResponse paydo(Long orderNo,HttpServletRequest request){
        Integer userId=21;
        //这里是我们在项目中暂时生成的文件夹，名称为upload
        String path=request.getSession().getServletContext().getRealPath("upload");
        ServerResponse response=orderService.paydo(userId,orderNo,path);
        return response;
    }
    @RequestMapping(value = "query_order_pay_status.do",method = RequestMethod.GET)
    @ApiOperation("查看订单支付状态")
    @ApiImplicitParam(name = "orderNo",value = "订单号",dataType = "Long",paramType = "query")
    public ServerResponse<Boolean> queryOrderStatus(Long orderNo){
        Integer userId=21;
        ServerResponse response=orderService.payStatus(userId,orderNo);
		if(response.isSuccess()){
			return ServerResponse.createBySuccess(true);
		}
		return ServerResponse.createBySuccess(false);
    }

    @RequestMapping(value = "alipay_callback.do",method = RequestMethod.GET)
    @ApiOperation("支付宝回调")
    public Object callback(HttpServletRequest request){
        /*当收银台调用预下单请求API生成二维码展示给用户后，用户通过手机扫描二维码进行支付，支付宝会将该笔订单的变更信息，沿着商户调用预下单请求时所传入的通知地址主动推送给商户。即我们称为支付宝回调,回调是为了商户收到用户的支付信息，然后更新库存，更新订单状态*/
     Map<String ,String[]> requestParams=request.getParameterMap();
     Map<String ,String> params=new HashMap<>();
     /*对Map进行遍历，获取key和value.遍历map集合的三种方式：*
     (1)map.keySet()获取key，然后根据key遍历value.也可以使用迭代器来遍历。
     (2)通过Map.entrySet使用iterator遍历key和value（1、2种方式都可以通过迭代器来进行）
     (3)第三种：通过Map.entrySet遍历key和value(for (Map.Entry<Integer, String> entry : map.entrySet()), entry.getKey(),entry.getValue())
      */
     String valueStr="";
     for(Iterator<String> iter=requestParams.keySet().iterator();iter.hasNext();){
         String name=iter.next();
         String[] values=requestParams.get(name);
         for(int i=0;i<=values.length;i++){
             //这里是为了在构成的valueStr中间有逗号隔开，且最后一个末尾没有逗号
             valueStr=(i==values.length-1? valueStr+values[i]:valueStr+values[i]+",");
         }
         params.put(name,valueStr);
     }
	logger.info("支付宝回调，sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
    logger.info("这时候从支付宝获取到了一些信息，收到支付宝异步回调：");
  //这个时候回调已经拿到了即支付宝回传过来的信息以及到达商户这边，我们需要验证，此时要验证回调的正确性是否是支付宝发的，并且避免重复通知
/*
第一步： 在通知返回参数列表中，除去sign、sign_type两个参数外，凡是通知返回回来的参数皆是待验签的参数。
第二步： 将剩下参数进行url_decode, 然后进行字典排序，组成字符串，得到待签名字符串：
第三步： 将签名参数（sign）使用base64解码为字节码串。(有的支付宝的SDK已经封装了)
第四步： 使用RSA的验签方法，通过签名字符串、签名参数（经过base64解码）及支付宝公钥验证签名。
第五步：需要严格按照如下描述校验通知数据的正确性。
* */
    params.remove("sign_type");
        try {
            Boolean alipayRsacheckv2=AlipaySignature.rsaCheckV2(params, Configs.getPublicKey(),"utf-8",Configs.getSignType());
            if (!alipayRsacheckv2){
                //这里取个反，当返回false的时候，直接返回
                return ServerResponse.createByErrorMessage("非法请求验证不通过");
            }
            //下面开始写业务逻辑：处理alipay回调的逻辑，这里判断订单是否已经支付了，更新订单状态这些逻辑



        } catch (AlipayApiException e) {
            logger.error("支付宝验证异常",e);
            e.printStackTrace();
        }
        //todo 验证各种数据
      return  orderService.aliCallback(params);

    }

}
