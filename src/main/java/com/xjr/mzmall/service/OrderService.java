package com.xjr.mzmall.service;

import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.ReturnOrderDTO;
import com.xjr.mzmall.entity.MzOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.vo.CommentsGoodsVo;
import com.xjr.mzmall.vo.MulOrderVo;
import com.xjr.mzmall.DO.OrderDo;
import com.xjr.mzmall.vo.MyOrderVo;
import com.xjr.mzmall.vo.OrderDetailVo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-01-20
 */
public interface OrderService extends IService<MzOrder> {
    Result createSingleOrder(Integer goodsId, Integer count, HttpServletRequest request);
    OrderDo getOrderInfoForPay(Integer orderId);
    Result changeOrderStatus(Integer orderId, OrderStatusEnum orderStatusEnum, BigDecimal orderAmount);
    Result createOrderFromCart(String cartIds, HttpServletRequest request);
    MulOrderVo getMelOrderVo(String orderIds);
    Result changeOrderStatus(String orderIds, OrderStatusEnum orderStatusEnum, Integer fre);
    Result changeOrderStatus(Integer orderId, OrderStatusEnum orderStatusEnum);
    Result changeOrderStatus2(Integer orderId, OrderStatusEnum orderStatusEnum);
    List<MyOrderVo> getMyOrderVo(Integer orderStatus, HttpServletRequest request);
    OrderDetailVo getOrderDetailVo(Integer orderId);
    List<CommentsGoodsVo> getCommentsGoodsVo(Integer userId);
    Result getSellerOrderList(Integer order, Integer userId, LayuiPage layuiPage);
    Result getSellerOrderListWait(Integer order, Integer userId, LayuiPage layuiPage);
    Result getSellerOrderListCancel(Integer order, Integer userId, LayuiPage layuiPage);
    Result getSellerOrderListReturn(Integer order, Integer userId, LayuiPage layuiPage);
    Result getSellerOrderListFinish(Integer order, Integer userId, LayuiPage layuiPage);
    Result returnOrderApply(ReturnOrderDTO returnOrderDTO);
    Result getAllOrder(Integer orderId, LayuiPage layuiPage);
    BigDecimal getTotalAmount(Integer storeId);
    Result agreeReturnOrderApply(Integer orderId);
}
