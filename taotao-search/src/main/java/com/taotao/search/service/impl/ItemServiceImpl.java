package com.taotao.search.service.impl;

import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;
import com.taotao.search.mapper.ItemMapper;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by hu on 2018-06-01.
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public TaotaoResult importAllItems() {

        try {
            //查询商品列表
            List<Item> list = itemMapper.getItemList();
            //把商品信息写入索引库
            for (Item item: list){
                //创建一个SolrInputDocument对象
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id", item.getId());
                document.setField("item_title", item.getTitle());
                document.setField("item_sell_point", item.getSell_point());
                document.setField("item_price", item.getPrice());
                document.setField("item_image", item.getImage());
                document.setField("item_category_name", item.getCategory_name());
//                document.setField("item_desc", item.getItem_des());
                //写入索引库
                solrServer.add(document);

            }
            //提交
            solrServer.commit();
        }catch (Exception e){
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult insertItem(long itemId) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);

        //把商品写入索引库
        if (tbItem != null) {
            try {
                TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItem.getCid());
                //创建一个SolrInputDocument对象
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id", tbItem.getId());
                document.setField("item_title", tbItem.getTitle());
                document.setField("item_sell_point", tbItem.getSellPoint());
                document.setField("item_price", tbItem.getPrice());
                document.setField("item_image", tbItem.getImage());
                document.setField("item_category_name", tbItemCat.getName());
                //写入索引库
                solrServer.add(document);
                //提交
                solrServer.commit();
            } catch (Exception e) {
                e.printStackTrace();
                return TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
            }
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteItem(long itemId) {
        try {
            solrServer.deleteById(String.valueOf(itemId));
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
        return TaotaoResult.ok();
    }
}
