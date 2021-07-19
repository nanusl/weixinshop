package com.github.weixin;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;

import java.time.LocalDateTime;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-04 11:19
 */
public interface WeixinShopService {

    String appId = "wxe485ee32c5530e33";


    //region 接入商品前必须接口

    /**
     * 获取类目详情
     * <p>
     * https://developers.weixin.qq.com/doc/ministore/minishopopencomponent/API/cat/get_cat_list.html
     */
    String API_PRODUCT_CATEGORY_GET_URL = "https://api.weixin.qq.com/product/category/get?access_token=%s";


    /**
     * 获取品牌列表
     * <p>
     * https://developers.weixin.qq.com/doc/ministore/minishopopencomponent/API/cat/get_brand.html
     */
    String API_PRODUCT_BRAND_GET_URL = "https://api.weixin.qq.com/product/brand/get?access_token=%s";

    /**
     * 获取运费模板
     * <p>
     * https://developers.weixin.qq.com/doc/ministore/minishopopencomponent/API/cat/get_freight_template.html
     */
    String API_PRODUCT_DELIVERY_GET_FREIGHT_TEMPLATE_URL = "https://api.weixin.qq.com/product/delivery/get_freight_template?access_token=%s";

    /**
     * 获取店铺的商品分类
     * <p>
     * https://developers.weixin.qq.com/doc/ministore/minishopopencomponent/API/store/get_shopcat.html
     */
    String API_PRODUCT_STORE_GET_SHOPCAT_URL = "https://api.weixin.qq.com/product/store/get_shopcat?access_token=%s";

    //endregion


    //region 订单接口

    /**
     * 获取订单列表
     * <p>
     * https://developers.weixin.qq.com/doc/ministore/minishopopencomponent/API/order/get_order_list.html
     */
    String API_PRODUCT_ORDER_GET_LIST_URL = "https://api.weixin.qq.com/product/order/get_list?access_token=%s";

    //endregion


    String getCategory(Integer fatherCategoryId);

    String getBrand();

    String getFreightTemplate();

    String getShopCategory();

    String getOrderList(LocalDateTime startCreateTime, LocalDateTime endCreateTime,
                        LocalDateTime startUpdateTime, LocalDateTime endUpdateTime,
                        Integer status, Integer page, Integer pageSize);


    boolean sendKefuMessage(WxMpKefuMessage message) throws WxErrorException;
}
