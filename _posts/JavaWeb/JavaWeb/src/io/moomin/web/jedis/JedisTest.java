package io.moomin.web.jedis;

import io.moomin.web.utils.JedistPoolUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisTest {
    @Test
    public void test1() {
        Jedis jedis = new Jedis();
        //String
        jedis.set("username", "zhangsan");
        jedis.setex("activecode", 20, "hehe");
        //hash
        jedis.hset("user", "name", "lisi");
        jedis.hset("user", "age", "23");
        jedis.hset("user", "gender", "male");
        Map<String, String> user = jedis.hgetAll("user");
        Set<String> keySet = user.keySet();
        for (String key :
                keySet) {
            String s = user.get(key);
            System.out.println(key + ":" + s);
        }
        //List
        jedis.lpush("mylist", "a", "b", "c");
        jedis.rpush("mylist", "a", "b", "c");
        List<String> mylist = jedis.lrange("mylist", 0, -1);
        System.out.println(mylist);
        String mylist1 = jedis.lpop("mylist");
        String mylist2 = jedis.rpop("mylist");
        //set
        jedis.sadd("myset", "java", "php,", "c++");
        Set<String> myset = jedis.smembers("myset");
        System.out.println(myset);
        //sortedSet
        jedis.zadd("mysorted", 3, "亚索");
        jedis.zadd("mysorted", 50, "安妮");
        Set<String> mysorted = jedis.zrange("mysorted", 0, -1);
        System.out.println(mysorted);
        jedis.close();

    }

    @Test
    public void testJedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(50);
        jedisPoolConfig.setMaxIdle(10);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "localhost", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("hehhe", "hahah");
        jedis.close();
    }
    @Test
    public void testJedisUtils() {
        Jedis jedis = JedistPoolUtils.getJedis();
        jedis.set("hello", "jedisPoolUtils");
        jedis.close();
    }
}
