package com.xjr.mzmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum OrderStatusEnum {

    NEW_ORDERS(0, "新下单"),
    TO_BE_PAID(1, "待支付"),
    TO_BE_SHIPPED(2, "待发货"),
    SHIPPED(3, "已发货"),
    SIGNED(4, "已签收"),
    TO_BE_EVALUATED(5, "待评价"),
    CANCELED(6, "已取消"),
    CHARGEBACKS(7, "退单"),
    CANCEL_ORDER_APPLY(8, "申请取消订单"),
    CHARGEBACKS_APPLY(9, "申请退单"),
    FINISH(10, "已完成"),
    REFUSE_RETURN(11, "拒绝退单"),
    COMPLAINT_ORDER(12, "申诉订单"),
    COMPLAINT_ING(13, "申诉中");

    private Integer code;
    private String description;

}
