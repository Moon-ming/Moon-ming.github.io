package io.moomin;

import io.moomin.domain.Customer;
import io.moomin.utils.JpaUtils;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaTest {
    /*
     * 加载配置文件创建工厂（实体管理器工厂）对象
     * 通过实体管理器工厂获取实体管理器
     * 获取事务对象，开启事务
     * 完成增删改查操作
     * 提交事务（回滚事务）
     * 释放资源
     * */
    @Test
    public void test() {
        //根据持久化单元名称创建实体管理器工厂
        /*
         * EntityManagerFactory的创建过程比较浪费资源
         * 多个线程访问同一个EntityMannagerFactory不会有线程安全问题
         * 创建一个公共的EntityManagerFactory的对象
         * 静态代码块的形式创建
         * */
        /*EntityManagerFactory factory = Persistence.createEntityManagerFactory("myJpa");
        //创建实体管理器
        EntityManager em = factory.createEntityManager();*/
        EntityManager em = JpaUtils.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Customer customer = new Customer();
        customer.setCustName("moomin");
        customer.setCustIndustry("coder");
        em.persist(customer);
        tx.commit();
        em.close();
        //factory.close();

    }

    @Test
    public void testFind() {
        EntityManager entityManager = JpaUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.find(Customer.class, 2l);
        System.out.println(customer);
        transaction.commit();
        entityManager.close();

    }
    @Test
    public void testReference() {
        EntityManager entityManager = JpaUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.getReference(Customer.class, 2l);
        System.out.println(customer);
        transaction.commit();
        entityManager.close();

    }

    @Test
    public void testRemove() {
        EntityManager entityManager = JpaUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.getReference(Customer.class, 2l);
        entityManager.remove(customer);
        System.out.println(customer);
        transaction.commit();
        entityManager.close();

    }

    @Test
    public void testUpdate() {
        EntityManager entityManager = JpaUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.getReference(Customer.class, 1l);
        customer.setCustPhone("1715616516");
        entityManager.merge(customer);
        transaction.commit();
        entityManager.close();

    }
}
