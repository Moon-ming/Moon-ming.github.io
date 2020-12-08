package io.moomin.web.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JedistPoolUtils {
    private static JedisPool jedisPool;
    static{
        InputStream is = JedistPoolUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
        jedisPoolConfig.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));
        jedisPool = new JedisPool(jedisPoolConfig, properties.getProperty("host"), Integer.parseInt(properties.getProperty("port")));

    }
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}