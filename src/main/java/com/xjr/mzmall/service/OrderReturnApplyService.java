package com.xjr.mzmall.service;

import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.OrderReturnApply;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-02-09
 */
public interface OrderReturnApplyService extends IService<OrderReturnApply> {
    Result getReturnApplyList(LayuiPage layuiPage, Integer orderId, HttpServletRequest request);
    Result refuseOrderReturnApply(Integer orderId, String handleNote);
}
