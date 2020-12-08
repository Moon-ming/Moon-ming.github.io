package io.moomin.dao.impl;

import io.moomin.dao.AccountDao;
import io.moomin.domain.Account;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

public class AccountDaoImpl1 implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Account findAccountById(Integer accountId) {
        List<Account> query = jdbcTemplate.query("select * from account where id = ?", new BeanPropertyRowMapper<Account>(Account.class), accountId);
        return query.isEmpty() ?  null : query.get(0);
    }

    public Account findAccountByName(String accountName) {
        List<Account> query = jdbcTemplate.query("select * from account where name = ?", new BeanPropertyRowMapper<Account>(Account.class), accountName);
        if (query.isEmpty()) {
            return null;
        }
        if (query.size() > 1) {
            throw new RuntimeException("not only");
        }
        return query.get(0);
    }

    public void updateAccount(Account account) {
        int update = jdbcTemplate.update("update account set name = ?,money = ? where id=?", account.getName(), account.getMoney(), account.getId());
        System.out.println(update);
    }


    }

