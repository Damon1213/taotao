package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TreeNode;
import com.taotao.pojo.TbItem;

import java.util.List;

/**
 * Created by hu on 2018-05-20.
 */
public interface ItemService {
    TbItem getItemById(long itemId);
    //商品分页详情
    EUDataGridResult getItemList(int page,int rows);
    //商品类目详情
    List<TreeNode> getItemCatList(long parentId);

}
