package com.taotao.service;

import com.taotao.common.pojo.TreeNode;
import com.taotao.common.utils.TaotaoResult;

import java.util.List;

/**
 * Created by hu on 2018-05-29.
 */
public interface ContentCategoryService {
    List<TreeNode> getCategoryList(long parentId);
    TaotaoResult insertContentCategory(long parentId, String name);
    TaotaoResult deleteContentCategory(long id);
    TaotaoResult updateContentCategory(long id, String name);
}
