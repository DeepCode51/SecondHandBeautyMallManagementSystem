package com.xjr.mzmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.DO.ComplaintOrderDo;
import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.AddComplaintDTO;
import com.xjr.mzmall.entity.Complaint;
import com.xjr.mzmall.entity.MzOrder;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.mapper.ComplaintMapper;
import com.xjr.mzmall.mapper.OrderMapper;
import com.xjr.mzmall.mapper.UserMapper;
import com.xjr.mzmall.service.ComplaintService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjr.mzmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjr
 * @since 2023-02-11
 */
@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements ComplaintService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private OrderService orderService;

    @Override
    public Result addComplaint(AddComplaintDTO addComplaintDTO, HttpServletRequest request) {
        Complaint complaint = new Complaint();
        complaint.setOrderId(addComplaintDTO.getOrderId());
        complaint.setComplaintReason(addComplaintDTO.getReason());
        MzOrder mzOrder = orderMapper.selectById(addComplaintDTO.getOrderId());
        Integer userId = mzOrder.getUserId();
        User user = userMapper.selectById(userId);
        complaint.setUsername(user.getUsername());
        complaint.setUserPhone(user.getPhone());
        complaint.setStatus(0);
        complaintMapper.insert(complaint);
        orderMapper.changeOrderStatus(addComplaintDTO.getOrderId(), OrderStatusEnum.COMPLAINT_ING.getCode());
        return Result.success();
    }

    @Override
    public Result agreeComplaint(Integer id) {
        Complaint complaint = complaintMapper.selectById(id);
        Integer orderId = complaint.getOrderId();
        complaintMapper.update(null, new LambdaUpdateWrapper<Complaint>()
        .eq(Complaint::getId, id)
        .set(Complaint::getStatus, 1));
        // 修改订单状态
        orderMapper.update(null, new LambdaUpdateWrapper<MzOrder>()
                .eq(MzOrder::getOrderId, orderId)
        .set(MzOrder::getOrderStatus, OrderStatusEnum.CHARGEBACKS.getCode()));
        // 调用第三方支付接口 TODO
        return Result.success();
    }

    @Override
    public Result refuseComplaint(Integer id) {
        Complaint complaint = complaintMapper.selectById(id);
        Integer orderId = complaint.getOrderId();
        complaintMapper.update(null, new LambdaUpdateWrapper<Complaint>()
                .eq(Complaint::getId, id)
                .set(Complaint::getStatus, -1));
        orderMapper.update(null, new LambdaUpdateWrapper<MzOrder>()
                .eq(MzOrder::getOrderId, orderId)
                .set(MzOrder::getOrderStatus, OrderStatusEnum.SHIPPED.getCode()));
        return Result.success();
    }

    @Override
    public Result getComplaintList(LayuiPage layuiPage, Integer orderId) {
        Page<ComplaintOrderDo> page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        Page<ComplaintOrderDo> complaintList = complaintMapper.getComplaintList(page, orderId);
        return Result.success(complaintList.getRecords());
    }
}
