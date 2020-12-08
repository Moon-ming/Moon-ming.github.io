package io.moomin.dao;

import io.moomin.domain.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDao {

    //查询所有
    @Select("select * from account")
    public List<Account> findAll();

    //保存用户信息
    @Insert("insert into account (name,money) values (#{name},#{money})")
    public void saveAccount(Account account);
}
