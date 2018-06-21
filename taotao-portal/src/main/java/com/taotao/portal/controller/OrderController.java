package com.taotao.portal.controller;

import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.OrderService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by hu on 2018-06-21.
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order-cart")
    public String showOrderCart(HttpServletRequest request, HttpServletResponse response, Model model) {
        //取购物车商品列表
        List<CartItem> cartItemList = cartService.getCartItemList(request, response);
        //传递给页面
        model.addAttribute("cartList", cartItemList);
        return "order-cart";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createOrder(Order order, Model model) {
        try {
            String orderId = orderService.createOrder(order);
            if (orderId.trim().length() > 0) {
                model.addAttribute("orderId", orderId);
                model.addAttribute("payment", order.getPayment());
                model.addAttribute("date", new DateTime().plusDays(3).toString("yyyy-MM-dd"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

}
