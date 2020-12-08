package io.moomin.jdbctemplate;

import io.moomin.dao.AccountDao;
import io.moomin.domain.Account;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/*
CRUD
 */
public class JdbcTemplateDemo3 {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("bean.xml");
        //JdbcTemplate jdbcTemplate = classPathXmlApplicationContext.getBean("jdbcTemplate", JdbcTemplate.class);
        AccountDao accountDao = classPathXmlApplicationContext.getBean("accountDao", AccountDao.class);
        Account accountById = accountDao.findAccountById(1);
        System.out.println(accountById);
        accountById.setMoney(3000f);

        accountDao.updateAccount(accountById);
    }
}
