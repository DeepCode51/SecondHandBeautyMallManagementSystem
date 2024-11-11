package com.xjr.mzmall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjr.mzmall.entity.Category;
import com.xjr.mzmall.entity.Comments;
import com.xjr.mzmall.entity.Goods;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.GoodsStatusEnum;
import com.xjr.mzmall.service.*;
import com.xjr.mzmall.utils.CommonUtils;
import com.xjr.mzmall.vo.*;
import com.xjr.mzmall.DO.OrderDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/mzmall")
public class FrontPageController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CommentsService commentsService;

    @GetMapping("/myprod.html")
    public String roMyProd(HttpServletRequest request, Model model) {
        // 查找待评价商品
        User user = (User) request.getSession().getAttribute("user");
        List<CommentsGoodsVo> commentsGoodsVo = orderService.getCommentsGoodsVo(user.getUserId());
        model.addAttribute("commentsGoodsVo", commentsGoodsVo);
        return "buyer/myprod";
    }

    @GetMapping("/orderxq.html")
    public String toOrderDetail(@RequestParam("orderId") Integer orderId, Model model) {
        OrderDetailVo orderDetailVo = orderService.getOrderDetailVo(orderId);
        model.addAttribute("orderDetailVo", orderDetailVo);
        return "buyer/orderxq";
    }

    @GetMapping("/myorder")
    public String toMyOrder(@RequestParam(value = "orderstatus", required = false) Integer orderStatus,
                            HttpServletRequest request,
                            Model model) {
        List<MyOrderVo> myOrderVo = orderService.getMyOrderVo(orderStatus, request);
        model.addAttribute("myOrderVo", myOrderVo);
        return "buyer/myorderq";
    }

    @GetMapping("/topay/{orderIds}")
    public String toPay(@PathVariable String orderIds, Model model) {
        MulOrderVo melOrderVo = orderService.getMelOrderVo(orderIds);
        model.addAttribute("melOrderVo", melOrderVo);
        return "buyer/order2";
    }

    @GetMapping("/tocart")
    public String toCart(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<CartVo> cartInfo = shoppingCartService.getCartInfo(user.getUserId());
        model.addAttribute("cartInfo", cartInfo);
        return "buyer/cart";
    }

    @GetMapping("/address.html")
    public String toAddress() {
        return "buyer/address";
    }

    @GetMapping("/toupdatepassword")
    public String toUpdatePassword() {
        return "buyer/updatepassword";
    }

    @GetMapping("/touserinfo")
    public String toUserInfo() {
        return "buyer/mygrxx";
    }

    @GetMapping("/search")
    public String searchGoods(@RequestParam("searchKey") String searchKey, Model model) {
        List<Goods> goodsList = goodsService.list(new LambdaQueryWrapper<Goods>()
                 .like(Goods::getGoodsName, searchKey)
                 .eq(Goods::getStatus, GoodsStatusEnum.SHELVES.getCode()));
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("searchKey", searchKey);
        return "buyer/proList";
    }

    @GetMapping("/took")
    public String toOkPage(Model model) {
        List<List<Goods>> guessYouWillLikeGoods = goodsService.getGuessYouWillLikeGoods(10, 2);
        model.addAttribute("guessYouWillLikeGoods", guessYouWillLikeGoods);
        return "buyer/ok";
    }

    @GetMapping("/topaypage/{orderId}")
    public String toPayPage(@PathVariable Integer orderId,
                            Model model) {
        // 需要页面展示的内容  订单的详情 总价
        OrderDo orderInfoForPay = orderService.getOrderInfoForPay(orderId);
        model.addAttribute("orderVo", orderInfoForPay);
        return "buyer/order";
    }

    @GetMapping("/detail")
    public String toGoodsDetailPage(@RequestParam("goodsid") Integer goodsId, Model model) {
        Goods goods = goodsService.getById(goodsId);
        Category category = categoryService.getById(goods.getCategoryId());
        String detailImg = goods.getDetailImg();
        List<String> imgList = CommonUtils.splitDetailImg(detailImg);
        goods.setDetailImgList(imgList);
        // 4个为你推荐 10个猜你喜欢 商品的评价
        List<List<Goods>> recommendGoods = goodsService.getRecommendGoods(4, 1);
        List<List<Goods>> guessYouWillLikeGoods = goodsService.getGuessYouWillLikeGoods(10, 2);
        // 获取评论
        List<Comments> comments = commentsService.getCommentsByGoodsId(goodsId);
        model.addAttribute("recommendGoods", recommendGoods);
        model.addAttribute("guessYouWillLikeGoods", guessYouWillLikeGoods);
        model.addAttribute("goods", goods);
        model.addAttribute("category", category);
        model.addAttribute("comments", comments);
        return "buyer/proDetail";
    }

    @GetMapping("/searchbycategory/{id}")
    public String toCategoryGoodsPage(@PathVariable Integer id, Model model) {
        List<Goods> list = goodsService.list(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getCategoryId, id)
                .eq(Goods::getStatus, GoodsStatusEnum.SHELVES.getCode()));
        Category category = categoryService.getById(id);
        model.addAttribute("category", category);
        model.addAttribute("goodsList", list);
        return "buyer/categorygoodslist";
    }

    @GetMapping(value = {"index", "index.html"})
    public String toIndex(Model model, HttpServletRequest request) {
        if (Objects.isNull(request.getSession().getAttribute("allCategory"))) {
            List<Category> categories = categoryService.list();
            List<Category> categories1 = categoryService.topFive();
            request.getSession().setAttribute("allCategory", categories);
            request.getSession().setAttribute("topCategory", categories1);
        }
        List<List<Goods>> recommendGoods = goodsService.getRecommendGoods(16, 4);
        List<List<Goods>> guessYouWillLikeGoods = goodsService.getGuessYouWillLikeGoods(16, 4);
        model.addAttribute("recommendGoods", recommendGoods);
        model.addAttribute("guessYouWillLikeGoods", guessYouWillLikeGoods);
        return "buyer/index";
    }

    @GetMapping(value = {"/", "login", "login.html"})
    public String toLogin() {
        return "buyer/login";
    }

    @GetMapping("/reg.html")
    public String toReg() {
        return "buyer/reg";
    }
}
