package com.xjr.mzmall.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReturnApplyDo {
    private Integer orderId;
    private String username;
    private BigDecimal totalAmount;
    private String returnReason;
    private String handleNote;
    private Integer status;
}
