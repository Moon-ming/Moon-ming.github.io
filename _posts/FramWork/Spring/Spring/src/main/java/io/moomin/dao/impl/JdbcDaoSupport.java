package io.moomin.dao.impl;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/*public class JdbcDaoSupport {
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setDataSource(DruidDataSource dataSource) {
        if (jdbcTemplate == null) {
            jdbcTemplate = createJdbcTemplate(dataSource);
        }
    }


    private JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}*/
