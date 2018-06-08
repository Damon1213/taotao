package com.taotao.controller;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hu on 2018-05-20.
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    /*@RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getitemById(@PathVariable Long itemId){
        TbItem tbItem = itemService.getItemById(itemId);
        return tbItem;
    }*/

    @RequestMapping("/item/list")
    @ResponseBody
    public EUDataGridResult getItemList(int page,int rows){
        EUDataGridResult result = itemService.getItemList(page, rows);
        return result;
    }

    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createItem(TbItem item,String desc,String itemParams){
        //生成商品id
        //使用时间+随机数策略生成
        long itemId = IDUtils.genItemId();
        item.setId(itemId);
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setParamData(itemParams);
        TaotaoResult result = itemService.createItem(item,itemDesc,itemParamItem);
        //插入solr索引库
        itemService.insertSolr(itemId);
        return result;
    }

    @RequestMapping(value = "/rest/item/delete")
    @ResponseBody
    public TaotaoResult deleteItem(Long[] ids) {
        List<Long> list = Arrays.asList(ids);
        TaotaoResult result = itemService.deleteItems(list);
        return result;
    }

    @RequestMapping(value = "/rest/item/instock")
    @ResponseBody
    public TaotaoResult instock(Long[] ids) {
        TaotaoResult result = itemService.instock(ids);
        return result;
    }

    @RequestMapping(value = "/rest/item/reshelf")
    @ResponseBody
    public TaotaoResult reshelf(Long[] ids) {
        TaotaoResult result = itemService.reshelf(ids);
        return result;
    }
}
