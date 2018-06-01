package com.taotao.controller;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hu on 2018-05-31.
 */
@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/query/list")
    @ResponseBody
    public EUDataGridResult getContentList(int page,int rows){
        EUDataGridResult result = contentService.getContentList(page, rows);
        return result;
    }

    @RequestMapping("/save")
    @ResponseBody
    public TaotaoResult insertContent(TbContent content){
        TaotaoResult result = contentService.insertContent(content);
        return result;
    }

    @RequestMapping("/edit")
    @ResponseBody
    public TaotaoResult updateContent(TbContent content){
        TaotaoResult result = contentService.updateContent(content);
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult deleteContents(Long[] ids){
        List<Long> list = Arrays.asList(ids);
        TaotaoResult result = contentService.deleteContents(list);
        return result;
    }

}
