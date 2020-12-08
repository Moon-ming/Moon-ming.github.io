package io.moomin.test;

import io.moomin.dao.AccountDao;
import io.moomin.domain.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestMybatis {
    @Test
    public void run1() throws IOException {
        //加载配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //创建SqlSessionFactory对象
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        //创建SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //获取代理对象
        AccountDao mapper = sqlSession.getMapper(AccountDao.class);
        //查询所有数据
        List<Account> all = mapper.findAll();
        for (Account a :
                all) {
            System.out.println(a);
        }
        //关闭资源
        sqlSession.close();
        in.close();

    }
    @Test
    public void run2() throws IOException {
        Account account = new Account();
        account.setName("十大");
        account.setMoney(400d);
        //加载配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //创建SqlSessionFactory对象
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        //创建SqlSession对象
        SqlSession sqlSession = factory.openSession();
        //获取代理对象
        AccountDao mapper = sqlSession.getMapper(AccountDao.class);
        //保存
        mapper.saveAccount(account);
        //提交事务
        sqlSession.commit();
        //关闭资源
        sqlSession.close();
        in.close();

    }
}
