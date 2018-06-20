package com.taotao.portal.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.portal.pojo.CartItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by hu on 2018-06-14.
 */
public interface CartService {
    //添加商品到购物车
    TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response);
    //购物车展示商品
    List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response);
    //删除购物车商品
    TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response);
}
