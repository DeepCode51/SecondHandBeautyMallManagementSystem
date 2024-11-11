package com.xjr.mzmall.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailDo {
    private Integer orderId;
    private BigDecimal totalAmount;
    private Integer orderStatus;
    private Integer goodsId;
    private Integer goodsCount;
    private String deliveryName;
    private String deliveryPhone;
    private String deliveryAddress;
    private String goodsName;
    private String cover;
    private BigDecimal price;
    private String orderStatusStr;
}
