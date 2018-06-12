package com.taotao.rest.service;

import com.taotao.common.utils.TaotaoResult;

/**
 * Created by hu on 2018-06-11.
 */
public interface ItemService {

//    获取商品详细信息
    TaotaoResult getItemBaseInfo(long itemId);
    //获取商品详情
    TaotaoResult getItemDescInfo(long itemId);
    //获取商品规格参数
    TaotaoResult getItemParamItemInfo(long itemId);
}
