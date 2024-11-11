package com.xjr.mzmall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommentsGoodsVo {
    private Integer commentsCount;
    private Integer goodsId;
    private String goodsName;
    private String cover;
    private BigDecimal price;
    private Integer orderId;
}
