package io.moomin;

import io.moomin.dao.CustomerDao;
import io.moomin.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:appliactionContext.xml")
public class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testFindOne() {
        Customer one = customerDao.findOne(3l);
        System.out.println(one);
    }

    @Test
    public void testSave() {
        Customer customer = new Customer();
        customer.setCustName("dasd");
        customerDao.save(customer);
    }

    @Test
    public void testUpdate() {
        Customer customer = new Customer();
        customer.setCustName("dasd+123");
        customer.setCustId(9l);
        customerDao.save(customer);
    }

    @Test
    public void testDelete() {
        Customer customer = new Customer();
        customerDao.delete(9l);
    }

    @Test
    public void testFindAll() {
        List<Customer> list = customerDao.findAll();
        for (Customer c :
                list) {
            System.out.println(c);
        }
    }
    @Test
    public void testCount() {
        long count = customerDao.count();
        System.out.println(count);

    }
    @Test
    public void testExists() {
        boolean exists = customerDao.exists(8l);
        System.out.println(exists);

    }
    @Test
    @Transactional
    //延迟加载
    public void testGetOne() {
        Customer one = customerDao.getOne(8l);
        System.out.println(one);

    }

    //jpql
    @Test
    public void testJpqlFind() {
        Customer buhui = customerDao.findJpql("buhui");
        System.out.println(buhui);

    }
    @Test
    public void testJpqlFindNameAndId() {
        Customer buhui = customerDao.findCustNameAndId("buhui",8l);
        System.out.println(buhui);

    }

   /* * springdataJpa使用jpql完成更新/删除操作
    * 需要手动添加事务支持
    * 默认执行结束后回滚事务
    * */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testJpqlUpdateNameAndId() {
        customerDao.UpdateId(8l,"jsaodia");


    }

    @Test
    public void testSqlFind() {
        List<Object[]> sql = customerDao.findSql();
        for (Object[] l :
                sql) {
            System.out.println(Arrays.toString(l));
        }
    }

    @Test
    public void testSqlFindName() {
        List<Object[]> sql = customerDao.findSqlName("moomin");
        for (Object[] l :
                sql) {
            System.out.println(Arrays.toString(l));
        }
    }
    //测试方法命名规则的查询
    @Test
    public void testFindByCustName() {
        customerDao.findByCustName("moomin");
    }

    @Test
    public void testFindByCustNameLike() {
        List<Customer> customers = customerDao.findByCustNameLike("%o%");
        System.out.println(customers);
    }
    @Test
    public void testFindByCustNameAndCustId() {
        Customer moomin = customerDao.findByCustNameAndCustId("moomin", 1l);
        System.out.println(moomin);
    }
}
