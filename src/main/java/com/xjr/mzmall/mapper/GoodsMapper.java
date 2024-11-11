package com.xjr.mzmall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjr.mzmall.DO.GoodsTopFiveDo;
import com.xjr.mzmall.dto.GoodsListDTO;
import com.xjr.mzmall.entity.Goods;
import com.xjr.mzmall.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    IPage<GoodsVo> getGoodsList(@Param("page") IPage<GoodsVo> page, @Param("ew")GoodsListDTO goodsListDTO);
    IPage<GoodsVo> getGoodsList2(@Param("page") IPage<GoodsVo> page, @Param("ew")GoodsListDTO goodsListDTO);
    List<Goods> randomGetGoods(@Param("num") int num);
    Integer getGoodsCount(@Param("userId") Integer userId);
    List<GoodsTopFiveDo> getGoodsFiveDo(@Param("storeId") Integer storeId,
                                        @Param("beginTime") String beginTime,
                                        @Param("endTime") String  endTime);
    Integer getGoodsCountByGoodsId(@Param("goodsId") Integer goodsId, @Param("time") String time);
}
