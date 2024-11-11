package com.xjr.mzmall.service;

import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.AddComplaintDTO;
import com.xjr.mzmall.entity.Complaint;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-02-11
 */
public interface ComplaintService extends IService<Complaint> {
    Result addComplaint(AddComplaintDTO addComplaintDTO, HttpServletRequest request);
    Result agreeComplaint(Integer id);
    Result refuseComplaint(Integer id);
    Result getComplaintList(LayuiPage layuiPage, Integer orderId);
}
