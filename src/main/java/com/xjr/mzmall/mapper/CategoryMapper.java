package com.xjr.mzmall.mapper;

import com.xjr.mzmall.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> topFive();
}
