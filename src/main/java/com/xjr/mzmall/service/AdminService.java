package com.xjr.mzmall.service;

import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-01-18
 */
public interface AdminService extends IService<Admin> {
    Result login(Admin admin, String captcha, HttpServletRequest request);
    Result updatePsd(String oldPassword, String newPassword, Integer adminId);
}
