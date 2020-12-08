package io.moomin.jdbctemplate;

import io.moomin.domain.Account;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/*
CRUD
 */
public class JdbcTemplateDemo2 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = classPathXmlApplicationContext.getBean("jdbcTemplate", JdbcTemplate.class);
        //执行操作
        //保存
        //jdbcTemplate.update("insert into account(name,money)values(?,?)","eee","3333");
        //更新
        //jdbcTemplate.update("update account set name=?,money=? where id=?","test",4567,4);
        //删除
        //查询所有
       /* List<Account> accountList = jdbcTemplate.query("select * from account", new BeanPropertyRowMapper<Account>(Account.class));
        for (Account ac :
                accountList) {
            System.out.println(ac);
        }*/
        //查询一个
        //List<Account> account = jdbcTemplate.query("select * from account where id=?", new BeanPropertyRowMapper<Account>(Account.class), 9);
        //System.out.println(account.isEmpty()?"nothing":account.get(0));

        //查询返回一行一列（使用聚合函数，但不加group by 子句）
        //Integer integer = jdbcTemplate.queryForObject("select count(*) from account where money > ?", Integer.class, 2000);
        //System.out.println(integer);
    }
}
