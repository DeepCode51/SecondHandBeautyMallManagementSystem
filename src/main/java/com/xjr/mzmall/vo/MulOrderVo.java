package com.xjr.mzmall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MulOrderVo {
    /**
     * 1,2,3
     */
    private String orderIds;
    /**
     * 总金额 不含运费
     */
    private BigDecimal goodsTotalAmount;
    /**
     * 总金额 含运费
     */
    private BigDecimal TotalAmount;

    private List<OrderGoodsVo> list;
}
