package com.xjr.mzmall.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wf.captcha.utils.CaptchaUtil;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Admin;
import com.xjr.mzmall.mapper.AdminMapper;
import com.xjr.mzmall.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjr
 * @since 2023-01-18
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public Result login(Admin admin, String captcha, HttpServletRequest request) {
        //验证码检验
        if (!CaptchaUtil.ver(captcha, request)) {
            CaptchaUtil.clear(request);  // 清除session中的验证码
            return Result.fail("验证码错误");
        }
        List<Admin> list =
                this.list(new LambdaQueryWrapper<Admin>()
                        .eq(Admin::getUsername, admin.getUsername()));
        if (list.isEmpty()) {
            return Result.fail("用户名不存在");
        }
        if (list.size() > 1) {
            return Result.fail("用户名不唯一，请联系运维人员排查问题");
        }
        String password = admin.getPassword();
        String md5Hex = DigestUtil.md5Hex(password);
        if (list.get(0).getPassword().equals(md5Hex)) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", admin);
            return Result.success();
        }
        return Result.fail("未知错误，请联系运维人员排查问题");
    }

    @Override
    public Result updatePsd(String oldPassword, String newPassword, Integer adminId) {
        Admin admin = this.getById(adminId);
        String s = DigestUtil.md5Hex(oldPassword);
        if (!admin.getPassword().equals(s)) {
            return Result.fail("原密码错误");
        }
        String s1 = DigestUtil.md5Hex(newPassword);
        Admin admin1 = new Admin();
        admin1.setId(adminId);
        admin1.setPassword(s1);
        this.updateById(admin1);
        return Result.success();
    }
}
