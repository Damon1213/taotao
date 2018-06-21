package com.taotao.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.dao.JedisClient;
import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by hu on 2018-06-21.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_INIT_ID}")
    private String ORDER_INIT_ID;
    @Value("${ORDER_DETAIL_GEN_KEY}")
    private String ORDER_DETAIL_GEN_KEY;

    /**
     * 创建订单
     * @param order 订单信息
     * @param itemList  订单商品列表
     * @param orderShipping 订单物流信息
     * @return
     */
    @Override
    public TaotaoResult createOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping) {
        //向订单表中插入记录
        //获取订单号，通过redis自增获取id
        String s = jedisClient.get(ORDER_GEN_KEY);
        if (StringUtils.isBlank(s)) {
            jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
        }
        long orderId = jedisClient.incr(ORDER_GEN_KEY);
        //补全pojo的属性
        order.setOrderId(orderId + "");
        //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        order.setStatus(1);
        Date date = new Date();
        order.setCreateTime(date);
        //0：未评价 1：已评价
        order.setBuyerRate(0);
        //向订单表中插入数据
        orderMapper.insertSelective(order);

        //插入订单明细
        for (TbOrderItem orderItem : itemList) {
            //补全订单明细
            //取订单明细id
            long orderDetailId = jedisClient.incr(ORDER_DETAIL_GEN_KEY);
            orderItem.setId(orderDetailId + "");
            orderItem.setOrderId(orderId + "");
            //向订单明细插入记录
            orderItemMapper.insertSelective(orderItem);
        }

        //插入物流表
        //补全物流信息
        orderShipping.setOrderId(orderId + "");
        orderShipping.setCreated(date);
        orderShippingMapper.insertSelective(orderShipping);

        return TaotaoResult.ok(orderId);
    }

    /**
     * 根据订单ID查询订单
     * @param orderId 订单ID
     * @return
     */
    @Override
    public TaotaoResult selectOrderById(long orderId) {
        //查出订单
        TbOrder order = orderMapper.selectByPrimaryKey(orderId + "");
        //该商品是否存在
        if (StringUtils.isBlank(order.getOrderId())) {
            return TaotaoResult.build(500, "没有查询到该订单");
        }
        //查出订单商品列表
        TbOrderItemExample example = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId + "");
        List<TbOrderItem> orderItemList = orderItemMapper.selectByExample(example);
        //查询物流信息
        TbOrderShipping orderShipping = orderShippingMapper.selectByPrimaryKey(orderId + "");
        //封装信息到order
        Order newOrder = new Order();
        newOrder.setOrderId(orderId + "");
        newOrder.setPayment(order.getPayment());
        newOrder.setPaymentType(order.getPaymentType());
        newOrder.setStatus(order.getStatus());
        newOrder.setCreateTime(order.getCreateTime());
        newOrder.setPostFee(order.getPostFee());
        newOrder.setUserId(order.getUserId());
        newOrder.setBuyerMessage(order.getBuyerMessage());
        newOrder.setBuyerNick(order.getBuyerNick());
        newOrder.setOrderItems(orderItemList);
        newOrder.setOrderShipping(orderShipping);

        return TaotaoResult.ok(newOrder);
    }

    /**
     * 根据用户分页查询订单
     * @param userID 用户ID
     * @param page 第几页
     * @param count 每页条数
     * @return
     */
    @Override
    public TaotaoResult selectOrdersByPage(long userID, Integer page, Integer count) {
        TbOrderExample example = new TbOrderExample();
        TbOrderExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userID);
        //分页处理
        PageHelper.startPage(page,count);
        List<TbOrder> orderList = orderMapper.selectByExample(example);
        return TaotaoResult.ok(orderList);
    }

    /**
     * 修改订单状态
     * @param orderId 订单id
     * @param status 订单状态
     * @param paymentTime 付款时间
     * @return
     */
    @Override
    public TaotaoResult updateStatus(long orderId, int status, Date paymentTime) {
        TbOrder order = orderMapper.selectByPrimaryKey(orderId + "");
        if (null == order) {
            return TaotaoResult.build(500,"该订单不能存在");
        }
        order.setStatus(2);
        order.setPaymentTime(paymentTime);
        orderMapper.updateByPrimaryKeySelective(order);
        return TaotaoResult.ok();
    }
}
