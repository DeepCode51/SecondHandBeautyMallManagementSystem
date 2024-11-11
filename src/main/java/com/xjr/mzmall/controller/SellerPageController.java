package com.xjr.mzmall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjr.mzmall.entity.*;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.service.*;
import com.xjr.mzmall.utils.CommonUtils;
import com.xjr.mzmall.vo.CommentVo;
import com.xjr.mzmall.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/seller")
public class SellerPageController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private StoreService storeService;

    @GetMapping("/blackuserlist.html")
    public String blackUserList() {
        return "seller/userlist";
    }

    @GetMapping("/refuseorder.html")
    public String toRefuseorder(@RequestParam("orderId") Integer orderId, Model model){
        model.addAttribute("orderId", orderId);
        return "seller/refuseorder";
    }

    @GetMapping(value = {"/reg.html", "reg"})
    public String toRegPage() {
        return "seller/reg";
    }

    @GetMapping("/usersetting.html")
    public String toUserSetting(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        Store store = storeService.getOne(new LambdaQueryWrapper<Store>().eq(Store::getUserId, userId));
        model.addAttribute("store", store);
        return "seller/usersetting";
    }

    @GetMapping("/userpassword.html")
    public String toUserPassword() {
        return "seller/userpassword";
    }

    @GetMapping("/commentreview.html")
    public String toCommentReview(@RequestParam("commentId") Integer commentId, Model model) {
        model.addAttribute("commentId", commentId);
        return "seller/commentsreview";
    }

    @GetMapping("/commentsdetail.html")
    public String toCommentsDetail(@RequestParam("commentId") Integer commentsId,
                                   Model model) {
        CommentVo commentVo = commentsService.getCommentVo(commentsId);
        model.addAttribute("commentVo", commentVo);
        return "seller/commentsdetail";
    }

    @GetMapping("/commentslist.html")
    public String toCommentsList() {
        return "seller/commentslist";
    }

    @GetMapping("/finishorderlist.html")
    public String toFinishOrderList() {
        return "seller/finishorderlist";
    }

    @GetMapping("/returnapplylist.html")
    public String toReturnApplyList() {
        return "seller/returnorderapplylist";
    }

    @GetMapping("/cancelapplylist.html")
    public String toCancelApplyList() {
        return "seller/cancelorderlist";
    }

    @GetMapping("/waitorderlist.html")
    public String toWaitOrderList() {
        return "seller/waitorderlist";
    }

    @GetMapping("/orderdetail.html")
    public String toOrderDetail(@RequestParam("orderId") Integer orderId, Model model) {
        OrderDetailVo orderDetailVo = orderService.getOrderDetailVo(orderId);
        model.addAttribute("orderDetailVo", orderDetailVo);
        return "seller/orderdetail";
    }

    @GetMapping("/allorderlist.html")
    public String toAllOrderList() {
        return "seller/allorderlist";
    }

    @GetMapping("/togoodsupdate.html")
    public String toGoodsUpdate(@RequestParam("goodsid") Integer goodId, Model model) {
        List<Category> list = categoryService.list();
        Goods goods = goodsService.getById(goodId);
        String detailImg = goods.getDetailImg();
        List<String> imgList = CommonUtils.splitDetailImg(detailImg);
        goods.setDetailImgList(imgList);
        model.addAttribute("goods", goods);
        model.addAttribute("categoryList", list);
        return "seller/goodsupdate";
    }

    @GetMapping("/goodsadd.html")
    public String toGoodsAdd(Model model) {
        List<Category> list = categoryService.list();
        model.addAttribute("categoryList", list);
        return "seller/goodsadd";
    }

    @GetMapping("/goodslist.html")
    public String toGoodsList(Model model) {
        List<Category> list = categoryService.list();
        model.addAttribute("categoryList", list);
        return "seller/goods";
    }

    @GetMapping("/welcome.html")
    public String toWelcome(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        Integer userId = user.getUserId();
        Store store = storeService.getOne(new LambdaQueryWrapper<Store>().eq(Store::getUserId, userId));
        Integer storeId = store.getId();
        Integer goodsCount = goodsService.goodsCount(userId);
        int allOrderCount =
                orderService
                        .count(new LambdaQueryWrapper<MzOrder>()
                                .eq(MzOrder::getStoreId, storeId));
        int finishOrderCount =
                orderService
                        .count(new LambdaQueryWrapper<MzOrder>()
                                .eq(MzOrder::getStoreId, storeId)
                                .eq(MzOrder::getOrderStatus, OrderStatusEnum.FINISH.getCode()));
        BigDecimal totalAmount = orderService.getTotalAmount(storeId);
        model.addAttribute("goodsCount", goodsCount);
        model.addAttribute("allOrderCount", allOrderCount);
        model.addAttribute("finishOrderCount", finishOrderCount);
        model.addAttribute("totalAmount", totalAmount);
        return "seller/welcome";
    }

    @GetMapping(value = {"/index", "/index.html"})
    public String toIndex() {
        return "seller/index";
    }

    @GetMapping(value = {"/login", "/login.html"})
    public String toLogin() {
        return "seller/login";
    }
}
