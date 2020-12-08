package io.moomin.jdbctemplate;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateDemo1 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = classPathXmlApplicationContext.getBean("jdbcTemplate", JdbcTemplate.class);
        jdbcTemplate.execute("insert into account(name,money)values('ddd',2222)");
        /*DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql:///spring");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("918111");
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(druidDataSource);
        jdbcTemplate.execute("insert into account(name,money)values('ccc',1000)");*/
    }
}
