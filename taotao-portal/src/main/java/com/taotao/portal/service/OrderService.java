package com.taotao.portal.service;

import com.taotao.portal.pojo.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hu on 2018-06-21.
 */
public interface OrderService {

    String createOrder(Order order, HttpServletRequest request, HttpServletResponse response);
}
