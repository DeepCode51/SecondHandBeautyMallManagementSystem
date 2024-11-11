package com.xjr.mzmall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDetailVo {
    private Integer orderId;
    private BigDecimal totalAmount;
    private Integer orderStatus;
    private String deliveryName;
    private String deliveryPhone;
    private String deliveryAddress;
    private String orderStatusStr;
    private List<MyOrderGoodsVo> list;
}
