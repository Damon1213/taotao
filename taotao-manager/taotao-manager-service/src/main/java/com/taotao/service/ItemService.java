package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;

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
    TaotaoResult createItem(TbItem item, TbItemDesc itemDesc,TbItemParamItem itemParamItem);
    TaotaoResult insertSolr(long itemId);
    //查询商品详情
    TbItemDesc getItemDescById(long itemId);
    //删除商品
    TaotaoResult deleteItems(List<Long> list);
    //下架商品
    TaotaoResult instock(Long[] ids);
    //上架商品
    TaotaoResult reshelf(Long[] ids);


}
