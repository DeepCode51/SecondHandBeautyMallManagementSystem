package com.xjr.mzmall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Category;
import com.xjr.mzmall.entity.Store;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.ResultEnum;
import com.xjr.mzmall.mapper.CategoryMapper;
import com.xjr.mzmall.mapper.StoreMapper;
import com.xjr.mzmall.mapper.UserMapper;
import com.xjr.mzmall.service.StoreService;
import com.xjr.mzmall.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjr.mzmall.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final String PASSWORD = "123456";
    private final String AVATAR = "moren.jpg";

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private StoreMapper storeMapper;

    @Override
    public Result updateUser(User user) {
        this.updateById(user);
        return Result.success();
    }

    @Override
    public Result getUser(Integer userId) {
        User user = this.getById(userId);
        return Result.success(user);
    }

    @Override
    public Result deleteByIds(String ids) {
        List<Integer> list = CommonUtils.StringsToList(ids);
        this.removeByIds(list);
        return Result.success();
    }

    @Override
    public Result regUser(User user) {
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if (list.size() > 0) {
            return Result.fail("该用户名已存在，请更换");
        }
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        user.setAvatar(AVATAR);
        this.save(user);
        return Result.success();
    }

    @Override
    public Result login(String username, String password, HttpServletRequest request) {
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (list.size() > 1) {
            return Result.fail("账号不唯一，请联系管理员排查");
        }
        User user = list.get(0);
        String s = DigestUtil.md5Hex(password);
        if (!user.getPassword().equals(s)) {
            return Result.fail("账号密码不正确");
        }
        request.getSession().setAttribute("user", user);
        Object allCategory = request.getSession().getAttribute("allCategory");
        if (Objects.isNull(allCategory)) {
            List<Category> categories = categoryMapper.selectList(null);
            List<Category> categories1 = categoryMapper.topFive();
            request.getSession().setAttribute("allCategory", categories);
            request.getSession().setAttribute("topCategory", categories1);
        }
        return Result.success();
    }

    @Override
    public Result sellerLogin(String username, String password, HttpServletRequest request) {
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (list.size() > 1) {
            return Result.fail("账号不唯一，请联系管理员排查");
        }
        User user = list.get(0);
        String s = DigestUtil.md5Hex(password);
        if (!user.getPassword().equals(s)) {
            return Result.fail("账号密码不正确");
        }
        if (0 == user.getRole()) {
            return Result.fail("抱歉，您还未完成卖家注册");
        }
        List<Store> stores = storeMapper.selectList(new LambdaQueryWrapper<Store>().eq(Store::getUserId, user.getUserId()));
        Store store = stores.get(0);
        if (store.getDisabled() == 1) {
            return Result.fail("抱歉，您的账户已被管理员拉黑。");
        }
        request.getSession().setAttribute("user", user);
        Object allCategory = request.getSession().getAttribute("allCategory");
        if (Objects.isNull(allCategory)) {
            List<Category> categories = categoryMapper.selectList(null);
            List<Category> categories1 = categoryMapper.topFive();
            request.getSession().setAttribute("allCategory", categories);
            request.getSession().setAttribute("topCategory", categories1);
        }
        return Result.success();
    }

    @Override
    public Result regSeller(User user, String storeName) {
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        Integer userId = 0;
        if (list.size() > 0) {
            // 存在修改角色信息
            User user1 = list.get(0);
            this.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, user1.getUserId())
            .set(User::getRole, 1));
            userId = user1.getUserId();
        }else {
            user.setRole(1);
            String password = user.getPassword();
            user.setPassword(DigestUtil.md5Hex(password));
            user.setAvatar(AVATAR);
            this.save(user);
            userId = user.getUserId();
        }
        Store store = new Store();
        store.setStoreName(storeName);
        store.setUserId(userId);
        storeMapper.insert(store);
        return Result.success();
    }

    @Override
    public Result blacked(Integer id) {
        this.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, id)
        .set(User::getDisabled, 1));
        return Result.success();
    }

    @Override
    public Result cancelBlacked(Integer id) {
        this.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, id)
                .set(User::getDisabled, 0));
        return Result.success();
    }

    @Override
    public Result getUserList(LayuiPage layuiPage, String username, String phone, Integer code) {
        Long curPage = layuiPage.getPage();
        Long pageLimit = layuiPage.getLimit();
        if (Objects.isNull(curPage)) {
            curPage = 1l;
        }
        if (Objects.isNull(pageLimit)) {
            pageLimit = 10l;
        }
        Page<User> userPage = new Page<>(curPage, pageLimit);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank(username)) {
            queryWrapper.like("username", username);
        }
        if (!StrUtil.isBlank(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (!Objects.isNull(code)) {
            queryWrapper.eq("role", code);
        }
        Page<User> page = this.page(userPage, queryWrapper);
        return Result.success(ResultEnum.SUCCESS.getMessage(), page.getRecords(), page.getTotal());
    }

    @Override
    public Result getBlackedUser(LayuiPage layuiPage, String username, String phone, Integer role) {
        Long curPage = layuiPage.getPage();
        Long pageLimit = layuiPage.getLimit();
        if (Objects.isNull(curPage)) {
            curPage = 1l;
        }
        if (Objects.isNull(pageLimit)) {
            pageLimit = 10l;
        }
        Page<User> userPage = new Page<>(curPage, pageLimit);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank(username)) {
            queryWrapper.like("username", username);
        }
        if (!StrUtil.isBlank(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (!Objects.isNull(role)) {
            queryWrapper.eq("role", role);
        }
        queryWrapper.eq("disabled", 1);
        Page<User> page = this.page(userPage, queryWrapper);
        return Result.success(ResultEnum.SUCCESS.getMessage(), page.getRecords(), page.getTotal());
    }

    @Override
    public Result create(User user) {
        user.setPassword(DigestUtil.md5Hex(PASSWORD));
        user.setAvatar(AVATAR);
        this.save(user);
        return Result.success();
    }
}
