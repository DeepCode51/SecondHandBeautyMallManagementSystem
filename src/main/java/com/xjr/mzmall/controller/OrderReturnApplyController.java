package com.xjr.mzmall.controller;


import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.RefuseReasonDTO;
import com.xjr.mzmall.service.OrderReturnApplyService;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xjr
 * @since 2023-02-09
 */
@RestController
@RequestMapping("/seller/order/returnApply")
public class OrderReturnApplyController {

    @Autowired
    private OrderReturnApplyService orderReturnApplyService;

    @PostMapping("/refuse")
    public Result refuseOrderReturnApply(RefuseReasonDTO refuseReasonDTO) {
        return orderReturnApplyService.refuseOrderReturnApply(refuseReasonDTO.getOrderId(),
                refuseReasonDTO.getHandleNote());
    }

    @GetMapping("/list")
    public Result getList(LayuiPage layuiPage, Integer orderId, HttpServletRequest request) {
        return orderReturnApplyService.getReturnApplyList(layuiPage, orderId, request);
    }

}

