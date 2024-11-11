package com.xjr.mzmall.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.DO.*;
import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.ReturnOrderDTO;
import com.xjr.mzmall.entity.*;
import com.xjr.mzmall.enums.GoodsStatusEnum;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.enums.PayStatusEnum;
import com.xjr.mzmall.mapper.*;
import com.xjr.mzmall.service.OrderDetailService;
import com.xjr.mzmall.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjr.mzmall.utils.CommonUtils;
import com.xjr.mzmall.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjr
 * @since 2023-01-20
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, MzOrder> implements OrderService {

    // 运费
    private final int freight = 3;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderReturnApplyMapper orderReturnApplyMapper;


    @Override
    public Result createSingleOrder(Integer goodsId, Integer count, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        Store store = storeMapper.selectOne(new LambdaQueryWrapper<Store>().eq(Store::getUserId, userId));
        Goods goods = goodsMapper.selectById(goodsId);
        // 判断库存
        Integer stock = goods.getStock();
        if (stock > 0) {
            Goods goods1 = new Goods();
            goods1.setGoodsId(goods.getGoodsId());
            if (stock - count > 0) {
                // 更新库存信息
                goods1.setStock(stock - count);
            }else if (stock - count == 0) {
                goods1.setStock(0);
                goods1.setStatus(GoodsStatusEnum.TAKE_DOWN.getCode());
            }else {
                return Result.fail("商品库存不足");
            }
            goodsMapper.updateById(goods1);
        }else {
            return Result.fail("商品库存不足");
        }
        MzOrder order = new MzOrder();
        order.setUserId(userId);
        order.setStoreId(store.getId());
        order.setPayStatus(PayStatusEnum.NOT_PAID.getCode());
        order.setOrderStatus(OrderStatusEnum.TO_BE_PAID.getCode());
        order.setTotalAmount(goods.getPrice());
        order.setCreateTime(LocalDateTime.now());
        order.setDeliveryName(user.getName());
        order.setDeliveryPhone(user.getPhone());
        order.setDeliveryAddress(user.getAddress());
        log.info("开始加入订单表");
        this.save(order);
        Integer orderId = order.getOrderId();
        log.info("新生成的订单编号为====>" + orderId);
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setGoodsId(goodsId);
        orderDetail.setGoodsCount(count);
        orderDetail.setCreateTime(LocalDateTime.now());
        log.info("开始加入订单详情表");
        orderDetailMapper.insert(orderDetail);
        log.info("加入订单详情表完成");
        return Result.success(orderId);
    }

    @Override
    public OrderDo getOrderInfoForPay(Integer orderId) {
        List<PayOrderDo> payOrderDo = orderMapper.getPayOrderDo(orderId);
        OrderDo orderVo = new OrderDo();
        orderVo.setOrderId(payOrderDo.get(0).getOrderId());
        orderVo.setGoodsTotalAmount(payOrderDo.get(0).getTotalAmount());
        orderVo.setTotalAmount(payOrderDo.get(0).getTotalAmount().add(new BigDecimal(freight)));
        ArrayList<OrderGoodsVo> orderGoodsVos = new ArrayList<>();
        for (PayOrderDo orderDo : payOrderDo) {
            OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
            orderGoodsVo.setGoodsName(orderDo.getGoodsName());
            orderGoodsVo.setCount(orderDo.getGoodsCount());
            orderGoodsVo.setPrice(orderDo.getPrice());
            orderGoodsVo.setCover(orderDo.getCover());
            orderGoodsVos.add(orderGoodsVo);
        }
        orderVo.setList(orderGoodsVos);
        return orderVo;
    }

    @Override
    public Result changeOrderStatus(Integer orderId, OrderStatusEnum orderStatusEnum, BigDecimal orderAmount) {
        // 调用第三方支付接口 TODO
        // 如果成功则修改订单状态
        MzOrder mzOrder = new MzOrder();
        mzOrder.setOrderId(orderId);
        mzOrder.setOrderStatus(orderStatusEnum.getCode());
        mzOrder.setPayStatus(PayStatusEnum.PAID.getCode());
        mzOrder.setTotalAmount(orderAmount);
        this.updateById(mzOrder);
        return Result.success();
    }

    @Override
    public Result createOrderFromCart(String cartIds, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String name = user.getName();
        String address = user.getAddress();
        String phone = user.getPhone();
        List<Integer> list = CommonUtils.StringsToList(cartIds);
        List<CartInfoDo> cartInfoDo = shoppingCartMapper.getCartInfoDo(list);
        // 店铺订单映射关系
        HashMap<Integer, MzOrder> mzOrderHashMap = new HashMap<>();
        for (CartInfoDo infoDo : cartInfoDo) {
            if (infoDo.getStock() < infoDo.getCount()) {
                return Result.fail(infoDo.getGoodsName() + "库存不足");
            }
            Integer goodsId = infoDo.getGoodsId();
            // 更新库存
            log.info("更新商品库存");
            Goods goods = new Goods();
            goods.setGoodsId(goodsId);
            goods.setStock(infoDo.getStock() - infoDo.getCount());
            if (infoDo.getStock() - infoDo.getCount() == 0) {
                goods.setStatus(GoodsStatusEnum.TAKE_DOWN.getCode());
            }
            goodsMapper.updateById(goods);
            log.info("更新商品库存完成");
            if (!mzOrderHashMap.containsKey(infoDo.getStoreId())) {
                MzOrder mzOrder = new MzOrder();
                mzOrder.setUserId(infoDo.getUserId());
                mzOrder.setStoreId(infoDo.getStoreId());
                mzOrder.setTotalAmount(infoDo.getSubtotal().add(new BigDecimal(freight)));
                mzOrder.setPayStatus(PayStatusEnum.NOT_PAID.getCode());
                mzOrder.setOrderStatus(OrderStatusEnum.TO_BE_PAID.getCode());
                mzOrder.setDeliveryName(name);
                mzOrder.setDeliveryPhone(phone);
                mzOrder.setDeliveryAddress(address);
                mzOrderHashMap.put(infoDo.getStoreId(),mzOrder);
            }else {
                MzOrder mzOrder = mzOrderHashMap.get(infoDo.getStoreId());
                mzOrder.setTotalAmount(mzOrder.getTotalAmount().add(infoDo.getSubtotal()));
                mzOrderHashMap.put(infoDo.getStoreId(), mzOrder);
            }
        }
        Iterator<Map.Entry<Integer, MzOrder>> iterator = mzOrderHashMap.entrySet().iterator();
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        ArrayList<Integer> list1 = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Integer, MzOrder> next = iterator.next();
            MzOrder value = next.getValue();

            // 店铺编号
            Integer key = next.getKey();
            // 加入订单表
            log.info("开始加入订单表，所属店铺编号为" + key);
            orderMapper.insert(value);
            Integer orderId = value.getOrderId();
            list1.add(orderId);
            for (CartInfoDo infoDo : cartInfoDo) {
                if (infoDo.getStoreId() == key) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setGoodsId(infoDo.getGoodsId());
                    orderDetail.setGoodsCount(infoDo.getCount());
                    orderDetails.add(orderDetail);
                }
            }
        }
        // 加入订单详情表
        log.info("开始加入订单详情表");
        orderDetailService.saveBatch(orderDetails);
        // 所有操作操作完成后删除购物车的内容
        shoppingCartMapper.deleteBatchIds(list);
        return Result.success(list1);
    }

    @Override
    public MulOrderVo getMelOrderVo(String orderIds) {
        List<Integer> list = CommonUtils.StringsToList(orderIds);
        List<PayOrderDo> payOrderDo2 = orderMapper.getPayOrderDo2(list);
        MulOrderVo mulOrderVo = new MulOrderVo();
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<OrderGoodsVo> orderGoodsVos = new ArrayList<>();
        BigDecimal bigDecimal = new BigDecimal(0);
        for (PayOrderDo payOrderDo : payOrderDo2) {
            stringBuffer.append(payOrderDo.getOrderId());
            stringBuffer.append(",");
            OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
            orderGoodsVo.setGoodsName(payOrderDo.getGoodsName());
            orderGoodsVo.setCount(payOrderDo.getGoodsCount());
            orderGoodsVo.setPrice(payOrderDo.getPrice());
            orderGoodsVo.setCover(payOrderDo.getCover());
            bigDecimal = bigDecimal.add(payOrderDo.getTotalAmount());
            orderGoodsVos.add(orderGoodsVo);
        }
        mulOrderVo.setOrderIds(stringBuffer.toString());
        mulOrderVo.setGoodsTotalAmount(bigDecimal);
        mulOrderVo.setTotalAmount(bigDecimal.add(new BigDecimal(freight)));
        mulOrderVo.setList(orderGoodsVos);
        return mulOrderVo;
    }

    @Override
    public Result changeOrderStatus(String orderIds, OrderStatusEnum orderStatusEnum, Integer fre) {
        List<Integer> list = CommonUtils.StringsToList(orderIds);
        if (fre == 1) {
            // 有运费
            for (Integer integer : list) {
                MzOrder mzOrder = orderMapper.selectById(integer);
                mzOrder.setTotalAmount(mzOrder.getTotalAmount().add(new BigDecimal(freight)));
                mzOrder.setPayStatus(PayStatusEnum.PAID.getCode());
                mzOrder.setOrderStatus(orderStatusEnum.getCode());
                orderMapper.updateById(mzOrder);
            }
        }else {
            for (Integer integer : list) {
                MzOrder mzOrder = orderMapper.selectById(integer);
                mzOrder.setPayStatus(PayStatusEnum.PAID.getCode());
                mzOrder.setOrderStatus(orderStatusEnum.getCode());
                orderMapper.updateById(mzOrder);
            }
        }
        return Result.success();
    }

    /**
     * 用于修改不需要修改商品数量的情况
     * @param orderId
     * @param orderStatusEnum
     * @return
     */
    @Override
    public Result changeOrderStatus2(Integer orderId, OrderStatusEnum orderStatusEnum) {
        MzOrder mzOrder = new MzOrder();
        mzOrder.setOrderId(orderId);
        mzOrder.setOrderStatus(orderStatusEnum.getCode());
        orderMapper.updateById(mzOrder);
        return Result.success();
    }

    /**
     * 用于修改需要修改商品数量的情况
     * @param orderId
     * @param orderStatusEnum
     * @return
     */
    @Override
    public Result changeOrderStatus(Integer orderId, OrderStatusEnum orderStatusEnum) {
        MzOrder mzOrder = new MzOrder();
        mzOrder.setOrderId(orderId);
        mzOrder.setOrderStatus(orderStatusEnum.getCode());
        orderMapper.updateById(mzOrder);
        // 找到订单详情
        List<OrderDetail> orderDetails =
                orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                        .eq(OrderDetail::getOrderId, orderId));
        // 修改商品数量及状态
        for (OrderDetail orderDetail : orderDetails) {
            Goods goods = goodsMapper.selectById(orderDetail.getGoodsId());
            goods.setStock(goods.getStock() + orderDetail.getGoodsCount());
            if (goods.getStatus() == GoodsStatusEnum.TAKE_DOWN.getCode()) {
                goods.setStatus(GoodsStatusEnum.SHELVES.getCode());
            }
            goodsMapper.updateById(goods);
        }
        return Result.success();
    }

    @Override
    public List<MyOrderVo> getMyOrderVo(Integer orderStatus, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<MyOrderDo> myOrderDoList = orderMapper.getMyOrderDo(user.getUserId(), orderStatus);
        ArrayList<MyOrderVo> myOrderVos = new ArrayList<>();
        HashMap<Integer, MyOrderVo> integerMyOrderVoHashMap = new HashMap<>();
        for (MyOrderDo myOrderDo : myOrderDoList) {
            if (!integerMyOrderVoHashMap.containsKey(myOrderDo.getOrderId())) {
                MyOrderVo myOrderVo = new MyOrderVo();
                myOrderVo.setOrderId(myOrderDo.getOrderId());
                myOrderVo.setTotalAmount(myOrderDo.getTotalAmount());
                myOrderVo.setCreateTime(myOrderDo.getCreateTime());
                myOrderVo.setOrderStatus(myOrderDo.getOrderStatus());
                myOrderVo.setOrderStatusStr(myOrderDo.getOrderStatusStr());
                MyOrderGoodsVo myOrderGoodsVo = new MyOrderGoodsVo();
                myOrderGoodsVo.setGoodsId(myOrderDo.getGoodsId());
                myOrderGoodsVo.setGoodsCount(myOrderDo.getGoodsCount());
                myOrderGoodsVo.setGoodsCover(myOrderDo.getCover());
                myOrderGoodsVo.setGoodsName(myOrderDo.getGoodsName());
                myOrderGoodsVo.setPrice(myOrderDo.getPrice());
                ArrayList<MyOrderGoodsVo> myOrderGoodsVos = new ArrayList<>();
                myOrderGoodsVos.add(myOrderGoodsVo);
                myOrderVo.setList(myOrderGoodsVos);
                integerMyOrderVoHashMap.put(myOrderDo.getOrderId(), myOrderVo);
            }else {
                MyOrderVo myOrderVo = integerMyOrderVoHashMap.get(myOrderDo.getOrderId());
                List<MyOrderGoodsVo> list = myOrderVo.getList();
                MyOrderGoodsVo myOrderGoodsVo = new MyOrderGoodsVo();
                myOrderGoodsVo.setGoodsId(myOrderDo.getGoodsId());
                myOrderGoodsVo.setGoodsCount(myOrderDo.getGoodsCount());
                myOrderGoodsVo.setGoodsCover(myOrderDo.getCover());
                myOrderGoodsVo.setGoodsName(myOrderDo.getGoodsName());
                myOrderGoodsVo.setPrice(myOrderDo.getPrice());
                list.add(myOrderGoodsVo);
                myOrderVo.setList(list);
                integerMyOrderVoHashMap.put(myOrderDo.getOrderId(), myOrderVo);
            }
        }
        Iterator<Map.Entry<Integer, MyOrderVo>> iterator = integerMyOrderVoHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, MyOrderVo> next = iterator.next();
            MyOrderVo value = next.getValue();
            myOrderVos.add(value);
        }
        myOrderVos.sort((t1,t2)->t2.getCreateTime().compareTo(t1.getCreateTime()));
        return myOrderVos;
    }

    @Override
    public OrderDetailVo getOrderDetailVo(Integer orderId) {
        List<OrderDetailDo> orderDetailDo = orderMapper.getOrderDetailDo(orderId);
        ArrayList<MyOrderGoodsVo> myOrderGoodsVos = new ArrayList<>();
        for (OrderDetailDo detailDo : orderDetailDo) {
            MyOrderGoodsVo myOrderGoodsVo = new MyOrderGoodsVo();
            myOrderGoodsVo.setGoodsId(detailDo.getGoodsId());
            myOrderGoodsVo.setGoodsName(detailDo.getGoodsName());
            myOrderGoodsVo.setGoodsCover(detailDo.getCover());
            myOrderGoodsVo.setGoodsCount(detailDo.getGoodsCount());
            myOrderGoodsVo.setPrice(detailDo.getPrice());
            myOrderGoodsVos.add(myOrderGoodsVo);
        }
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        OrderDetailDo orderDetailDo1 = orderDetailDo.get(0);
        orderDetailVo.setOrderId(orderDetailDo1.getOrderId());
        orderDetailVo.setOrderStatus(orderDetailDo1.getOrderStatus());
        orderDetailVo.setOrderStatusStr(orderDetailDo1.getOrderStatusStr());
        orderDetailVo.setTotalAmount(orderDetailDo1.getTotalAmount());
        orderDetailVo.setDeliveryName(orderDetailDo1.getDeliveryName());
        orderDetailVo.setDeliveryPhone(orderDetailDo1.getDeliveryPhone());
        orderDetailVo.setDeliveryAddress(orderDetailDo1.getDeliveryAddress());
        orderDetailVo.setList(myOrderGoodsVos);
        return orderDetailVo;
    }

    @Override
    public List<CommentsGoodsVo> getCommentsGoodsVo(Integer userId) {
        return orderMapper.getCommentsGoodsVo(userId);
    }

    @Override
    public Result getSellerOrderList(Integer order, Integer userId, LayuiPage layuiPage) {
        Page<SellerOrderVo> page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        page.setOptimizeCountSql(false);
        IPage<SellerOrderVo> sellerOrderVo = orderMapper.getSellerOrderVo(order, userId, page);
        return Result.success(sellerOrderVo.getRecords());
    }

    @Override
    public Result getSellerOrderListWait(Integer order, Integer userId, LayuiPage layuiPage) {
        Page<SellerOrderVo> sellerOrderVoPage = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        sellerOrderVoPage.setOptimizeCountSql(false);
        IPage<SellerOrderVo> sellerOrderListWait = orderMapper.getSellerOrderListWait(order, userId, sellerOrderVoPage);
        return Result.success(sellerOrderListWait.getRecords());
    }

    @Override
    public Result getSellerOrderListCancel(Integer order, Integer userId, LayuiPage layuiPage) {
        Page<SellerOrderVo> page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        page.setOptimizeCountSql(false);
        IPage<SellerOrderVo> sellerOrderListCancel = orderMapper.getSellerOrderListCancel(order, userId, page);
        return Result.success(sellerOrderListCancel.getRecords());
    }

    @Override
    public Result getSellerOrderListReturn(Integer order, Integer userId, LayuiPage layuiPage) {
        Page<SellerOrderVo> page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        page.setOptimizeCountSql(false);
        IPage<SellerOrderVo> sellerOrderListCancel = orderMapper.getSellerOrderListReturn(order, userId, page);
        return Result.success(sellerOrderListCancel.getRecords());
    }

    @Override
    public Result getSellerOrderListFinish(Integer order, Integer userId, LayuiPage layuiPage) {
        Page<SellerOrderVo> page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        page.setOptimizeCountSql(false);
        IPage<SellerOrderVo> sellerOrderListCancel = orderMapper.getSellerOrderListFinish(order, userId, page);
        return Result.success(sellerOrderListCancel.getRecords());
    }

    /**
     * 申请退单
     * @param returnOrderDTO
     * @return
     */
    @Override
    public Result returnOrderApply(ReturnOrderDTO returnOrderDTO) {
        MzOrder mzOrder = orderMapper.selectById(returnOrderDTO.getOrderId());
        LocalDateTime createTime = mzOrder.getCreateTime();
        long between = LocalDateTimeUtil.between(createTime, LocalDateTime.now(), ChronoUnit.DAYS);
        // 订单大于7天无法退单
        if (between > 7) {
            return Result.fail("订单太久啦，无法退单");
        }
        // 修改订单状态
        orderMapper.changeOrderStatus(returnOrderDTO.getOrderId(), OrderStatusEnum.CHARGEBACKS_APPLY.getCode());
        // 加入退单申请表
        OrderReturnApply orderReturnApply = new OrderReturnApply();
        orderReturnApply.setOrderId(returnOrderDTO.getOrderId());
        orderReturnApply.setUserId(mzOrder.getUserId());
        orderReturnApply.setStoreId(mzOrder.getStoreId());
        orderReturnApply.setReturnReason(returnOrderDTO.getReturnReason());
        orderReturnApply.setTotalAmount(mzOrder.getTotalAmount());
        orderReturnApplyMapper.insert(orderReturnApply);
        return Result.success();
    }

    @Override
    public Result getAllOrder(Integer orderId, LayuiPage layuiPage) {
        Page<AdminOrderVo> adminOrderVoPage = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        IPage<AdminOrderVo> allOrderList = orderMapper.getAllOrderList(orderId, adminOrderVoPage);
        return Result.success(allOrderList.getRecords());
    }

    @Override
    public Result agreeReturnOrderApply(Integer orderId) {
        // 调用第三方支付接口 TODO
        orderMapper.update(null, new LambdaUpdateWrapper<MzOrder>()
                .eq(MzOrder::getOrderId, orderId)
                .set(MzOrder::getOrderStatus, OrderStatusEnum.CHARGEBACKS.getCode()));
        orderReturnApplyMapper.update(null,
                new LambdaUpdateWrapper<OrderReturnApply>()
                .eq(OrderReturnApply::getOrderId, orderId)
                .set(OrderReturnApply::getHandleNote, "同意")
                .set(OrderReturnApply::getStatus, 1));
        return Result.success();
    }

    @Override
    public BigDecimal getTotalAmount(Integer storeId) {
        BigDecimal finishOrderAmount = orderMapper.getFinishOrderAmount(storeId);
        if (Objects.isNull(finishOrderAmount)){
            finishOrderAmount = new BigDecimal(0);
        }
        return finishOrderAmount;
    }
}
