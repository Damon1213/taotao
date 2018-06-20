package com.taotao.portal.service.impl;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 2018-06-14.
 */
@Service
public class CartServiceImpl implements CartService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${ITEM_INFO_URL}")
    private String ITEM_INFO_URL;

    @Override
    public TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        //取商品信息
        CartItem cartItemNew = null;
        //查找购物车是否有该商品
        List<CartItem> list = getCartItems(request);
        //判断购物车商品列表中是否存在此商品
        if (null != list && list.size() > 0) {
            for (CartItem cartItem : list) {
                //如果存在，则数量加num
                if (cartItem.getId() == itemId) {
                    cartItem.setNum(cartItem.getNum() + num);
                    cartItemNew = cartItem;
                    break;
                }
            }
        }
        if (null == cartItemNew) {
            cartItemNew = new CartItem();
            //根据商品id查找商品信息
            String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
            //把json转换成java对象
            TaotaoResult result = TaotaoResult.formatToPojo(json, TbItem.class);
            if (result.getStatus() == 200) {
                TbItem item = (TbItem) result.getData();
                cartItemNew.setId(itemId);
                cartItemNew.setNum(num);
                cartItemNew.setPrice(item.getPrice());
                cartItemNew.setTitle(item.getTitle());
                cartItemNew.setImage(item.getImage() == null ? "" : item.getImage().split(",")[0]);
            }
            //添加到购物车
            list.add(cartItemNew);
        }
        //把购物车列表写入cookie
        CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
        return TaotaoResult.ok();
    }

    /**
     * 从cookie中取商品列表
     * @param request
     * @return
     */
    public List<CartItem> getCartItems(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookieValue(request, "TT_CART", true);
        try {
            if (null == cookieValue) {
                return new ArrayList<CartItem>();
            }
            //把cookieValue转换成商品列表
            List<CartItem> list = JsonUtils.jsonToList(cookieValue, CartItem.class);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<CartItem>();
    }

    @Override
    public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
        List<CartItem> list = getCartItems(request);
        return list;
    }

    @Override
    public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {
        //取出购物车所有商品
        String cookieValue = CookieUtils.getCookieValue(request, "TT_CART", true);
        //把cookieValue转换成商品列表
        List<CartItem> list = JsonUtils.jsonToList(cookieValue, CartItem.class);
        for (CartItem cartItem : list) {
            if (cartItem.getId() == itemId) {
                //移除需要删除的购物车商品
                list.remove(cartItem);
                break;
            }
        }
        //把购物车列表重新写入cookie
        CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
        return TaotaoResult.ok();
    }
}
