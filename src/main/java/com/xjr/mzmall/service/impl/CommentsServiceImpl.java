package com.xjr.mzmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.entity.Comments;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.mapper.CommentsMapper;
import com.xjr.mzmall.mapper.OrderMapper;
import com.xjr.mzmall.service.CommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjr.mzmall.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xjr
 * @since 2023-01-23
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Override
    public Result createComments(Comments comments, Integer orderId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        String avatar = user.getAvatar();
        comments.setUserId(userId);
        comments.setUserCover(avatar);
        this.save(comments);
        // 修改订单状态
        orderMapper.changeOrderStatus(orderId, OrderStatusEnum.FINISH.getCode());
        return Result.success();
    }

    @Override
    public List<Comments> getCommentsByGoodsId(Integer goodsId) {
        List<Comments> list = commentsMapper.getCommentsByGoodsId(goodsId);
        for (Comments comments : list) {
            if (comments.getParentId() != 0) {
                for (Comments comments1 : list) {
                    if (comments1.getId() == comments.getParentId()) {
                        if (comments1.getList() == null) {
                            comments1.setList(new ArrayList<Comments>());
                        }
                        List<Comments> list1 = comments1.getList();
                        list1.add(comments);
                        comments1.setList(list1);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Result getComments(Integer userId, LayuiPage layuiPage) {
        Page<CommentVo> commentVoPage = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        commentVoPage.setOptimizeCountSql(false);
        IPage<CommentVo> comment = commentsMapper.getComment(userId, commentVoPage);
        return Result.success(comment.getRecords());
    }

    @Override
    public CommentVo getCommentVo(Integer commentsId) {
        return commentsMapper.getCommentOne(commentsId);
    }
}
