package io.moomin.dao.impl;

import com.alibaba.druid.pool.DruidDataSource;
import io.moomin.dao.AccountDao;
import io.moomin.domain.Account;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {


    public Account findAccountById(Integer accountId) {
        List<Account> query = getJdbcTemplate().query("select * from account where id = ?", new BeanPropertyRowMapper<Account>(Account.class), accountId);
        return query.isEmpty() ?  null : query.get(0);
    }

    public Account findAccountByName(String accountName) {
        List<Account> query = getJdbcTemplate().query("select * from account where name = ?", new BeanPropertyRowMapper<Account>(Account.class), accountName);
        if (query.isEmpty()) {
            return null;
        }
        if (query.size() > 1) {
            throw new RuntimeException("not only");
        }
        return query.get(0);
    }

    public void updateAccount(Account account) {
        int update = getJdbcTemplate().update("update account set name = ?,money = ? where id=?", account.getName(), account.getMoney(), account.getId());
        System.out.println(update);
    }


    }

