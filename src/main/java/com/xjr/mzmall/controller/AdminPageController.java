package com.xjr.mzmall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjr.mzmall.entity.Category;
import com.xjr.mzmall.entity.Goods;
import com.xjr.mzmall.entity.MzOrder;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.service.*;
import com.xjr.mzmall.utils.CommonUtils;
import com.xjr.mzmall.vo.OrderDetailVo;
import com.xjr.mzmall.vo.StoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/login")
    public String toLogin() {
        return "admin/login";
    }

    @GetMapping("/index.html")
    public String toIndex() {
        return "admin/index";
    }

    @GetMapping("/welcome.html")
    public String toWelcome(Model model) {
        int userCount = userService.count();
        int storeCount = storeService.count();
        int allOrderCount = orderService.count();
        int finishOrderCount = orderService
                .count(new LambdaQueryWrapper<MzOrder>()
                        .eq(MzOrder::getOrderStatus, OrderStatusEnum.FINISH));
        model.addAttribute("userCount", userCount);
        model.addAttribute("storeCount", storeCount);
        model.addAttribute("allOrderCount", allOrderCount);
        model.addAttribute("finishOrderCount", finishOrderCount);
        return "admin/welcome";
    }

    @GetMapping("/userlist.html")
    public String toUserList() {
        return "admin/userlist";
    }

    @GetMapping("/useradd.html")
    public String toUserAdd() {
        return "admin/useradd";
    }

    @GetMapping("/touserupdate.html")
    public String toUserUpdate(@RequestParam("userid") Integer id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "admin/userupdate";
    }

    @GetMapping("/categorylist.html")
    public String toCategoryList() {
        return "admin/categorylist";
    }

    @GetMapping("/categoryadd.html")
    public String toCategoryAdd() {
        return "admin/categoryadd";
    }

    @GetMapping("/tocategoryupdate.html")
    public String toCategoryUpdate(@RequestParam("categoryid") String id, Model model) {
        Category category = categoryService.getById(id);
        model.addAttribute("category", category);
        return "admin/categoryupdate";
    }

    @GetMapping("/orderlist.html")
    public String toOrderList() {
        return "admin/allorderlist";
    }

    @GetMapping("/orderdetail.html")
    public String toOrderDetail(@RequestParam("orderId") Integer orderId, Model model) {
        OrderDetailVo orderDetailVo = orderService.getOrderDetailVo(orderId);
        model.addAttribute("orderDetailVo", orderDetailVo);
        return "admin/orderdetail";
    }

    @GetMapping("/storelist.html")
    public String toStoreList() {
        return "admin/storelist";
    }

    @GetMapping("/storedetail.html")
    public String toStoreDetail(@RequestParam("storeId") Integer storeId, Model model) {
        StoreVo storeVo = storeService.getStoreVo(storeId);
        BigDecimal totalAmount = storeService.getTotalAmount(storeId);
        storeVo.setTotalAmount(totalAmount);
        model.addAttribute("storeVo", storeVo);
        return "admin/storedetail";
    }

    @GetMapping("/usersetting.html")
    public String toUserSetting() {
        return "admin/usersetting";
    }

    @GetMapping("/userpassword.html")
    public String toUserPassword() {
        return "admin/userpassword";
    }

    @GetMapping("/goodslist.html")
    public String toGoodsList(Model model) {
        List<Category> list = categoryService.list();
        model.addAttribute("categoryList", list);
        return "admin/goods";
    }

    @GetMapping("/togoodsdetail.html")
    public String toGoodsDetailList(@RequestParam("goodsid") Integer goodId, Model model) {
        List<Category> list = categoryService.list();
        Goods goods = goodsService.getById(goodId);
        String detailImg = goods.getDetailImg();
        List<String> imgList = CommonUtils.splitDetailImg(detailImg);
        goods.setDetailImgList(imgList);
        model.addAttribute("goods", goods);
        model.addAttribute("categoryList", list);
        return "admin/goodsdetail";
    }

    @GetMapping("/complaintorderapplylist.html")
    public String toComplaintOrderApplyList(){
        return "admin/complaintorderapplylist";
    }
}
