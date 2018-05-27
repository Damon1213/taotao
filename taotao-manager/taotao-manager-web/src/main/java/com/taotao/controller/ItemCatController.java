package com.taotao.controller;

import com.taotao.common.pojo.TreeNode;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by hu on 2018-05-25.
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/list")
    @ResponseBody
    public List<TreeNode> getItemCatList(@RequestParam(value = "id",defaultValue = "0")Long parentId){
        List<TreeNode> list = itemService.getItemCatList(parentId);
        return list;
    }
}
