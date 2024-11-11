package com.xjr.mzmall.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xjr
 * @since 2023-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MzOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    private Integer userId;

    /**
     * 店铺编号
     */
    private Integer storeId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 支付状态0未支付 1已支付
     */
    private Integer payStatus;

    /**
     * 订单状态0新下单 1待支付 2待发货 3已发货 4已签收 5待评价(已完成) 6已取消 7退单
     */
    private Integer orderStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String deliveryName;

    private String deliveryPhone;

    private String deliveryAddress;
}
