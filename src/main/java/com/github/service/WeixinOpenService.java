package com.github.service;

import com.github.common.GlobalConstants;
import com.github.weixin.WeixinOpenConfigStorage;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.open.api.WxOpenConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-03 20:51
 */
@Slf4j
@Service
public class WeixinOpenService extends WxOpenServiceImpl {

    private WxOpenMessageRouter wxOpenMessageRouter;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        setWxOpenConfigStorage(getWxOpenConfigStorage());
        wxOpenMessageRouter = new WxOpenMessageRouter(this);
        wxOpenMessageRouter.rule().handler((wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
            log.info("\n接收到 {} 公众号请求消息，内容：{}", wxMpService.getWxMpConfigStorage().getAppId(), wxMpXmlMessage);
            return null;
        }).next();
    }

    public WxOpenConfigStorage getWxOpenConfigStorage() {
        WeixinOpenConfigStorage weixinOpenConfigStorage = new WeixinOpenConfigStorage(stringRedisTemplate);
        weixinOpenConfigStorage.setComponentAppId(GlobalConstants.componentAppId);
        weixinOpenConfigStorage.setComponentAppSecret(GlobalConstants.componentAppSecret);
        weixinOpenConfigStorage.setComponentToken(GlobalConstants.componentToken);
        weixinOpenConfigStorage.setComponentAesKey(GlobalConstants.componentAesKey);
        return weixinOpenConfigStorage;
    }

    public WxOpenMessageRouter getWxOpenMessageRouter() {
        return wxOpenMessageRouter;
    }
}
