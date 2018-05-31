package com.taotao.service.impl;

import com.taotao.common.pojo.TreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hu on 2018-05-29.
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<TreeNode> getCategoryList(long parentId) {
        //根据parentId查询节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        List<TreeNode> resultList = new ArrayList<>();
        for (TbContentCategory category: list){
            //创建一个节点
            TreeNode node = new TreeNode();
            node.setId(category.getId());
            node.setText(category.getName());
            node.setState(category.getIsParent()?"closed":"open");
            resultList.add(node);
        }
        return resultList;
    }

    @Override
    public TaotaoResult insertContentCategory(long parentId, String name) {
        TbContentCategory category = new TbContentCategory();
        category.setIsParent(false);
        category.setName(name);
        Date date = new Date();
        category.setCreated(date);
        category.setUpdated(date);
        //'状态。可选值:1(正常),2(删除)',
        category.setStatus(1);
        category.setParentId(parentId);
        category.setSortOrder(1);
        //插入记录
        contentCategoryMapper.insert(category);
        //查看父节点isParent()是true或者false,false修改为true
        TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parentCategory.getIsParent()){
            parentCategory.setIsParent(true);
            //更新父节点
            contentCategoryMapper.updateByPrimaryKey(parentCategory);
        }

        return TaotaoResult.ok(category);
    }

    @Override
    public TaotaoResult deleteContentCategory(long id) {
        //获取要删除的节点
        TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
        //判断是否为父节点
        if (category.getIsParent()){
            //获取所有该节点下的子节点
            List<TbContentCategory> list = getChildNodeList(category.getId());
            //删除所有子节点
            for (TbContentCategory contentCategory: list){
                //递归删除
                deleteContentCategory(contentCategory.getId());
            }

        }
        //判断父节点下是否还有其他子节点
        if (getChildNodeList(category.getParentId()).size() == 1){
            //没有其他子节点将父节点标记为叶子叶子节点
            TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(category.getParentId());
            parentCategory.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKey(parentCategory);
        }
        //删除本节点
        contentCategoryMapper.deleteByPrimaryKey(id);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateContentCategory(long id, String name) {
        TbContentCategory category = new TbContentCategory();
        category.setId(id);
        category.setName(name);
        category.setUpdated(new Date());
        contentCategoryMapper.updateByPrimaryKeySelective(category);
        return TaotaoResult.ok();
    }

    /**
     * 获取该节点下的所有子节点
     * @param parentId 父节点id
     * @return
     */
    private List<TbContentCategory> getChildNodeList(long parentId){
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //返回符合要求的节点
        return contentCategoryMapper.selectByExample(example);
    }
}
