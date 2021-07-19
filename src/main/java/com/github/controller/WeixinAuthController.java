package com.github.controller;

import com.github.service.WeixinOpenService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.bean.result.WxOpenAuthorizerInfoResult;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-04 10:46
 */
@Slf4j
@Controller
@RequestMapping("/auth")
public class WeixinAuthController extends BaseController {

    @Resource
    private WeixinOpenService weixinOpenService;

    /**
     * 第一步
     * 令牌（component_access_token）是第三方平台接口的调用凭据
     * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/api/component_access_token.html
     */
    @GetMapping("componentAccessToken")
    @ResponseBody
    @CachePut
    public String componentAccessToken() {
        try {
            weixinOpenService.getWxOpenComponentService().getComponentAccessToken(false);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @GetMapping("authUrl")
    @ResponseBody
    public String gotoPreAuthUrlShow() {
        return "<a href='goto_auth_url'>去授权</a>";
    }

    /**
     * 第二步
     * 预授权码（pre_auth_code）
     * <p>
     * https://developers.weixin.qq.com/doc/oplatform/Third-party_Platforms/api/pre_auth_code.html
     */
    @GetMapping("goto_auth_url")
    public void gotoPreAuthUrl(HttpServletRequest request, HttpServletResponse response) {
        String host = "wx2.dayutang.cn";
        String url = "https://" + host + "/wxThirdPlatform/authCode";
        try {
            url = weixinOpenService.getWxOpenComponentService().getPreAuthUrl(url);
            // 添加来源，解决302跳转来源丢失的问题
            response.addHeader("Referer", "https://" + host);
            response.sendRedirect(url);
        } catch (WxErrorException | IOException e) {
            log.error("gotoPreAuthUrl", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("getAuthorizerInfo")
    @ResponseBody
    public WxOpenAuthorizerInfoResult getAuthorizerInfo(@RequestParam String appId) {
        try {
            return weixinOpenService.getWxOpenComponentService().getAuthorizerInfo(appId);
        } catch (WxErrorException e) {
            log.error("getAuthorizerInfo", e);
            throw new RuntimeException(e);
        }
    }

}
