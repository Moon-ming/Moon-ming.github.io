package io.moomin.test;

import io.moomin.dao.UserDao;
import io.moomin.domain.User;
import org.junit.Test;

public class UserDaoTest {

    @Test
    public void testLogin() {
        User loginuser = new User();
        loginuser.setUsername("zhangsan");
        loginuser.setPassword("123");
        UserDao dao = new UserDao();
        User login = dao.login(loginuser);
        System.out.println(login);

    }
}
