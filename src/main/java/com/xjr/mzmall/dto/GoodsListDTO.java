package com.xjr.mzmall.dto;

import lombok.Data;

@Data
public class GoodsListDTO {
    private Integer userId;
    private String goodsName;
    private Integer categoryId;
    private Integer newLevel;
    private Integer status;
}
