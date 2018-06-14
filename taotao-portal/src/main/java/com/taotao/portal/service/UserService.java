package com.taotao.portal.service;

import com.taotao.pojo.TbUser;

/**
 * Created by hu on 2018-06-14.
 */
public interface UserService {
    TbUser getUserByToken(String token);
}
