package io.moomin.dao;

import io.moomin.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 用户操作的dao
 */
public interface UserDao {
    public List<User> findAll();

    public User findUserByUsernameAndPassword(String username, String password);

    void add(User user);


    void delete(int parseInt);

    User findById(int parseInt);

    void update(User user);

    int findTotalCount(Map<String, String[]> parameterMap);

    List<User> findByPage(int start, int i1, Map<String, String[]> parameterMap);
}
