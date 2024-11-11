package com.xjr.mzmall.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminOrderVo {
    private Integer orderId;
    private BigDecimal totalAmount;
    private Integer orderStatus;
    private Integer payStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String orderStatusStr;
    private String payStatusStr;
}
