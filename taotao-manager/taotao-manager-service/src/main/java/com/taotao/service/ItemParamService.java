package com.taotao.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItemParam;

/**
 * Created by hu on 2018-05-28.
 */
public interface ItemParamService {

    TaotaoResult getItemParamByCid(long id);
    TaotaoResult insertItemParam(TbItemParam itemParam);
}
