package com.xjr.mzmall.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import com.xjr.mzmall.DO.CartInfoDo;
import com.xjr.mzmall.entity.ShoppingCart;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.mapper.ShoppingCartMapper;
import com.xjr.mzmall.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @Resource
    private UserMapper userMapper;

    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Test
    public void test5() {
        DateTime date = DateUtil.date();
        String endDay = DateUtil.format(date, "yyyy-MM-dd");
        DateTime dateTime = DateUtil.offsetDay(date, -7);
        String beginDay = DateUtil.format(dateTime, "yyyy-MM-dd");
        System.out.println(endDay);
        System.out.println(beginDay);
    }

    @Test
    public void test4() {
        LocalDateTime parse = LocalDateTimeUtil.parse("2023-01-23T12:23:56");
        long between = LocalDateTimeUtil.between(parse, LocalDateTime.now(), ChronoUnit.DAYS);
        System.out.println(between);
    }

    @Test
    public void test3() {
        User user = new User();
        user.setUsername("usertext");
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        user.setEmail("duahdtest@qq.com");
        user.setPhone("13863637" + RandomUtil.randomInt(100, 999));
        user.setAddress("浙江财经大学东方学院" + RandomUtil.randomInt(1, 17) + "#" + RandomUtil.randomInt(100, 999));
        user.setRole(RandomUtil.randomInt(0, 2));
        int insert = userMapper.insert(user);
        System.out.println(user);
    }

    @Test
    public void test2() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        List<CartInfoDo> cartInfoDo = shoppingCartMapper.getCartInfoDo(list);
        System.out.println(cartInfoDo);
    }

    @Test
    public void insertUser() {
        for (int i = 3; i < 54; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setPassword("e10adc3949ba59abbe56e057f20f883e");
            user.setEmail("duahd" + i + "@qq.com");
            user.setPhone("13863637" + RandomUtil.randomInt(100, 999));
            user.setAddress("浙江财经大学东方学院" + RandomUtil.randomInt(1, 17) + "#" + RandomUtil.randomInt(100, 999));
            user.setRole(RandomUtil.randomInt(0, 2));
            userMapper.insert(user);
        }
    }

    @Test
    public void test1() {
        BigDecimal bigDecimal = new BigDecimal(2);
        System.out.println(bigDecimal.add(new BigDecimal(4)));
    }
}