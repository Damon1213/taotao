package com.taotao.dao;

/**
 * Created by hu on 2018-05-31.
 */
public interface JedisClient {

    String get(String key);
    String set(String key, String value);
    //hash存取
    String hget(String hkey, String key);
    long hset(String hkey, String key, String value);
    //将key中储存的数字值增1
    long incr(String key);
    //设置过期时间
    long expire(String key, long second);
    //查询数据剩余时间
    long ttl(String key);
    long del(String key);
    long hdel(String hkey, String key);
}
