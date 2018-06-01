package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_CONTENT_SYNC_URL}")
    private String REST_CONTENT_SYNC_URL;

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

        //添加缓存同步逻辑
        try {
            HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());
        }catch (Exception e){
            e.printStackTrace();
        }

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateContent(TbContent content) {
        //补全pojo内容
        content.setUpdated(new Date());
        contentMapper.updateByPrimaryKeySelective(content);

        //添加缓存同步逻辑
        try {
            HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());
        }catch (Exception e){
            e.printStackTrace();
        }

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContents(List<Long> ids) {
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        //查询出需要删除的content
        List<TbContent> list = contentMapper.selectByExample(example);
        //然后再删除
        contentMapper.deleteByExample(example);


        //添加缓存同步逻辑
        try {
            for (TbContent content: list) {
                HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }
}
