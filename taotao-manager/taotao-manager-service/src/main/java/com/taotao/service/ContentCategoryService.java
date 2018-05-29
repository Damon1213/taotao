package com.taotao.service;

import com.taotao.common.pojo.TreeNode;

import java.util.List;

/**
 * Created by hu on 2018-05-29.
 */
public interface ContentCategoryService {
    List<TreeNode> getCategoryList(long parentId);
}
