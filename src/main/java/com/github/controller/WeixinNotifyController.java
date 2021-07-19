package com.github.controller;

import com.github.service.WeixinOpenService;
import com.github.weixin.WeixinOpenConfigStorage;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.redis.WxRedisOps;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-03 21:16
 */
@Slf4j
@RestController
@RequestMapping("/wxThirdPlatform")
public class WeixinNotifyController extends BaseController {

    @Resource
    private WeixinOpenService weixinOpenService;

    /**
     * 接收验证票据（component_verify_ticket）
     * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/api/component_verify_ticket.html
     *
     * @return
     */
    @RequestMapping("verifyTicket")
    public String verifyTicket(@RequestBody(required = false) String requestBody, @RequestParam("timestamp") String timestamp,
                               @RequestParam("nonce") String nonce, @RequestParam("signature") String signature,
                               @RequestParam(name = "encrypt_type", required = false) String encType,
                               @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        log.info("\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, encType, msgSignature, timestamp, nonce, requestBody);

        if (!StringUtils.equalsIgnoreCase("aes", encType)
                || !weixinOpenService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        // aes加密的消息
        WxOpenXmlMessage inMessage = WxOpenXmlMessage.fromEncryptedXml(requestBody,
                weixinOpenService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        try {
            String out = weixinOpenService.getWxOpenComponentService().route(inMessage);
            log.debug("\n组装回复信息：{}", out);
        } catch (WxErrorException e) {
            log.error("receive_ticket", e);
        }


        return "success";
    }

    @RequestMapping("authCode")
    @ResponseBody
    public WxOpenQueryAuthResult authCode(@RequestBody(required = false) String requestBody,
                                          @RequestParam("auth_code") String authCode,
                                          @RequestParam("expires_in") int expiresIn) {
        log.info("\n接收微信请求：[authCode=[{}], expires_in=[{}],requestBody=[\n{}\n] ", authCode, expiresIn, requestBody);

        try {
            WxOpenQueryAuthResult queryAuthResult = weixinOpenService.getWxOpenComponentService().getQueryAuth(authCode);

            WeixinOpenConfigStorage wxOpenConfigStorage = (WeixinOpenConfigStorage) weixinOpenService.getWxOpenComponentService().getWxOpenConfigStorage();
            WxRedisOps redisOps = wxOpenConfigStorage.getRedisOps();

            redisOps.setValue("wechat_authorization_code", authCode, expiresIn - 200, TimeUnit.SECONDS);
            log.info("getQueryAuth = {}", queryAuthResult);
            return queryAuthResult;
        } catch (WxErrorException e) {
            log.error("gotoPreAuthUrl", e);
            throw new RuntimeException(e);
        }
    }

}
