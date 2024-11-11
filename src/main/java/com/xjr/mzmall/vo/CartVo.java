package com.xjr.mzmall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartVo {
    private Integer cartId;
    private Integer goodsId;
    private Integer count;
    private BigDecimal price;
    private Integer storeId;
    private String cover;
    private String goodDesc;
    private Integer stock;
    private String goodsName;
    private BigDecimal subtotal;
}
