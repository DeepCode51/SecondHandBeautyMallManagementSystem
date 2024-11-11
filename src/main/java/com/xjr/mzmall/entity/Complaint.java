package com.xjr.mzmall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xjr
 * @since 2023-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer orderId;

    private String complaintReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 处理状态 0未处理 1已处理
     */
    private Integer status;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户电话
     */
    private String userPhone;


}
