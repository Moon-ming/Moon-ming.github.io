package io.moomin.test;

import io.moomin.dao.UserDao;
import io.moomin.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class userTest {
    @Test
    public void testuser() throws IOException {
        //获取字节输入流
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //根据字节输入流构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        //根据SqlSessionFactory生产一个SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //使用SqlSession获取Dao的代理对象
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        //执行Dao方法
        List<User> all = userDao.findAll();
        for (User user :
                all) {
            System.out.println(user);
        }
        //释放资源
        sqlSession.close();
        in.close();
    }
}
