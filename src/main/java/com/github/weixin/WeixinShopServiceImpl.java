package com.github.weixin;

import com.github.service.WeixinOpenService;
import cn.hutool.core.date.DatePattern;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-04 11:30
 */
@Slf4j
@Service
public class WeixinShopServiceImpl extends WxOpenServiceImpl implements WeixinShopService {

    public WeixinShopServiceImpl(WeixinOpenService weixinOpenService) {
        setWxOpenConfigStorage(weixinOpenService.getWxOpenConfigStorage());
    }

    @Override
    public String getCategory(Integer fatherCategoryId) {
        if (fatherCategoryId == null) {
            fatherCategoryId = 0;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("f_cat_id", fatherCategoryId);
        try {
            return post(String.format(API_PRODUCT_CATEGORY_GET_URL, getWxOpenComponentService()
                    .getAuthorizerAccessToken(appId, false)), jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBrand() {
        try {
            return post(String.format(API_PRODUCT_BRAND_GET_URL, getWxOpenComponentService()
                    .getAuthorizerAccessToken(appId, false)), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFreightTemplate() {
        return null;
    }

    @Override
    public String getShopCategory() {
        return null;
    }

    @Override
    public String getOrderList(LocalDateTime startCreateTime, LocalDateTime endCreateTime,
                               LocalDateTime startUpdateTime, LocalDateTime endUpdateTime,
                               Integer status, Integer page, Integer pageSize) {
        JsonObject jsonObject = new JsonObject();

        if (status == null) {
            status = 10;
        }

        if (page == null || 1 > page) {
            page = 1;
        }

        if (pageSize == null || 10000 < pageSize) {
            pageSize = 10;
        }

        if (startCreateTime != null && endCreateTime != null) {
            jsonObject.addProperty("start_create_time", startCreateTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            jsonObject.addProperty("end_create_time", endCreateTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        } else if (startUpdateTime != null && endUpdateTime != null) {
            jsonObject.addProperty("start_update_time", startUpdateTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            jsonObject.addProperty("end_update_time", endUpdateTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        } else {
            throw new IllegalArgumentException("start_create_time&end_create_time 和 start_update_time&end_update_time 至少填一对参数");
        }

        jsonObject.addProperty("page_size", pageSize);
        jsonObject.addProperty("page", page);
        jsonObject.addProperty("status", status);

        try {
            return post(String.format(API_PRODUCT_ORDER_GET_LIST_URL, getWxOpenComponentService()
                    .getAuthorizerAccessToken(appId, false)), jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean sendKefuMessage(WxMpKefuMessage message) throws WxErrorException {
        return getWxOpenComponentService().getWxMpServiceByAppid(appId).getKefuService().sendKefuMessage(message);
    }
}
