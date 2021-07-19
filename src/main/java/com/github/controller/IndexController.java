package com.github.controller;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-01-27 11:42
 */
@RestController
@RequestMapping(/*consumes = {"text/plain", "application/json"}*/)
public class IndexController extends BaseController {

    @GetMapping(value = {"/", "/index", "index.html"}, produces = "text/html")
    public void index(HttpServletResponse response) throws IOException {
        Map<String, Object> vo = Maps.newHashMap();
        vo.put("uuid", RandomUtil.randomChar());
        vo.put("name", "test");
        vo.put("applicationName", "weixinshop");
        vo.put("authorities", "使用你的帐号信息（昵称、头像、地区及性别）");
        vo.put("last_login_time", new Date());
        renderHtml(response, "index", vo);
    }

    @GetMapping(value = "/get")
    public HttpEntity<String> get() {

        return ResponseEntity.ok("get");
    }

    @PostMapping(value = "/post")
    public HttpEntity<String> post() {

        return ResponseEntity.ok("post");
    }
}
