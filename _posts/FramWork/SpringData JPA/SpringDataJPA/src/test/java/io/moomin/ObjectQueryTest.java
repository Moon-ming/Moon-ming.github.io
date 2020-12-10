package io.moomin;

import io.moomin.dao.CustomerDao;
import io.moomin.dao.LinkManDao;
import io.moomin.domain.Customer;
import io.moomin.domain.LinkMan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:appliactionContext.xml")
public class ObjectQueryTest {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private LinkManDao linkManDao;

    @Test
    @Transactional
    public void testQueryOneToMany() {
        //查询一个对象的时候，通过此对象查询所有的关联对象
        Customer one = customerDao.getOne(96l);
        System.out.println(one);
        //对象导航查询，此客户下的所有联系人，一到多，默认延迟加载
        Set<LinkMan> linkMans = one.getLinkMans();
        for (LinkMan linkMan : linkMans
        ) {
            System.out.println(linkMan);
        }
    }

    @Test
    @Transactional
    public void testQueryManyToOne() {
        LinkMan one = linkManDao.findOne(4l);
        //对象导航查询所属的客户,多到一，立即加载
        Customer customer = one.getCustomer();
        System.out.println(one);
        System.out.println(customer);
    }

}
