package com.xjr.mzmall.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MyOrderVo {
    private Integer orderId;
    private BigDecimal totalAmount;
    private Integer orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String orderStatusStr;
    private List<MyOrderGoodsVo> list;
}
