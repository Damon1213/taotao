package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TreeNode;
import com.taotao.common.utils.*;
import com.taotao.dao.JedisClient;
import com.taotao.pojo.*;

import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.taotao.mapper.*;

import java.util.*;


/**
 * Created by hu on 2018-05-20.
 * 商品管理service
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SOLR_BASE_URL}")
    private String SOLR_BASE_URL;
    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;
    @Value("${REDIS_ITEM_EXPIRE}")
    private Integer REDIS_ITEM_EXPIRE;

    @Override
    public TbItem getItemById(long itemId) {
        //添加查询条件
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = itemMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            TbItem item = list.get(0);
            return item;
        }
        return null;
    }

    /**
     * 商品列表查询
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EUDataGridResult getItemList(int page, int rows) {
        //查询商品列表
        TbItemExample example = new TbItemExample();
        //分页处理
        PageHelper.startPage(page,rows);
        List<TbItem> list = itemMapper.selectByExample(example);
        //创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        //取记录总条数
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    /**
     * 商品类目查询
     * @param parentId
     * @return
     */
    @Override
    public List<TreeNode> getItemCatList(long parentId) {
        //根据parentId查询分类列表
        TbItemCatExample example = new TbItemCatExample();
        //设置查询条件
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        //分类列表转换成TreeNode的列表
        List<TreeNode> resultList = new ArrayList<>();
        for(TbItemCat tbItemCat: list){
            //创建一个TreeNode对象
            TreeNode node = new TreeNode(tbItemCat.getId(), tbItemCat.getName(), tbItemCat.getIsParent() ? "closed" : "open");
            resultList.add(node);
        }

        return resultList;
    }

    /**
     * 新增商品
     * @param item
     * @return
     */
    @Override
    public TaotaoResult createItem(TbItem item, TbItemDesc itemDesc, TbItemParamItem itemParamItem) {
        //生成商品id
        //使用时间+随机数策略生成
//        long itemId = IDUtils.genItemId();
//        item.setId(itemId);
        item.setStatus((byte) 1);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        //把数据插入到商品表
        itemMapper.insert(item);
        //把商品插入商品描述表
        itemDesc.setItemId(item.getId());
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        itemDescMapper.insert(itemDesc);
        //添加规格参数
        itemParamItem.setItemId(item.getId());
        itemParamItem.setCreated(date);
        itemParamItem.setUpdated(date);
        itemParamItemMapper.insert(itemParamItem);
        return TaotaoResult.ok();

    }

    @Override
    public TaotaoResult insertSolr(long itemId) {
        //插入索引库
        try {
            HttpClientUtil.doPost(SOLR_BASE_URL + "/insert/" + itemId);
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteItems(List<Long> list) {
//        //删除商品
//        TbItemExample itemExample = new TbItemExample();
//        TbItemExample.Criteria itemCriteria = itemExample.createCriteria();
//        itemCriteria.andIdIn(list);
//        itemMapper.deleteByExample(itemExample);
//        //删除商品详情
//        TbItemDescExample itemDescExample = new TbItemDescExample();
//        TbItemDescExample.Criteria itemDescCriteria = itemDescExample.createCriteria();
//        itemDescCriteria.andItemIdIn(list);
//        itemDescMapper.deleteByExample(itemDescExample);
//        //删除商品规格参数
//        TbItemParamItemExample itemParamItemExample = new TbItemParamItemExample();
//        TbItemParamItemExample.Criteria itemParamItemCriteria = itemParamItemExample.createCriteria();
//        itemParamItemCriteria.andItemIdIn(list);
//        itemParamItemMapper.deleteByExample(itemParamItemExample);

        for (Long itemId : list) {
            //删除商品
            itemMapper.deleteByPrimaryKey(itemId);
            //删除商品详情
            itemDescMapper.deleteByPrimaryKey(itemId);
            //删除商品规格参数
            TbItemParamItemExample example = new TbItemParamItemExample();
            example.createCriteria().andItemIdEqualTo(itemId);

            List<TbItemParamItem> itemParamItems = itemParamItemMapper.selectByExample(example);
            if (itemParamItems != null && itemParamItems.size() > 0) {
                TbItemParamItem itemParamItem = itemParamItems.get(0);
                itemParamItemMapper.deleteByPrimaryKey(itemParamItem.getId());
            }

            //删除索引数据
            try {
                HttpClientUtil.doPost(SOLR_BASE_URL + "/delete/" + itemId);
            }catch (Exception e){
                e.printStackTrace();
                return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
            }

            try {
                //删除redis缓存
                jedisClient.del(REDIS_ITEM_KEY + ":" + itemId + ":base");
                jedisClient.del(REDIS_ITEM_KEY + ":" + itemId + ":desc");
                jedisClient.del(REDIS_ITEM_KEY + ":" + itemId + ":param");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult instock(Long[] ids) {
        for (Long id : ids) {
            TbItem tbItem = new TbItem();
            tbItem.setId(id);
            //商品状态，1-正常，2-下架，3-删除
            tbItem.setStatus((byte) 2);
            //更新日期改变
            tbItem.setUpdated(new Date());
            itemMapper.updateByPrimaryKeySelective(tbItem);
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult reshelf(Long[] ids) {
        for (Long id : ids) {
            TbItem tbItem = new TbItem();
            tbItem.setId(id);
            //商品状态，1-正常，2-下架，3-删除
            tbItem.setStatus((byte) 1);
            //更新日期改变
            tbItem.setUpdated(new Date());
            itemMapper.updateByPrimaryKeySelective(tbItem);
        }
        return TaotaoResult.ok();
    }

    @Override
    public TbItemDesc getItemDesc(long itemId) {
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        return itemDesc;
    }

    @Override
    public TbItemParamItem getItemParamItem(long itemId) {
        TbItemParamItemExample example = new TbItemParamItemExample();
        example.createCriteria().andItemIdEqualTo(itemId);
        List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TaotaoResult updateItem(TbItem item) {
        item.setUpdated(new Date());
        itemMapper.updateByPrimaryKeySelective(item);
        try {
            //更新redis缓存,先删除原来的
            jedisClient.del(REDIS_ITEM_KEY + ":" + item.getId() + ":base");
            //把商品信息写入缓存
            jedisClient.set(REDIS_ITEM_KEY + ":" + item.getId() + ":base", JsonUtils.objectToJson(item));
            //设置key的有效期
            jedisClient.expire(REDIS_ITEM_KEY + ":" + item.getId() + ":base", REDIS_ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateItemDesc(TbItemDesc itemDesc) {
        itemDesc.setUpdated(new Date());
        itemDescMapper.updateByPrimaryKeySelective(itemDesc);
        try {
            //更新redis缓存,先删除原来的
            jedisClient.del(REDIS_ITEM_KEY + ":" + itemDesc.getItemId() + ":desc");
            //把商品信息写入缓存
            jedisClient.set(REDIS_ITEM_KEY + ":" + itemDesc.getItemId() + ":desc", JsonUtils.objectToJson(itemDesc));
            //设置key的有效期
            jedisClient.expire(REDIS_ITEM_KEY + ":" + itemDesc.getItemId() + ":desc", REDIS_ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateItemParamItem(TbItemParamItem itemParamItem) {
        itemParamItem.setUpdated(new Date());
        itemParamItemMapper.updateByPrimaryKeySelective(itemParamItem);
        try {
            //更新redis缓存,先删除原来的
            jedisClient.del(REDIS_ITEM_KEY + ":" + itemParamItem.getItemId() + ":param");
            //把商品信息写入缓存
            jedisClient.set(REDIS_ITEM_KEY + ":" + itemParamItem.getItemId() + ":param", JsonUtils.objectToJson(itemParamItem));
            //设置key的有效期
            jedisClient.expire(REDIS_ITEM_KEY + ":" + itemParamItem.getItemId() + ":param", REDIS_ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }
}
