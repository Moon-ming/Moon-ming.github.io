package io.moomin.jdbctemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateDemo {
    public static void main(String[] args) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql:///spring");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("918111");
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(druidDataSource);
        jdbcTemplate.execute("insert into account(name,money)values('ccc',1000)");
    }
}
