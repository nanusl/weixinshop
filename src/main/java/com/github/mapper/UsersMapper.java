package com.github.mapper;

import com.github.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-03 17:49
 */
public interface UsersMapper extends BaseMapper<Users> {

    Users findByDisplayName(String displayName);

}
