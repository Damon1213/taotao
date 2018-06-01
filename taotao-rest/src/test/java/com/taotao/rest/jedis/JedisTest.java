package com.taotao.rest.jedis;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;

/**
 * Created by hu on 2018-05-31.
 */
public class JedisTest {
    /**
     * 单机版
     */
    @Test
    public void testJedisSingle(){
        //创建一个jedis的对象
        Jedis jedis = new Jedis("192.168.6.43",6379);
        //调用jedis对象的方法，方法名称和redis的命令一致
        jedis.set("key1","jedis test");
        String string = jedis.get("key1");
        System.out.println(string);
        //关闭jedis
        jedis.close();
    }

    /**
     * 使用连接池
     */
    @Test
    public void testJedisPool(){
        //创建jedis连接池
        JedisPool pool = new JedisPool("192.168.6.43",6379);
        //从连接池获取jedis对象
        Jedis resource = pool.getResource();
        String string = resource.get("key1");
        System.out.println(string);
        //关闭jedis对象
        resource.close();
        //关闭连接池
        pool.close();
    }

    /**
     * 集群版测试
     */
    @Test
    public void testJedisCluster(){
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.6.43",7001));
        nodes.add(new HostAndPort("192.168.6.43",7002));
        nodes.add(new HostAndPort("192.168.6.43",7003));
        nodes.add(new HostAndPort("192.168.6.43",7004));
        nodes.add(new HostAndPort("192.168.6.43",7005));
        nodes.add(new HostAndPort("192.168.6.43",7006));

        //自带连接池
        JedisCluster cluster = new JedisCluster(nodes);

        cluster.set("key1","hello world!!!");
        String s = cluster.get("key1");
        System.out.println(s);

        cluster.close();
    }

    /**
     * spring单机版测试
     */
    @Test
    public void testSpringJedisSingle(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisPool pool = (JedisPool) applicationContext.getBean("redisClient");
        Jedis jedis = pool.getResource();
        String s = jedis.get("key1");
        System.out.println(s);
        jedis.close();
        pool.close();
    }

    @Test
    public void testSpringJedisCluster(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JedisCluster cluster = (JedisCluster) applicationContext.getBean("redisClient");
        String s = cluster.get("key1");
        System.out.println(s);
        cluster.close();
    }
}
