package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by hu on 2018-05-30.
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public EUDataGridResult getContentList(int page, int rows) {
        TbContentExample example = new TbContentExample();
        //分页处理
        PageHelper.startPage(page,rows);
        List<TbContent> list = contentMapper.selectByExample(example);
        //创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        //取记录总条数
        PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public TaotaoResult insertContent(TbContent content) {
        //补全pojo内容
        Date date = new Date();
        content.setCreated(date);
        content.setUpdated(date);
        contentMapper.insert(content);

        return TaotaoResult.ok();
    }

}
