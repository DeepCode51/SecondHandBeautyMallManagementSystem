package com.xjr.mzmall.DO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MyOrderDo {
    private Integer orderId;
    private BigDecimal totalAmount;
    private Integer orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer goodsId;
    private Integer goodsCount;
    private String goodsName;
    private String cover;
    private BigDecimal price;
    private String orderStatusStr;
}
