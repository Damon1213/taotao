package com.taotao.controller;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setParamData(itemParams);
        TaotaoResult result = itemService.createItem(item,itemDesc,itemParamItem);
        return result;
    }
}
