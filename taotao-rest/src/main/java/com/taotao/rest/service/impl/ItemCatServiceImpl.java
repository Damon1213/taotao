package com.taotao.rest.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 2018-05-29.
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${INDEX_ITEM_CAT_REDIS_KEY}")
    private String INDEX_ITEM_CAT_REDIS_KEY;

    @Override
    public CatResult getItemCatList() {
        CatResult catResult = new CatResult();
        //从缓存中取
        try {
            String result = jedisClient.hget(INDEX_ITEM_CAT_REDIS_KEY, 0 + "");
            //判断字符串是否为空
            if (!StringUtils.isBlank(result)){
                //字符串转换成pojo
                CatResult pojo = JsonUtils.jsonToPojo(result, CatResult.class);
                return pojo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        //查询分类列表
        List<?> list = getCatList(0);
        catResult.setData(list);

        //写入缓存
        try {
            //把list转换成字符串
            String json = JsonUtils.objectToJson(catResult);
            jedisClient.hset(INDEX_ITEM_CAT_REDIS_KEY,0 + "",json);
        }catch (Exception e){
            e.printStackTrace();
        }

        return catResult;
    }

    /**
     * 查询分类列表
     * @param parentId
     * @return
     */
    private List<?> getCatList(long parentId){
        //创建查询条件
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询,返回list
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        //向list中添加节点
        List resultList = new ArrayList<>();
        int count = 0;
        for (TbItemCat itemCat: list){
            //判断是否为父节点
            if (itemCat.getIsParent()) {
                CatNode catNode = new CatNode();
                if (parentId == 0) {
                    catNode.setName("<a href='/products/" + itemCat.getId() + ".html'>" + itemCat.getName() + "</a>");
                } else {
                    catNode.setName(itemCat.getName());
                }
                catNode.setUrl("/products/" + itemCat.getId() + ".html");
                catNode.setItem(getCatList(itemCat.getId()));
                resultList.add(catNode);
                count++;
                //第一层只取14条记录
                if (parentId == 0 && count >= 14){
                    break;
                }
            }else { //如果是叶子节点
                resultList.add("/products/"+itemCat.getId()+".html|"+itemCat.getName());
            }
        }

        return resultList;
    }
}
