package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;

/**
 * Created by hu on 2018-05-30.
 */
public interface ContentService {
    EUDataGridResult getContentList(int page,int rows);
    TaotaoResult insertContent(TbContent content);
}
