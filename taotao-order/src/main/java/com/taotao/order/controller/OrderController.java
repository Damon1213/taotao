package com.taotao.order.controller;

import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by hu on 2018-06-21.
 */
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createOrder(@RequestBody Order order) {
        try {
            TaotaoResult result = orderService.createOrder(order, order.getOrderItems(), order.getOrderShipping());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/info/{orderId}")
    @ResponseBody
    public TaotaoResult selectOrderById(@PathVariable Long orderId) {
        try {
            TaotaoResult result = orderService.selectOrderById(orderId);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/list/{userID}/{page}/{count}")
    @ResponseBody
    public TaotaoResult selectOrdersByPage(@PathVariable Long userID,
                                           @PathVariable Integer page,
                                           @PathVariable Integer count) {
        try {
            TaotaoResult result = orderService.selectOrdersByPage(userID, page, count);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateStatus(Long orderId, Integer status) {
        try {
            TaotaoResult result = orderService.updateStatus(orderId, status, new Date());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

}
