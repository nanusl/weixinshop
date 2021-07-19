package com.github.weixin.shop;

import com.github.mapper.UsersMapper;
import com.github.entity.Users;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-03 19:10
 */
public class MybatisPlusTest extends WeixinshopApplicationTests {

    @Resource
    private UsersMapper mapper;

    @Test
    public void select() {
        List<Users> userList = mapper.selectList(null);
        userList.forEach(System.out::println);
        Users users = mapper.findByDisplayName("a");
    }
}
