package com.taotao.search.dao;

import com.taotao.search.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;

/**
 * Created by hu on 2018-06-04.
 */
public interface SearchDao {
    SearchResult search(SolrQuery query) throws Exception;
}
