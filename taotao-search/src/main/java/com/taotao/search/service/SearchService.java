package com.taotao.search.service;

import com.taotao.search.pojo.SearchResult;

/**
 * Created by hu on 2018-06-04.
 */
public interface SearchService {
    SearchResult search(String queryString, int page, int rows) throws Exception;
}
