package com.taotao.order.dao.impl;


import com.taotao.order.dao.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by hu on 2018-05-31.
 */
public class JedisClientSingle implements JedisClient {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String s = jedis.get(key);
        jedis.close();
        return s;
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String s = jedis.set(key, value);
        jedis.close();
        return s;
    }

    @Override
    public String hget(String hkey, String key) {
        Jedis jedis = jedisPool.getResource();
        String s = jedis.hget(hkey, key);
        jedis.close();
        return s;
    }

    @Override
    public long hset(String hkey, String key, String value) {
        Jedis jedis = jedisPool.getResource();
        Long aLong = jedis.hset(hkey, key, value);
        jedis.close();
        return aLong;
    }

    @Override
    public long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long aLong = jedis.incr(key);
        jedis.close();
        return aLong;
    }

    @Override
    public long expire(String key, long second) {
        Jedis jedis = jedisPool.getResource();
        Long aLong = jedis.expire(key, (int) second);
        jedis.close();
        return aLong;
    }

    @Override
    public long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        Long aLong = jedis.ttl(key);
        jedis.close();
        return aLong;
    }

    @Override
    public long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long aLong = jedis.del(key);
        jedis.close();
        return aLong;
    }

    @Override
    public long hdel(String hkey, String key) {
        Jedis jedis = jedisPool.getResource();
        Long aLong = jedis.hdel(hkey, key);
        jedis.close();
        return aLong;
    }
}
