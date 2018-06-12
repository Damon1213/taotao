package com.taotao.portal.service;

import com.taotao.pojo.TbItemDesc;
import com.taotao.portal.pojo.ItemInfo;

/**
 * Created by hu on 2018-06-12.
 */
public interface ItemService {
    //获取商品基本信息
    ItemInfo getItemById(long itemId);
    //获取商品详情
    TbItemDesc getItemDesc(long itemId);
    //获取商品规格参数
    String getItemParamItem(long itemId);

}
