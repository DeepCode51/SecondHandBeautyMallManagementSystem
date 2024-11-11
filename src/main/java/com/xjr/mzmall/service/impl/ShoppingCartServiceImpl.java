package com.xjr.mzmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Goods;
import com.xjr.mzmall.entity.ShoppingCart;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.mapper.GoodsMapper;
import com.xjr.mzmall.mapper.ShoppingCartMapper;
import com.xjr.mzmall.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjr.mzmall.vo.CartVo;
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
 * @since 2023-01-20
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Result addShoppingCart(Integer goodId, Integer count, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        ShoppingCart one = this.getOne(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId)
                .eq(ShoppingCart::getGoodsId, goodId));
        if (!Objects.isNull(one)) {
            return Result.fail("该商品已在购物车中，请勿重复添加");
        }
        Goods goods = goodsMapper.selectById(goodId);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCart.setGoodsId(goodId);
        shoppingCart.setCount(count);
        shoppingCart.setStoreId(goods.getStoreId());
        this.save(shoppingCart);
        return Result.success();
    }

    @Override
    public List<CartVo> getCartInfo(Integer userId) {
        // 获取在购物车中并未下架的商品
        return shoppingCartMapper.getCartInfo(userId);
    }
}
