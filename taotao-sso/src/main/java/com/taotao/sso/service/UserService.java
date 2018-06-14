package com.taotao.sso.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hu on 2018-06-12.
 */
public interface UserService {

    TaotaoResult check(String param, Long type);

    TaotaoResult register(TbUser user);

    TaotaoResult login(String username, String password, HttpServletRequest request, HttpServletResponse response);

    TaotaoResult getUserByToken(String token);

    TaotaoResult logout(String token, HttpServletRequest request, HttpServletResponse response);
}
