package io.moomin.dao;

import com.sun.xml.internal.bind.v2.model.core.ID;
import io.moomin.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerDao extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    /**
     * 根据客户名称查询客户
     * jpql: from Customer where custName = ?
     * @param custName
     * @return
     */
    @Query(value = "from Customer where custName = ?")
    public Customer findJpql(String custName);

    @Query(value = "from Customer  where custName = ? and custId = ?")
    public Customer findCustNameAndId(String name, Long id);

    @Query(value = "update Customer set custName = ?2 where custId = ?1")
    @Modifying
    public void UpdateId(Long id,String name);

    @Query(value = "select * from cst_customer" ,nativeQuery = true)
    public List<Object[]> findSql();

    @Query(value = "select * from cst_customer where cust_name like ?" ,nativeQuery = true)
    public List<Object[]> findSqlName(String name);


    public Customer findByCustName(String custName);

    public List<Customer> findByCustNameLike(String custName);

    public Customer findByCustNameAndCustId(String custName, Long id);
}
