package com.github.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-03 17:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Users extends BaseModel{

    private static final long serialVersionUID = -4798000218050277672L;

    private String displayName;

}
