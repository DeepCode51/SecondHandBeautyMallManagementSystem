package com.xjr.mzmall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderGoodsVo {
    private String goodsName;
    /**
     * 订单商品数量
     */
    private Integer count;
    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 商品小图片
     */
    private String cover;
}
