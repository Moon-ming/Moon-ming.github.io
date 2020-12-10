package io.moomin;

import io.moomin.domain.Customer;
import io.moomin.utils.JpaUtils;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class JpqlTest {
    @Test
    public void testAll() {
        EntityManager entityManager = JpaUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String jpql = "from io.moomin.domain.Customer";
        Query query = entityManager.createQuery(jpql);
        List resultList = query.getResultList();
        for (Object l :
                resultList) {
            System.out.println(l);
        }
        transaction.commit();
        entityManager.close();
    }
    @Test
    public void testOders() {
        EntityManager entityManager = JpaUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String jpql = "from Customer order by custId desc";
        Query query = entityManager.createQuery(jpql);
        List resultList = query.getResultList();
        for (Object l :
                resultList) {
            System.out.println(l);
        }
        transaction.commit();
        entityManager.close();
    }
    @Test
    public void testCount() {
        EntityManager entityManager = JpaUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String jpql = "select count(custId) from Customer";
        Query query = entityManager.createQuery(jpql);

        Object singleResult = query.getSingleResult();
        System.out.println(singleResult);
        transaction.commit();
        entityManager.close();
    }
    @Test
    public void testPaged() {
        EntityManager entityManager = JpaUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String jpql = "from Customer";
        Query query = entityManager.createQuery(jpql);
        query.setFirstResult(1);
        query.setMaxResults(2);
        List resultList = query.getResultList();
        for (Object l :
                resultList) {
            System.out.println(l);
        }
        transaction.commit();
        entityManager.close();
    }

    @Test
    public void testCondition() {
        EntityManager entityManager = JpaUtils.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        String jpql = "from Customer where custName like ?";
        Query query = entityManager.createQuery(jpql);

        query.setParameter(1, "%u%");
        List resultList = query.getResultList();
        for (Object l :
                resultList) {
            System.out.println(l);
        }
        transaction.commit();
        entityManager.close();
    }
}
