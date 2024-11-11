package com.xjr.mzmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.DO.ReturnApplyDo;
import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.OrderReturnApply;
import com.xjr.mzmall.entity.Store;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.mapper.OrderMapper;
import com.xjr.mzmall.mapper.OrderReturnApplyMapper;
import com.xjr.mzmall.mapper.StoreMapper;
import com.xjr.mzmall.service.OrderReturnApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjr
 * @since 2023-02-09
 */
@Service
public class OrderReturnApplyServiceImpl extends ServiceImpl<OrderReturnApplyMapper, OrderReturnApply> implements OrderReturnApplyService {

    @Autowired
    private OrderReturnApplyMapper orderReturnApplyMapper;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Result getReturnApplyList(LayuiPage layuiPage, Integer orderId, HttpServletRequest request) {
        Page<ReturnApplyDo> page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        page.setOptimizeCountSql(false);
        User user = (User) request.getSession().getAttribute("user");
        Store store = storeMapper.selectOne(new LambdaQueryWrapper<Store>().eq(Store::getUserId, user.getUserId()));
        List<ReturnApplyDo> returnApplyDo = orderReturnApplyMapper.getReturnApplyDo(page, orderId, store.getId());
        return Result.success(returnApplyDo);
    }

    @Override
    public Result refuseOrderReturnApply(Integer orderId, String handleNote) {
        orderReturnApplyMapper.update(null, new LambdaUpdateWrapper<OrderReturnApply>()
                .eq(OrderReturnApply::getOrderId, orderId)
                .set(OrderReturnApply::getHandleNote, handleNote)
                .set(OrderReturnApply::getStatus, 1));
        orderMapper.changeOrderStatus(orderId, OrderStatusEnum.REFUSE_RETURN.getCode());
        return Result.success();
    }
}
