package com.taotao.portal.controller;

import com.taotao.pojo.TbItemDesc;
import com.taotao.portal.pojo.ItemInfo;
import com.taotao.portal.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hu on 2018-06-12.
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItem(@PathVariable Long itemId, Model model) {
        ItemInfo item = itemService.getItemById(itemId);
        model.addAttribute("item", item);

        return "item";
    }

    @RequestMapping("/item/desc/{itemId}")
    @ResponseBody
    public String getItemDesc(@PathVariable Long itemId) {
        TbItemDesc itemDesc = itemService.getItemDesc(itemId);
        return itemDesc.getItemDesc();
    }

    @RequestMapping(value = "/item/param/{itemId}", produces= MediaType.TEXT_HTML_VALUE+";charset=utf-8")
    @ResponseBody
    public String getItemParamItem(@PathVariable Long itemId) {
        String param = itemService.getItemParamItem(itemId);
        return param;
    }

}
