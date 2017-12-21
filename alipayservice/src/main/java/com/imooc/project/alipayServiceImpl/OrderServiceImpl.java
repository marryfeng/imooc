package com.imooc.project.alipayServiceImpl;


import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.imooc.project.alipayService.IOrderService;
import com.imooc.project.common.Const;
import com.imooc.project.common.ResponseCode;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_order;
import com.imooc.project.entity.mmall_order_item;
import com.imooc.project.entity.mmall_pay_info;
import com.imooc.project.mapper.mmall_orderMapper;
import com.imooc.project.mapper.mmall_order_itemMapper;
import com.imooc.project.mapper.mmall_pay_infoMapper;
import com.imooc.project.util.BigDecimalUtil;
import com.imooc.project.util.DateUtil;
import com.imooc.project.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18.
 */
@Service
public class OrderServiceImpl implements IOrderService {

    public static Logger log= LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private mmall_orderMapper orderMapper;
    @Autowired
    private mmall_order_itemMapper  itemMapper;

    @Autowired
    private mmall_pay_infoMapper payInfoMapper;





    @Override
    public ServerResponse paydo(Integer userId, Long orderNo,String path) {

        Map<String ,String> model=new HashMap<>();

        //调用支付宝当面付的接口，返回需要支付的二维码信息
        if (userId==null && orderNo==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
       //查看该用户的订单是否存在，如果存在则执行当面付
        log.info("查询该用户的订单是否存在");
        mmall_order order=orderMapper.selectOrder(userId,orderNo);
        if (order==null){
            return ServerResponse.createByErrorMessage("用户订单不存在,系统错误");
        }
        model.put("orderNo",order.getOrderNo().toString());
        log.info("查询该用户的订单存在，接入支付宝当面付功能");
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("慕课商城扫码支付").toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "2088102170124280";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单号为").append(outTradeNo).append("商品金额共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "001";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "storeId";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";


        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        log.info("获取该订单的订单明细");
        List<mmall_order_item> orderItemList=itemMapper.selectItem(userId,orderNo);
        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        for (mmall_order_item item: orderItemList) {
            GoodsDetail detail= GoodsDetail.newInstance(item.getProductId().toString(),item.getProductName(), BigDecimalUtil.mul(item.getCurrentUnitPrice().doubleValue(),new Double(100)).longValue(),item.getQuantity());
            goodsDetailList.add(detail);
        }
        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                log.info("打印出二维码，显示到前台页面");
                //这一步是如果文件目录不存在，则创建一个文件
                File foder=new File(path);
                if (!foder.exists()){
                    foder.setWritable(true);
                    foder.mkdir();
                }
                //二维码生成路径，即二维码生成后保存在哪里
                String qrPath = String.format(path+"/qr-%s.png", response.getOutTradeNo());
                String qrFileName=String.format("qr-%s.png",response.getOutTradeNo());

                //这里主要获取到二维码的图片，拿到二维码,这里使用的是google的zxing工具生成二维码
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File  targetFile=new File(path,qrFileName);
                //将二维码上传到服务器，这里就不用了，然后拼接url
                String url=qrPath;

                model.put("quPath",url);
                log.info("filePath:" + qrPath);
                //ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                return ServerResponse.createBySuccess(model);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }


    }
    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
  //测试订单的支付状态
    @Override
    public ServerResponse payStatus(Integer userId, Long orderNo) {
        if (userId==null && orderNo==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //查看该用户的订单是否存在，如果存在则执行当面付
        log.info("查询该用户的订单是否存在");
        mmall_order order=orderMapper.selectOrder(userId,orderNo);
        if (order==null){
            return ServerResponse.createByErrorMessage("用户订单不存在,系统错误");
        }
		//如果订单状态的值比付款的大，则都认为付款成功，直接返回成功
		if(Const.OrderStatus.PAID.getCode()<=order.getStatus()){
			return ServerResponse.createBySuccess();
		}else{
			return ServerResponse.createByError();
		}
		
		
	}
//alipay的回调，查询下订单的状态，是否已经支付，更新下支付状态等等，也就是说获取到支付宝的一些返回信息，商户需要修改这些信息
    @Override
    public ServerResponse aliCallback(Map<String, String> params) {
        //得到支付宝的返回的信息，修改订单的状态，封装支付信息插入到数据库表中
        String tradeNo=params.get("trade_no");
        Long orderNo=Long.parseLong(params.get("out_trade_no"));
        String tradeStatus=params.get("trade_status");
        mmall_order order=orderMapper.selectOrderNo(orderNo);
        if (order==null){
            return ServerResponse.createByErrorMessage("非此商城订单");
        }
        if (order.getStatus()>=Const.OrderStatus.PAID.getCode()){
            //如果此时订单的状态大于已付款，则表示支付宝重复调用
            return ServerResponse.createBySuccess("支付宝重复调用");
        }
        //如果状态是付款成功，则修改状态为付款成功
        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            order.setCreateTime(DateUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatus.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        //封装支付信息
        mmall_pay_info payInfo=new mmall_pay_info();
        payInfo.setOrderNo(orderNo);
        payInfo.setUserId(order.getUserId());
        payInfo.setPlatformStatus(tradeStatus);
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfoMapper.insert(payInfo);
        return ServerResponse.createBySuccess();
    }

}
