package io.moomin.service;

import io.moomin.domain.PageBean;
import io.moomin.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 查询所有用户信息
     * @return
     */
    public List<User> findAll();

    /**
     * 登录方法
     * @param user
     * @return
     */
    public User login(User user);

    /**
     * 保存user
     * @param user
     */
    void addUser(User user);

    void deleteUser(String id);

    User findUserById(String id);

    void updateUser(User user);

    void delSelectedUser(String[] uids);

    //分页条件查询
    PageBean<User> findUserByPage(String currentPage, String rows, Map<String, String[]> parameterMap);
}
