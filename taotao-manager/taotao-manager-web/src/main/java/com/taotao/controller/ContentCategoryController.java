package com.taotao.controller;

import com.taotao.common.pojo.TreeNode;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by hu on 2018-05-29.
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("list")
    @ResponseBody
    public List<TreeNode> getContentCatList(@RequestParam(value = "id", defaultValue = "0")Long parentId){
        List<TreeNode> list = contentCategoryService.getCategoryList(parentId);
        return list;
    }
}
