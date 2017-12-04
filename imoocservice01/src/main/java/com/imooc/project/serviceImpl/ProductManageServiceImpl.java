package com.imooc.project.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.imooc.project.common.Const;
import com.imooc.project.common.ResponseCode;
import com.imooc.project.common.ServerResponse;
import com.imooc.project.entity.mmall_category;
import com.imooc.project.entity.mmall_product;
import com.imooc.project.mapper.mmall_categoryMapper;
import com.imooc.project.mapper.mmall_productMapper;
import com.imooc.project.service.CategoryService;
import com.imooc.project.service.ProductManageService;
import com.imooc.project.util.DateUtil;
import com.imooc.project.util.PropertiesUtil;
import com.imooc.project.vo.ProductDetail;
import com.imooc.project.vo.ProductListVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */
@Service
public class ProductManageServiceImpl implements ProductManageService {
    @Autowired
    private mmall_productMapper productMapper;
    @Autowired
    private mmall_categoryMapper categoryMapper;
    //这里调用本身的接口，属于平级调用
    @Autowired
    private CategoryService categoryService;

    @Override
    public ServerResponse<String> updateOrAdd(mmall_product product) {
        //根据商品id来判断是用来更新商品还是添加商品，若id为空，则是新增商品。否则是更新商品
        if (product != null) {
            if (product.getId() != null) {
                //则是更新商品
                int i = productMapper.updateByPrimaryKey(product);
                if (i != 0) {
                    return ServerResponse.createBySuccessMessage("更新商品成功！");
                }
                return ServerResponse.createByErrorMessage("更新商品失败");
            } else {
                //若id为空，则是插入新商品
                int i = productMapper.insert(product);
                if (i != 0) {
                    return ServerResponse.createBySuccessMessage("插入商品成功！");
                }
                return ServerResponse.createByErrorMessage("插入商品失败");
            }
        }
        return ServerResponse.createByErrorMessage("商品不能为空！");
    }

    @Override
    public ServerResponse<String> setProductStatus(Integer productId, Integer status) {
        if (productId != null) {
            mmall_product product = productMapper.selectByPrimaryKey(productId);
            if (product != null) {
                product.setStatus(status);
                int i = productMapper.updateByPrimaryKeySelective(product);
                if (i != 0) {
                    return ServerResponse.createBySuccessMessage("更新商品上下架成功！");
                }
                return ServerResponse.createByErrorMessage("更新商品上下架失败");
            }
            return ServerResponse.createByErrorMessage("无此商品！");
        }
        return ServerResponse.createByErrorMessage("商品id不能为空！");
    }

    @Override
    public ServerResponse getProductList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<mmall_product> productList = productMapper.selectProductList();
        if (CollectionUtils.isEmpty(productList)) {
            return ServerResponse.createByErrorMessage("无商品展示");
        }
        List<ProductListVo> list = Lists.newArrayList();
        for (mmall_product productItem : productList) {
            ProductListVo productVo = productConvertVo(productItem);
            list.add(productVo);
        }
        PageInfo info = new PageInfo(list);
        return ServerResponse.createBySuccess(info);
    }

    @Override
    public ServerResponse searchProduct(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        //如果都为空，则显示全部的商品信息，所以这里是可以允许两个参数为空的
        if (productName != null) {
            // 模糊查询的拼接：错误方式 productName = new StringBuilder("%").append("productName").append("%").toString();
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<mmall_product> productList = productMapper.searchProduct(productId, productName);
        List list = Lists.newArrayList();
        PageHelper.startPage(pageNum, pageSize);
        if (!CollectionUtils.isEmpty(productList)) {
            for (mmall_product product : productList) {
                ProductListVo productVo = this.productConvertVo(product);
                list.add(productVo);
            }
            PageInfo info = new PageInfo(list);
            return ServerResponse.createBySuccess(info);
        }
        return ServerResponse.createByErrorMessage("无此商品信息");
    }

    //将对象转为vo自己定义的对象
    public ProductListVo productConvertVo(mmall_product product) {
        ProductListVo productVo = new ProductListVo();
        productVo.setId(product.getId());
        productVo.setName(product.getName());
        productVo.setCategoryId(product.getCategoryId());
        productVo.setMainImage(product.getMainImage());
        productVo.setSubtitle(product.getSubtitle());
        productVo.setPrice(product.getPrice());
        productVo.setStatus(product.getStatus());
        return productVo;
    }

    @Override
    public ServerResponse getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorMessage("商品id不能为空");
        }
        mmall_product product = productMapper.selectByPrimaryKey(productId);
        ProductDetail productDetail = this.productDetaiConvertVo(product);
        if (product == null) {
            return ServerResponse.createByErrorMessage("查询无此商品");
        }
        return ServerResponse.createBySuccess(productDetail);
    }


    //商品详情的转为vo类显示给前端页面
    public ProductDetail productDetaiConvertVo(mmall_product product) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(product.getId());
        productDetail.setCategoryId(product.getCategoryId());
        productDetail.setName(product.getName());
        productDetail.setSubImages(product.getSubImages());
        productDetail.setMainImage(product.getMainImage());
        productDetail.setSubtitle(product.getSubtitle());
        productDetail.setPrice(product.getPrice());
        productDetail.setStatus(product.getStatus());
        productDetail.setDetail(product.getDetail());
        productDetail.setStock(product.getStock());
        productDetail.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
        productDetail.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));
        productDetail.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        mmall_category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category.getParentId() == null) {
            productDetail.setParentCategoryId(0);
        }
        productDetail.setParentCategoryId(category.getParentId());
        return productDetail;
    }

    //前端显示商品列表，并按照一定的顺序排序
    @Override
    public ServerResponse<PageInfo> getPortalProductList(Integer categoryId, String keyword, String orderBy, Integer pageNum, Integer pageSize) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        //这里需要根据商品id来判断这个类别是否存在，如果分类不存在，则返回给前台一个空即可
        if (categoryId != null) {
            mmall_category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //如果分类为空，则返回该类别为空的结果集，不报错
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> list = Lists.newArrayList();
                PageInfo info = new PageInfo(list);
                return ServerResponse.createBySuccess(info);
            }
            //商品展示的时候，当我们在搜索某一类商品的时候，它会有很多子类，比如手机类别，有华为型号的，华为型号下面又有很多子类，所以递归函数来调用

            categoryIdList = categoryService.getDeepCategory(category.getId()).getData();
        }
        //接下来判断关键字是否为空
        if (keyword != null) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        //排序处理
        PageHelper.startPage(pageNum, pageSize);
       /* if (StringUtils.isNotBlank(orderBy)){
            //分页的排序
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                //进行分割
               String[] orderArray=orderBy.split("_");
               //这里为什么没有排序的方法
               PageHelper.orderBy(orderArray[0]+" "+orderArray[1]);
            }
        }*/
        List<mmall_product> productList = productMapper.selectProtalProduct(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(productList)) {
            for (mmall_product product : productList) {
                ProductListVo productListVo = this.productConvertVo(product);
                productListVoList.add(productListVo);
            }
        }
        PageInfo info = new PageInfo(productListVoList);
        return ServerResponse.createBySuccess(info);
    }


//前台展示商品详情
    @Override
    public ServerResponse getPortalProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorMessage("商品id不能为空");
        }
        mmall_product product = productMapper.selectByPrimaryKey(productId);
        ProductDetail productDetail= this.productDetaiConvertVo(product);
        if (product == null) {
            return ServerResponse.createByErrorMessage("查询无此商品");
        }
        if (product.getStatus()!=Const.ProductStatus.ON_SALE.getCode()){
            return ServerResponse.createByErrorMessage("商品已下架");
        }
        return ServerResponse.createBySuccess(productDetail);
    }


}
