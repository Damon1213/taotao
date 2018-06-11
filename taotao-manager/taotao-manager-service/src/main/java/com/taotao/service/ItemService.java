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
    //删除商品
    TaotaoResult deleteItems(List<Long> list);
    //下架商品
    TaotaoResult instock(Long[] ids);
    //上架商品
    TaotaoResult reshelf(Long[] ids);
    //单个商品详情
    TbItemDesc getItemDesc(long itemId);
    //商品规格
    TbItemParamItem getItemParamItem(long itemId);
    //修改商品
    TaotaoResult updateItem(TbItem item);
    //修改商品详情
    TaotaoResult updateItemDesc(TbItemDesc itemDesc);
    //修改商品规格参数
    TaotaoResult updateItemParamItem(TbItemParamItem itemParamItem);


}
