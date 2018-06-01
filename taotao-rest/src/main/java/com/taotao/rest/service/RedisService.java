package com.taotao.rest.service;

import com.taotao.common.utils.TaotaoResult;

/**
 * redis缓存同步
 * Created by hu on 2018-06-01.
 */
public interface RedisService {
    TaotaoResult syncContent(long contentCid);
}
