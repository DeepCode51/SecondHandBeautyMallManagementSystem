package com.xjr.mzmall.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.GoodsListDTO;
import com.xjr.mzmall.entity.Comments;
import com.xjr.mzmall.entity.Goods;
import com.xjr.mzmall.entity.Store;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private StoreService storeService;

    @GetMapping("/blackeduser")
    public Result blackedUser(LayuiPage layuiPage,
                              @RequestParam(value = "username", required = false) String username,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "role", required = false) Integer role) {
        return userService.getBlackedUser(layuiPage, username, phone, role);
    }

    @PostMapping("/reg")
    public Result reg(User user, @RequestParam("storeName") String storeName) {
        return userService.regSeller(user, storeName);
    }

    @PostMapping("/updatev2")
    public Result updateV2(User user, @RequestParam("storeId") Integer storeId,
                           @RequestParam("storeName") String storeName, HttpServletRequest request) {
        User user1 = (User) request.getSession().getAttribute("user");
        user.setUserId(user1.getUserId());
        userService.updateById(user);
        Store store = new Store();
        store.setId(storeId);
        store.setStoreName(storeName);
        storeService.updateById(store);
        return Result.success();
    }

    @GetMapping("/getgoodstopfive")
    public Result getGoodsTopFive(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        return goodsService.getGoodsTopFive(userId);
    }

    @PostMapping("/updatepsd")
    public Result sellerUpdatePsd(@RequestParam("oldpassword") String oldPassword,
                                  @RequestParam("newpassword") String newPassword,
                                  HttpServletRequest request) {
        User user1 = (User) request.getSession().getAttribute("user");
        String oldStr = DigestUtil.md5Hex(oldPassword);
        if (!user1.getPassword().equals(oldStr)) {
            return Result.fail("原密码不正确");
        }
        String newS = DigestUtil.md5Hex(newPassword);
        User user = new User();
        user.setPassword(newS);
        user.setUserId(user1.getUserId());
        userService.updateById(user);
        request.getSession().removeAttribute("user");
        return Result.success();
    }

    @PostMapping("/update")
    public Result sellerUpdate(User user, HttpServletRequest request) {
        User user1 = (User) request.getSession().getAttribute("user");
        Integer userId = user1.getUserId();
        user.setUserId(userId);
        userService.updateById(user);
        return Result.success();
    }

    @PostMapping("/review")
    public Result CommentReview(@RequestParam("commentId") Integer commentId,
                                @RequestParam("content") String content,
                                HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Comments byId = commentsService.getById(commentId);

        Comments comments = new Comments();
        comments.setGoodsId(byId.getGoodsId());
        comments.setUserId(user.getUserId());
        comments.setUserCover(user.getAvatar());
        comments.setParentId(commentId);
        comments.setContent(content);
        commentsService.save(comments);
        return Result.success();
    }

    @GetMapping("/commentsList")
    public Result CommentsList(LayuiPage layuiPage, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return commentsService.getComments(user.getUserId(), layuiPage);
    }

    @GetMapping("/finishOrderList")
    public Result finishOrderList(@RequestParam(value = "orderId", required = false) Integer orderId,
                                  LayuiPage layuiPage,
                                  HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return orderService.getSellerOrderListFinish(orderId, user.getUserId(), layuiPage);
    }

    @PostMapping("/returnOrder/{orderId}")
    public Result returnOrder(@PathVariable Integer orderId) {
        return orderService.agreeReturnOrderApply(orderId);
    }

    @GetMapping("/returnOrderList")
    public Result returnOrderApplyList(@RequestParam(value = "orderId", required = false) Integer orderId,
                                       LayuiPage layuiPage,
                                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return orderService.getSellerOrderListReturn(orderId, user.getUserId(), layuiPage);
    }

    @PostMapping("/cancel/{orderId}")
    public Result cancelOrder(@PathVariable Integer orderId) {
        // 取消订单
        orderService.changeOrderStatus2(orderId, OrderStatusEnum.CANCELED);
        return Result.success();
    }

    @GetMapping("/cancelOrderList")
    public Result cancelOrderList(@RequestParam(value = "orderId", required = false) Integer orderId,
                                LayuiPage layuiPage,
                                HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return orderService.getSellerOrderListCancel(orderId, user.getUserId(), layuiPage);
    }

    @PostMapping("/shipments/{orderId}")
    public Result shipments(@PathVariable Integer orderId) {
        // 发货
        orderService.changeOrderStatus2(orderId, OrderStatusEnum.SHIPPED);
        return Result.success();
    }

    @GetMapping("/waitOrderList")
    public Result waitOrderList(@RequestParam(value = "orderId", required = false) Integer orderId,
                                LayuiPage layuiPage,
                                HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
       return orderService.getSellerOrderListWait(orderId, user.getUserId(), layuiPage);
    }

    @GetMapping("/allOrderList")
    public Result allOrderList(@RequestParam(value = "orderId", required = false) Integer orderId,
                               LayuiPage layuiPage,
                               HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return orderService.getSellerOrderList(orderId, user.getUserId(),layuiPage);
    }

    @PostMapping("/deleteGoods/{ids}")
    public Result deleteGoods(@PathVariable String ids) {
        return goodsService.deleteGoods(ids);
    }

    @PostMapping("/updategoods")
    public Result updateGoods(Goods goods) {
        return goodsService.updateGoods(goods);
    }

    @PostMapping("/addgoods")
    public Result addGoods(Goods goods, HttpServletRequest request) {
        return goodsService.createGoods(goods, request);
    }

    @GetMapping("/goodslist")
    public Result getGoodsList(LayuiPage layuiPage, GoodsListDTO goodsListDTO, HttpServletRequest request) {
        return goodsService.getGoodsList(layuiPage, goodsListDTO, request);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return Result.success();
    }

    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletRequest request) {
        return userService.sellerLogin(username, password, request);
    }
}
