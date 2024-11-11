package com.xjr.mzmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum  GoodsStatusEnum {

    SHELVES(0, "上架中"),
    TAKE_DOWN(1, "已下架");

    private Integer code;
    private String description;

}
