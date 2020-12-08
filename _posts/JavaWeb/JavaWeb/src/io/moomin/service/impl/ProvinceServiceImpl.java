package io.moomin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.moomin.dao.ProvinceDao;
import io.moomin.dao.impl.ProvInceDaoImpl;
import io.moomin.domain.Province;
import io.moomin.service.ProvinceService;
import io.moomin.web.utils.JedistPoolUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

public class ProvinceServiceImpl implements ProvinceService {
     ProvinceDao dao = new ProvInceDaoImpl();
    @Override
    public List<Province> findAll() {

        return dao.findAll();
    }
    /*
    使用redis缓存
     */
    @Override
    public String findAllJson() {
        //从redis中查询数据
        //获取redis客户端连接
        Jedis jedis = JedistPoolUtils.getJedis();
        String province = jedis.get("province");
        //判断数据是否为null
        if (province == null || province.length() == 0) {
            //从数据库中查询
            System.out.println("Redis中没有数据，查询数据库");
            //从数据中查询
            List<Province> ps = dao.findAll();
            //将list序列化为json
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                province = objectMapper.writeValueAsString(ps);
                System.out.println(province);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //将json数据存入redis
            jedis.set("province", province);
            //归还连接
            jedis.close();
        }else {
            System.out.println("redis中有数据,查询缓存...");
            System.out.println(province);
        }
        return province;
    }
}
