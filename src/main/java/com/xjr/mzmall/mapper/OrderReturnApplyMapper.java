package com.xjr.mzmall.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.DO.ReturnApplyDo;
import com.xjr.mzmall.entity.OrderReturnApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xjr
 * @since 2023-02-09
 */
@Mapper
public interface OrderReturnApplyMapper extends BaseMapper<OrderReturnApply> {
    List<ReturnApplyDo> getReturnApplyDo(@Param("page")Page<ReturnApplyDo> page,
                                         @Param("orderId") Integer orderId,
                                         @Param("storeId") Integer storeId);
}
