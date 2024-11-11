package com.xjr.mzmall.service;

import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Comments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xjr.mzmall.vo.CommentVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xjr
 * @since 2023-01-23
 */
public interface CommentsService extends IService<Comments> {
    Result createComments(Comments comments, Integer orderId, HttpServletRequest request);
    List<Comments> getCommentsByGoodsId(Integer goodsId);
    Result getComments(Integer userId, LayuiPage layuiPage);
    CommentVo getCommentVo(Integer commentsId);
}
