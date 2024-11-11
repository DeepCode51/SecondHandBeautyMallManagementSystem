package com.xjr.mzmall.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.DO.ComplaintOrderDo;
import com.xjr.mzmall.entity.Complaint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xjr
 * @since 2023-02-11
 */
@Mapper
public interface ComplaintMapper extends BaseMapper<Complaint> {
    Page<ComplaintOrderDo> getComplaintList(@Param("page")Page<ComplaintOrderDo> page, @Param("orderId") Integer orderId);
}
