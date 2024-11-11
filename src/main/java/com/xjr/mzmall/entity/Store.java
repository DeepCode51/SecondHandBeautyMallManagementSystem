package com.xjr.mzmall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String storeName;

    private Integer userId;

    /**
     * 0 可用
     * 1 不可用
     */
    private Integer disabled;

}
