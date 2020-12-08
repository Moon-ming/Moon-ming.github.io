package io.moomin.dao.impl;

import io.moomin.dao.ProvinceDao;
import io.moomin.domain.Province;
import io.moomin.web.utils.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ProvInceDaoImpl implements ProvinceDao {
    //声明成员变量 jdbctemplement
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Province> findAll() {
        //定义sql
        String sql = "select * from province";
        //执行sql
        List<Province> list = template.query(sql, new BeanPropertyRowMapper<Province>(Province.class));
        return list;
    }
}
