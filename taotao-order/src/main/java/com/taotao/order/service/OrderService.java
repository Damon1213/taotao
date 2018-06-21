package com.taotao.order.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

import java.util.Date;
import java.util.List;

/**
 * Created by hu on 2018-06-21.
 */
public interface OrderService {

    TaotaoResult createOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping);

    TaotaoResult selectOrderById(long orderId);

    TaotaoResult selectOrdersByPage(long userID, Integer page, Integer count);

    TaotaoResult updateStatus(long orderId, int status, Date paymentTime);
}
