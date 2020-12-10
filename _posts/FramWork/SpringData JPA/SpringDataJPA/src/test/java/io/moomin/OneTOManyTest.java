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
public class OneTOManyTest {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private LinkManDao linkManDao;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testAdd() {
        Customer customer = new Customer();
        customer.setCustName("autumn");

        LinkMan linkMan = new LinkMan();
        linkMan.setLkmName("moomin");
        //配置了客户到联系人的关系
        //由于配置了一的一方到多的一方的关联关系（发送一条update语句，多余，放弃维护权）
        customer.getLinkMans().add(linkMan);
        //由于配置了多的一方到一的一方的关联关系（当保存的时候就已经对外键赋值）
        linkMan.setCustomer(customer);
        customerDao.save(customer);
       linkManDao.save(linkMan);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void testCascadeAdd() {
        Customer customer = new Customer();
        customer.setCustName("autumn1");

        LinkMan linkMan = new LinkMan();
        linkMan.setLkmName("moomin1");

        //配置了客户到联系人的关系
        //由于配置了一的一方到多的一方的关联关系（发送一条update语句，多余，放弃维护权）
        customer.getLinkMans().add(linkMan);

        //由于配置了多的一方到一的一方的关联关系（当保存的时候就已经对外键赋值）
        linkMan.setCustomer(customer);

        customerDao.save(customer);

    }
    @Test
    @Transactional
    @Rollback(value = false)
    public void testCascadeRemove() {
        Customer one = customerDao.findOne(102l);
        customerDao.delete(one);

    }
}
