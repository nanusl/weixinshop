package com.github.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-03 20:32
 */
@Data
public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = -5603782447444190927L;

    @TableId
    private Long id;

}
