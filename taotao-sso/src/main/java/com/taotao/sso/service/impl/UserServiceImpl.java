package com.taotao.sso.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by hu on 2018-06-12.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;
    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;
    @Value("${SSO_SESSION_EXPIRE}")
    private Long SSO_SESSION_EXPIRE;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public TaotaoResult check(String param, Long type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        }else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        }else {
            criteria.andEmailEqualTo(param);
        }
        List<TbUser> list = userMapper.selectByExample(example);
        if (list != null && list.size() >0) {
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult register(TbUser user) {
        Date date = new Date();
        user.setCreated(date);
        user.setUpdated(date);
        //md5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        try {
            userMapper.insertSelective(user);
            return TaotaoResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.build(400,"注册失败，请检验数据后再提交");
    }

    @Override
    public TaotaoResult login(String username, String password) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(example);
        if (null == list || list.size() == 0) {
            return TaotaoResult.build(400,"用户名或密码错误");
        }
        TbUser user = list.get(0);
        //比对密码
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5DigestAsHex.equals(user.getPassword())) {
            return TaotaoResult.build(400,"用户名或密码错误");
        }
        //生成token
        String token = UUID.randomUUID().toString();
        //保存用户之前，把用户对象中的密码清空
        user.setPassword(null);
        //把用户信息写入redis
        jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(user));
        //设置session的过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);

        //返回token
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        //根据token从redis中查询用户信息
        String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
        //判断是否为空
        if (StringUtils.isBlank(json)) {
            return TaotaoResult.build(400, "此session已经过期，请重新登录");
        }
        //更新过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
        //返回用户信息
        return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));

    }

    @Override
    public TaotaoResult logout(String token) {
        //根据token从redis中查询用户信息
        String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
        //判断是否为空
        if (StringUtils.isBlank(json)) {
            return TaotaoResult.ok();
        }
        //删除
        jedisClient.del(REDIS_USER_SESSION_KEY + ":" + token);
        return TaotaoResult.ok();
    }
}
