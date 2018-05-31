package com.taotao.rest.service;

import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * Created by hu on 2018-05-31.
 */
public interface ContentService {
    List<TbContent> getContentListByCid(long contentCategoryId);
}
