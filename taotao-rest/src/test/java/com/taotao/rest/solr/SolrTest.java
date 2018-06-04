package com.taotao.rest.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * Created by hu on 2018-06-01.
 */
public class SolrTest {

    /**
     * 添加，修改（id一样的话，先删除原来的，再添加新的）
     * @throws Exception
     */
    @Test
    public void addDocument() throws Exception{
        //创建一个连接
        SolrServer solrServer = new HttpSolrServer("http://192.168.6.43:8080/solr");
        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","test003");
        document.addField("item_title","测试商品004");
        document.addField("item_sell_point","好吃又不贵");
        document.addField("item_price",17);
        //把文档对象写入索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }

    @Test
    public void deleteDocument() throws Exception{
        //创建一个连接
        SolrServer solrServer = new HttpSolrServer("http://192.168.6.43:8080/solr");
//        solrServer.deleteById("test001");
//        List<String> list = new ArrayList();
//        list.add("test001");
//        list.add("test002");
//        solrServer.deleteById(list);
        solrServer.deleteByQuery("*:*");
        //提交
        solrServer.commit();
    }

    @Test
    public void queryDocument() throws Exception {
        //创建一个连接
        SolrServer solrServer = new HttpSolrServer("http://192.168.6.43:8080/solr");
        //创建一个查询对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery("*:*");
        query.setStart(21);
        query.setRows(100);
        //执行查询
        QueryResponse response = solrServer.query(query);
        //去查询结果
        SolrDocumentList solrDocumentList = response.getResults();
        for (SolrDocument solrDocument : solrDocumentList){
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
        }
    }
}
