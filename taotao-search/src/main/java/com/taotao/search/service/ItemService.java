package com.taotao.search.service;

import com.taotao.common.utils.TaotaoResult;

/**
 * Created by hu on 2018-06-01.
 */
public interface ItemService {
    //导入数据
    TaotaoResult importAllItems();
    //插入增加的新数据（更新）
    TaotaoResult insertItem(long itemId);
}
