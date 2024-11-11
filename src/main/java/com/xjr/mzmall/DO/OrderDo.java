package com.xjr.mzmall.DO;

import com.xjr.mzmall.vo.OrderGoodsVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDo {
    private Integer orderId;

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
