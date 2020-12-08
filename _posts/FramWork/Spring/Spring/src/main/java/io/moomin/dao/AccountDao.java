package io.moomin.dao;

import io.moomin.domain.Account;

public interface AccountDao {
    Account findAccountById(Integer accountId);

    Account findAccountByName(String accountName);

    void updateAccount(Account account);
}
