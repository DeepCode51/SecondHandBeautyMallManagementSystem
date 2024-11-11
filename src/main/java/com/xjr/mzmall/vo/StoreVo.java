package com.xjr.mzmall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreVo {
    private Integer id;
    private String storeName;
    private String userName;
    private String name;
    private BigDecimal totalAmount;
}
