package com.xjr.mzmall.service;

import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
public interface UserService extends IService<User> {
    Result getUserList(LayuiPage layuiPage, String username, String phone, Integer code);
    Result create(User user);
    Result updateUser(User user);
    Result getUser(Integer userId);
    Result deleteByIds(String ids);
    Result regUser(User user);
    Result login(String username, String password, HttpServletRequest request);
    Result regSeller(User user, String storeName);

    Result blacked(Integer id);

    Result cancelBlacked(Integer id);

    Result sellerLogin(String username, String password, HttpServletRequest request);

    Result getBlackedUser(LayuiPage layuiPage, String username, String phone, Integer role);
}
