package com.github.controller;

import com.github.service.WeixinOpenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-04 11:17
 */
@RestController
@RequestMapping("/shop")
public class WeixinShopController extends BaseController {

    @Resource
    private WeixinOpenService weixinOpenService;




}
