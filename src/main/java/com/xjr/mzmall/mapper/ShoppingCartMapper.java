package com.xjr.mzmall.mapper;

import com.xjr.mzmall.DO.CartInfoDo;
import com.xjr.mzmall.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjr.mzmall.vo.CartVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xjr
 * @since 2023-01-20
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    List<CartVo> getCartInfo(@Param("userId") Integer userId);
    List<CartInfoDo> getCartInfoDo(List<Integer> list);
}
