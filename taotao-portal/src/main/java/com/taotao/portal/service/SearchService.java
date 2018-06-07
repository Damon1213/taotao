package com.taotao.portal.service;

import com.taotao.portal.pojo.SearchResult;

/**
 * Created by hu on 2018-06-07.
 */
public interface SearchService {

    SearchResult search(String queryString, int page);
}
