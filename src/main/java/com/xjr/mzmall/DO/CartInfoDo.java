package com.xjr.mzmall.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartInfoDo {
    private Integer userId;
    private Integer goodsId;
    private String goodsName;
    private Integer count;
    private Integer storeId;
    private BigDecimal price;
    private Integer stock;
    private BigDecimal subtotal;
}
