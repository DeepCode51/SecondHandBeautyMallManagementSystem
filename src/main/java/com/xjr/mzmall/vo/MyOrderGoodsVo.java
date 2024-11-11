package com.xjr.mzmall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyOrderGoodsVo {
    private Integer goodsId;
    private Integer goodsCount;
    private BigDecimal price;
    private String goodsName;
    private String goodsCover;
}
