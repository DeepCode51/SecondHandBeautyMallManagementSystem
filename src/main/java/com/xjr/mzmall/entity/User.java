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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String username;

    private String name;

    private String password;

    private String email;

    private String phone;

    private String address;

    /**
     * 0买家1卖家
     */
    private Integer role;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 0 可用
     * 1 不可用
     */
    private Integer disabled;
}
