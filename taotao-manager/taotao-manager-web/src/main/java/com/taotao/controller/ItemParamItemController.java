package com.taotao.controller;

import com.taotao.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hu on 2018-05-28.
 */
@Controller
public class ItemParamItemController {

    @Autowired
    private ItemParamItemService itemParamItemService;

    @RequestMapping("/item/{itemId}")
    public String showItemParam(@PathVariable Long itemId, Model model){
        String s = itemParamItemService.getItemParamByItemId(itemId);
        model.addAttribute("itemParam",s);
        return "item";
    }
}
