package com.xjr.mzmall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsVo {
    private Integer goodsId;
    private String goodsName;
    private BigDecimal price;
    private Integer stock;
    private String newLevel;
    private String status;
    private String categoryName;
}
