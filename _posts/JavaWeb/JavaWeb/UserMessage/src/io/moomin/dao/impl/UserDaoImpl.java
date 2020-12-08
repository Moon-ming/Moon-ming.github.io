package io.moomin.dao.impl;

import io.moomin.dao.UserDao;
import io.moomin.domain.User;
import io.moomin.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<User> findAll() {
        //使用JDBC操作数据库
        //定义sql
        String sql = "select * from user";
        List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));
        return users;
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {

        try {
            String sql = "select * from user where username = ? and password = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void add(User user) {
        String sql = "insert into user values(null,?,?,?,?,?,?,null,null)";
        template.update(sql, user.getName(), user.getGender(), user.getAge(), user.getAddress(), user.getQq(), user.getEmail());
    }

    @Override
    public void delete(int parseInt) {
        String sql = "delete from user where id = ?";
        template.update(sql, parseInt);
    }

    @Override
    public User findById(int parseInt) {
        String sql = "select * from user where id = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), parseInt);
    }

    @Override
    public void update(User user) {
        String sql = "update user set name = ? , gender = ?,age = ?, address = ?,qq = ? , email = ? where id = ?";
        template.update(sql, user.getName(), user.getGender(), user.getAge(), user.getAddress(), user.getQq(), user.getEmail(), user.getId());
    }

    @Override
    public int findTotalCount(Map<String, String[]> parameterMap) {
        //定义模版初始化sql
        String sql = "select count(*) from user where 1 = 1 ";
        StringBuilder stringBuilder = new StringBuilder(sql);
        //遍历map
        Set<String> keySet = parameterMap.keySet();
        //定义参数的集合
        List<Object> objects = new ArrayList<>();
        for (String s :
                keySet) {
            //排除分页条件参数
            if ("currentPage".equals(s) || "rows".equals(s)) {
                continue;
            }
            String strings = parameterMap.get(s)[0];
            //判断value是否有值
            if (strings != null && "" != (strings)) {
                stringBuilder.append(" and " + s + " like ?");
                objects.add("%"+strings+"%"); //?条件的值
            }

        }

        return template.queryForObject(stringBuilder.toString(), Integer.class,objects.toArray());
    }

    @Override
    public List<User> findByPage(int start, int i1, Map<String, String[]> parameterMap) {
        //定义模版初始化sql
        String sql = "select * from user where 1 = 1 ";
        StringBuilder stringBuilder = new StringBuilder(sql);
        //遍历map
        Set<String> keySet = parameterMap.keySet();
        //定义参数的集合
        List<Object> objects = new ArrayList<>();
        for (String s :
                keySet) {
            //排除分页条件参数
            if ("currentPage".equals(s) || "rows".equals(s)) {
                continue;
            }
            String strings = parameterMap.get(s)[0];
            //判断value是否有值
            if (strings != null && "" != (strings)) {
                stringBuilder.append(" and " + s + " like ?");
                objects.add("%"+strings+"%"); //?条件的值
            }
        }
        //添加分页查询
        stringBuilder.append(" limit ?,? ");
        //添加分页查询参数值
        objects.add(start);
        objects.add(i1);

        return template.query(stringBuilder.toString(), new BeanPropertyRowMapper<User>(User.class), objects.toArray());
    }


}
