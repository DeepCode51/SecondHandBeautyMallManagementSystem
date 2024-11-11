package com.xjr.mzmall.mapper;

import com.xjr.mzmall.DO.StoreTopDo;
import com.xjr.mzmall.entity.Store;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjr.mzmall.vo.StoreVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
public interface StoreMapper extends BaseMapper<Store> {
    StoreVo getStoreVo(@Param("storeId") Integer storeId);
    List<StoreTopDo> getTopFive(@Param("beginDay") String beginDay, @Param("endDay") String endDay);
    Integer getCountByStore(@Param("storeId") Integer storeId, @Param("time") String time);
    BigDecimal getTotalAmount(@Param("storeId") Integer storeId);
}
