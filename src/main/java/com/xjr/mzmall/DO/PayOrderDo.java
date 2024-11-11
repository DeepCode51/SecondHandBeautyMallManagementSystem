package com.xjr.mzmall.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayOrderDo {
    private Integer orderId;
    private BigDecimal totalAmount;
    private Integer goodsCount;
    private String goodsName;
    private BigDecimal price;
    private String cover;
}
