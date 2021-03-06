package com.taotao.search.controller;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.search.service.ItemService;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Created by hu on 2018-06-04.
 */
@Controller
@RequestMapping("/manager")
public class ItemController {

    @Autowired
    private ItemService itemService;


    /**
     * 导入商品数据到索引库
     * @return
     */
    @RequestMapping("/imortall")
    @ResponseBody
    public TaotaoResult importAllItems(){
        TaotaoResult result = itemService.importAllItems();
        return result;
    }

    /**
     * 插入或者更新新商品到索引库
     * @param itemId 商品id
     * @return
     */
    @RequestMapping("/insert/{itemId}")
    @ResponseBody
    public TaotaoResult insertItem(@PathVariable Long itemId) {
        TaotaoResult result = itemService.insertItem(itemId);
        return result;
    }

    /**
     * 删除商品
     * @return
     */
    @RequestMapping(value = "/delete/{itemId}", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult delete(@PathVariable Long itemId) {
//        System.out.println(itemId);
        itemService.deleteItem(itemId);
        return TaotaoResult.ok();
    }
}
