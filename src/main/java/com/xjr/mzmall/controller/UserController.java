package com.xjr.mzmall.controller;


import cn.hutool.crypto.digest.DigestUtil;
import com.xjr.mzmall.common.Result;
import com.xjr.mzmall.dto.AddComplaintDTO;
import com.xjr.mzmall.dto.ReturnOrderDTO;
import com.xjr.mzmall.entity.Comments;
import com.xjr.mzmall.entity.ShoppingCart;
import com.xjr.mzmall.entity.User;
import com.xjr.mzmall.enums.OrderStatusEnum;
import com.xjr.mzmall.service.*;
import com.xjr.mzmall.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xjr
 * @since 2023-01-19
 */
@RestController
@RequestMapping("/mzmall/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/complaintOrder")
    public Result complaintOrder(AddComplaintDTO addComplaintDTO, HttpServletRequest request) {
        return complaintService.addComplaint(addComplaintDTO, request);
    }

    @PostMapping("/returnOrder")
    public Result returnOrderApply(ReturnOrderDTO returnOrderDTO) {
        return orderService.returnOrderApply(returnOrderDTO);
    }

    @PostMapping("/pubComments")
    public Result pubComments(Comments comments, @RequestParam("orderId") Integer orderId,
                              HttpServletRequest request) {
        return commentsService.createComments(comments, orderId, request);
    }

    @PostMapping("/cancelOrder/{id}")
    public Result cancelOrder(@PathVariable String id) {
        List<Integer> list = CommonUtils.StringsToList(id);
        orderService.changeOrderStatus(list.get(0), OrderStatusEnum.CANCELED);
        return Result.success();
    }

    /**
     * 确认收货
     * @param orderId
     * @return
     */
    @PostMapping("/confirmOrder/{orderId}")
    public Result confirmOrder(@PathVariable Integer orderId){
        return orderService.changeOrderStatus(orderId, OrderStatusEnum.SIGNED);
    }

    /**
     * 付款后待发货
     * @param orderIds
     * @param fre
     * @return
     */
    @PostMapping("/paymu/{orderIds}/{fre}")
    public Result payMu(@PathVariable String orderIds, @PathVariable Integer fre) {
        return orderService.changeOrderStatus(orderIds, OrderStatusEnum.TO_BE_SHIPPED, fre);
    }

    @PostMapping("/createNewOrder/{cartIds}")
    public Result createNewOrder(@PathVariable String cartIds, HttpServletRequest request) {
        return orderService.createOrderFromCart(cartIds ,request);
    }

    @PostMapping("/deleteCart/{ids}")
    public Result deleteCart(@PathVariable String ids) {
        List<Integer> list = CommonUtils.StringsToList(ids);
        shoppingCartService.removeByIds(list);
        return Result.success();
    }

    @PostMapping("/updateCartGoodsCount")
    public Result updateCartGoodsCount(@RequestParam("cartId") Integer cartId,
                                       @RequestParam("count") Integer count) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartId(cartId);
        shoppingCart.setCount(count);
        shoppingCartService.updateById(shoppingCart);
        return Result.success();
    }

    @PostMapping("/updateAddress")
    public Result updateAddress(User user, HttpServletRequest request) {
        User user1 = (User) request.getSession().getAttribute("user");
        user.setUserId(user1.getUserId());
        userService.updateById(user);
        if (!Objects.isNull(user.getName())) {
            user1.setName(user.getName());
        }
        if (!Objects.isNull(user.getPhone())) {
            user1.setPhone(user.getPhone());
        }
        if (!Objects.isNull(user.getAddress())) {
            user1.setAddress(user.getAddress());
        }
        request.getSession().setAttribute("user", user1);
        return Result.success();
    }

    @PostMapping("/updatepassword")
    public Result updatePassword(@RequestParam("oldpassword") String oldPassword,
                                 @RequestParam("newpassword") String password,
                                 HttpServletRequest request) {
        // 修改密码后需要用户重新登录
        User user1 = (User) request.getSession().getAttribute("user");
        String oldS = DigestUtil.md5Hex(oldPassword);
        if (!user1.getPassword().equals(oldS)) {
            return Result.fail("原密码错误");
        }
        String newS = DigestUtil.md5Hex(password);
        User user = new User();
        user.setUserId(user1.getUserId());
        user.setPassword(newS);
        userService.updateById(user);
        request.getSession().removeAttribute("user");
        return Result.success();
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return Result.success();
    }

    @PostMapping("/updateinfo")
    public Result updateInfo(User user, HttpServletRequest request) {
        userService.updateById(user);
        User user1 = (User) request.getSession().getAttribute("user");
        user1.setName(user.getName());
        user1.setEmail(user.getEmail());
        user1.setPhone(user.getPhone());
        request.getSession().setAttribute("user", user1);
        return Result.success();
    }

    @PostMapping("/pay/{orderId}/{orderAmount}")
    public Result payOrder(@PathVariable Integer orderId, @PathVariable BigDecimal orderAmount) {
        // 只需要修改订单的状态 为待发货状态
        return orderService.changeOrderStatus(orderId, OrderStatusEnum.TO_BE_SHIPPED, orderAmount);
    }

    @PostMapping("/createneworder/{goodId}/{count}")
    public Result createNewOrder(@PathVariable Integer goodId,
                                 @PathVariable Integer count,
                                 HttpServletRequest request) {
        return orderService.createSingleOrder(goodId, count, request);
    }

    @PostMapping("/addshoppingcart/{goodId}/{count}")
    public Result addShoppingCart(@PathVariable Integer goodId,
                                  @PathVariable Integer count,
                                  HttpServletRequest request) {
        return shoppingCartService.addShoppingCart(goodId, count, request);
    }

    @PostMapping("/login")
    public Result login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletRequest request) {
        return userService.login(username, password, request);
    }

    @PostMapping("/reg")
    public Result reg(User user) {
        return userService.regUser(user);
    }
}

