package com.xjr.mzmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PayStatusEnum {

    NOT_PAID(0, "未支付"),
    PAID(1, "已支付");

    private Integer code;
    private String description;
}
