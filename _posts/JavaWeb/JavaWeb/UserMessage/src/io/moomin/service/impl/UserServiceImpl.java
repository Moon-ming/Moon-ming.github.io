package io.moomin.service.impl;

import io.moomin.dao.UserDao;
import io.moomin.dao.impl.UserDaoImpl;
import io.moomin.domain.PageBean;
import io.moomin.domain.User;
import io.moomin.service.UserService;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();

    @Override
    public List<User> findAll() {
        //调用dao完成查询
        return dao.findAll();
    }

    @Override
    public User login(User user) {
        return dao.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Override
    public void addUser(User user) {
        dao.add(user);
    }

    @Override
    public void deleteUser(String id) {
        dao.delete(Integer.parseInt(id));
    }

    @Override
    public User findUserById(String id) {
        return dao.findById(Integer.parseInt(id));
    }

    @Override
    public void updateUser(User user) {
        dao.update(user);
    }

    @Override
    public void delSelectedUser(String[] uids) {
        if (uids != null && uids.length > 0) {
            for (String id : uids) {
                dao.delete(Integer.parseInt(id));
            }  
        }

    }

    @Override
    public PageBean<User> findUserByPage(String currentPage, String rows, Map<String, String[]> parameterMap) {
        int totalCount = dao.findTotalCount(parameterMap);


        int i = Integer.parseInt(currentPage);
        int i1 = Integer.parseInt(rows);
        //计算总页码
        int totalPage = totalCount % i1 == 0 ? totalCount / i1 : (totalCount / i1) + 1;
        if (i <= 0) {
            i = 1;
        }
        if (i >= totalPage) {
            i = totalPage;
        }
        //创建空的PageBean对象
        PageBean<User> userPageBean = new PageBean<>();
        //设置参数
        userPageBean.setCurrentPage(i);
        userPageBean.setRows(i1);
        //调用dao查询总记录数

        userPageBean.setTotalCount(totalCount);
        //调用dao查询List集合
        int start = (i - 1) * i1;
        List<User> list = dao.findByPage(start, i1,parameterMap);
        userPageBean.setList(list);


        userPageBean.setTotalPage(totalPage);

        return userPageBean;
    }
}
