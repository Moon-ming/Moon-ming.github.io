package io.moomin;

import io.moomin.dao.RoleDao;
import io.moomin.dao.UserDao;
import io.moomin.domain.Role;
import io.moomin.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:appliactionContext.xml")
public class ManyToManyTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    /*
    * 保存一个用户，保存一个角色
    * 多对多放弃维护权，被动的一方放弃
    * */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testAdd() {
        User user = new User();
        user.setUserName("moomin");

        Role role = new Role();
        role.setRoleName("dsasd");

        user.getRoles().add(role);
        role.getUsers().add(user);

        userDao.save(user);
        roleDao.save(role);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void testCascadeAdd() {
        User user = new User();
        user.setUserName("moomin1");

        Role role = new Role();
        role.setRoleName("dsasd1");

        user.getRoles().add(role);
        role.getUsers().add(user);

        userDao.save(user);

    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void testCascadeRemove() {
        User one = userDao.findOne(4l);
        userDao.delete(one);

    }
}
