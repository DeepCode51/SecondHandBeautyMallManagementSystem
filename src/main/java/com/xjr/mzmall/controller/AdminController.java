package com.xjr.mzmall.controller;


import com.xjr.mzmall.common.LayuiPage;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.GoodsListDTO;
import com.xjr.mzmall.entity.Admin;
import com.xjr.mzmall.entity.Category;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xjr
 * @since 2023-01-18
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/blackedUser/{id}")
    public Result blackedUser(@PathVariable Integer id) {
        return userService.blacked(id);
    }

    @PostMapping("/cancelBlackedUser/{id}")
    public Result cancelBlackedUser(@PathVariable Integer id) {
        return userService.cancelBlacked(id);
    }

    @PostMapping("/blackedStore/{id}")
    public Result blackedStore(@PathVariable Integer id) {
        return storeService.blacked(id);
    }

    @PostMapping("/cancelBlackedStore/{id}")
    public Result cancelBlackedStore(@PathVariable Integer id) {
        return storeService.cancelBlacked(id);
    }

    @PostMapping("/refuseComplaint/{id}")
    public Result refuseComplaint(@PathVariable Integer id) {
        return complaintService.refuseComplaint(id);
    }

    @PostMapping("/agreeComplaint/{id}")
    public Result agreeComplaint(@PathVariable Integer id) {
       return complaintService.agreeComplaint(id);
    }

    @GetMapping("/complaintOrderList")
    public Result getComplaintOrderList(LayuiPage layuiPage, Integer orderId) {
        return complaintService.getComplaintList(layuiPage, orderId);
    }

    @GetMapping("/goodslist")
    public Result getGoodsList(LayuiPage layuiPage, GoodsListDTO goodsListDTO) {
        return goodsService.getGoodsList(layuiPage, goodsListDTO);
    }

    @GetMapping("/storetopfive")
    public Result getStoreTop() {
        return storeService.getTopFive();
    }

    @PostMapping("/updatepsd")
    public Result updatePsd(@RequestParam("oldpassword") String oldPassword,
                            @RequestParam("newpassword") String newPassword,
                            HttpServletRequest request) {
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        Integer id = admin.getId();
        return adminService.updatePsd(oldPassword, newPassword, id);
    }

    @PostMapping("/update")
    public Result update(Admin admin, HttpServletRequest request) {
        Admin admin1 = (Admin) request.getSession().getAttribute("admin");
        admin.setId(admin1.getId());
        adminService.updateById(admin);
        return Result.success();
    }

    @GetMapping("/allStoreList")
    public Result getStoreList(@RequestParam(value = "storeName", required = false) String storeName,
                               LayuiPage layuiPage) {
        return storeService.getStoreList(storeName, layuiPage);
    }

    @PostMapping("/returnOrder/{orderId}")
    public Result returnOrder(@PathVariable Integer orderId) {
        orderService.changeOrderStatus2(orderId, OrderStatusEnum.CHARGEBACKS);
        return Result.success();
    }

    @PostMapping("/cancelOrder/{orderId}")
    public Result cancelOrder(@PathVariable Integer orderId) {
        orderService.changeOrderStatus(orderId, OrderStatusEnum.CANCELED);
        return Result.success();
    }

    @GetMapping("/allOrderList")
    public Result getAllOrder(@RequestParam(value = "orderId", required = false) Integer orderId,
                              LayuiPage layuiPage) {
       return orderService.getAllOrder(orderId, layuiPage);
    }

    @PostMapping("/deleteCategory/{ids}")
    public Result deleteCategory(@PathVariable String ids) {
        return categoryService.deleteByIds(ids);
    }

    @PostMapping("/updateCategory")
    public Result updateCategory(Category category) {
        return categoryService.updateCategory(category);
    }

    @PostMapping("/createCategory")
    public Result createCategory(Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping("/categorylist")
    public Result getCategoryList(LayuiPage layuiPage,
                                  @RequestParam(value = "categoryName", required = false) String categoryName) {
        return categoryService.getCategoryList(layuiPage, categoryName);
    }

    @PostMapping("/deleteUser/{ids}")
    public Result deleteUser(@PathVariable String ids) {
        return userService.deleteByIds(ids);
    }

    @GetMapping("/getUser/{userId}")
    public Result getUser(@PathVariable Integer userId) {
       return userService.getUser(userId);
    }

    @PostMapping("/updateUser")
    public Result updateUser(User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/createUser")
    public Result createUser(User user) {
        return userService.regUser(user);
    }

    @GetMapping("/userlist")
    public Result getUserList(LayuiPage layuiPage,
                              @RequestParam(value = "username", required = false) String username,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "role", required = false) Integer role) {
        return userService.getUserList(layuiPage, username, phone, role);
    }

    @PostMapping("/login")
    public Result login(Admin admin, @RequestParam("captcha") String captcha, HttpServletRequest request) {
        return adminService.login(admin, captcha, request);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("admin");
        return Result.success();
    }

}
