package com.xjr.mzmall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjr.mzmall.entity.Comments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjr.mzmall.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xjr
 * @since 2023-01-23
 */
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {
    List<Comments> getCommentsByGoodsId(@Param("goodsId") Integer goodsId);
    IPage<CommentVo> getComment(@Param("userId") Integer userId, @Param("page") IPage<CommentVo> page);
    CommentVo getCommentOne(@Param("id") Integer commentId);
}
