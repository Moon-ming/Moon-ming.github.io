package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        //1.1获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2使用sortedset排序
        //Set<String> categorys = jedis.zrange("categorys", 0, -1);
        //1.3查询sortedset中的分数(cid)和值(cname)
        Set<Tuple> categorys = jedis.zrangeWithScores("categorys", 0, -1);
        List<Category> all = null;
        //2.判断查询的集合是否为空
        if (categorys == null || categorys.size() == 0) {
            System.out.println("search in sql...");
            //3.如果为空,需要从数据库查询,在将数据存入redis
            //3.1从数据库查询
            all = categoryDao.findAll();
            //3.2将集合数据存储到redis中
            for (int i = 0; i < all.size(); i++) {
                jedis.zadd("categorys", all.get(i).getCid(), all.get(i).getCname());
            }
        } else {
            System.out.println("search in redis...");
            //4.如果不为空,将set的数据存入list
            all = new ArrayList<Category>();
            for (Tuple tuple :
                    categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                all.add(category);
            }

        }
        return all;

    }
}
