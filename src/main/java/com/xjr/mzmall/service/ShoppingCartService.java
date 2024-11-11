package com.xjr.mzmall.service;

import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjr.mzmall.vo.CartVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-01-20
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    Result addShoppingCart(Integer goodId, Integer count, HttpServletRequest request);
    List<CartVo> getCartInfo(Integer userId);
}
