package com.xjr.mzmall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjr.mzmall.DO.MyOrderDo;
import com.xjr.mzmall.DO.OrderDetailDo;
import com.xjr.mzmall.DO.PayOrderDo;
import com.xjr.mzmall.entity.MzOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjr.mzmall.vo.AdminOrderVo;
import com.xjr.mzmall.vo.CommentsGoodsVo;
import com.xjr.mzmall.vo.SellerOrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xjr
 * @since 2023-01-20
 */
@Mapper
public interface OrderMapper extends BaseMapper<MzOrder> {
    List<PayOrderDo> getPayOrderDo(@Param("orderId") Integer orderId);
    void changeOrderStatus(@Param("orderId") Integer orderId,
                           @Param("orderStatus") Integer orderStatus);
    List<PayOrderDo> getPayOrderDo2(List<Integer> orderIds);
    List<MyOrderDo> getMyOrderDo(@Param("userId") Integer userId, @Param("orderStatus") Integer orderStatus);
    List<OrderDetailDo> getOrderDetailDo(@Param("orderId") Integer orderId);
    List<CommentsGoodsVo> getCommentsGoodsVo(@Param("userId") Integer userId);
    IPage<SellerOrderVo> getSellerOrderVo(@Param("orderId") Integer orderId,
                                          @Param("userId") Integer userId,
                                          @Param("page") IPage<SellerOrderVo> page);
    IPage<SellerOrderVo> getSellerOrderListWait(@Param("orderId") Integer orderId,
                                                @Param("userId") Integer userId,
                                                @Param("page") IPage<SellerOrderVo> page);
    IPage<SellerOrderVo> getSellerOrderListCancel(@Param("orderId") Integer orderId,
                                                @Param("userId") Integer userId,
                                                @Param("page") IPage<SellerOrderVo> page);
    IPage<SellerOrderVo> getSellerOrderListReturn(@Param("orderId") Integer orderId,
                                                  @Param("userId") Integer userId,
                                                  @Param("page") IPage<SellerOrderVo> page);
    IPage<SellerOrderVo> getSellerOrderListFinish(@Param("orderId") Integer orderId,
                                                  @Param("userId") Integer userId,
                                                  @Param("page") IPage<SellerOrderVo> page);
    IPage<AdminOrderVo> getAllOrderList(@Param("orderId") Integer orderId,
                                                 @Param("page") IPage<AdminOrderVo> page);
    BigDecimal getFinishOrderAmount(@Param("storeId") Integer storeId);
}
