package com.github.weixin.shop;

import com.github.weixin.WeixinShopService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-04 12:24
 */
public class WeixinShopTest extends WeixinshopApplicationTests {

    @Autowired
    private WeixinShopService weixinShopService;

    @Test
    void test() {
        String shopCategory = weixinShopService.getShopCategory();
        System.out.println("shopCategory = " + shopCategory);

        String category = weixinShopService.getCategory(null);

        System.out.println("category = " + category);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plus(-30, ChronoUnit.DAYS);
        String orderList = weixinShopService.getOrderList(startTime, now, null, null, 250, 1, 1000);

        System.out.println("orderList = " + orderList);
    }

    @Test
    void kfMsg() {
        WxMpKefuMessage kefuMessage = WxMpKefuMessage.TEXT().content("测试_from_callback😁\n换行\n<a href='www.baidu.com'>a标签</a>").toUser("o0mXB4g8mWEi3ZLhLyhwYUkL35aI").build();

        try {
            boolean isSend = weixinShopService.sendKefuMessage(kefuMessage);
            System.out.println("isSend = " + isSend);

        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
}
