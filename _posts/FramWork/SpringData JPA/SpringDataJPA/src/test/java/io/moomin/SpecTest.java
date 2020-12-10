package io.moomin;

import io.moomin.dao.CustomerDao;
import io.moomin.domain.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.*;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:appliactionContext.xml")
public class SpecTest {
    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testSpec() {
        //匿名内部类
        /**
         * 自定义查询条件
         * 实现Specification接口（提供泛型：查询的对象类型）
         * 实现toPredicate方法（构造查询条件）
         *  root：获取需要查询的对象属性
         *  CriteriaBuilder：构造查询条件的，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *
         *  查询条件
         *      查询方式：cb对象
         *      比较的属性名称 ：root对象
         */
        Specification<Customer> spec = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //获取比较的属性
                Path<Object> custName = root.get("custName");
                //构造查询条件 select * from cst_customer where cust_name = 'moomin'
                Predicate moomin = criteriaBuilder.equal(custName, "moomin");//进行精确匹配（比较的属性，取值）
                return moomin;
            }
        };
        Customer one = customerDao.findOne(spec);
        System.out.println(one);

    }

    @Test
    public void testSpecMore() {
        //匿名内部类
        /**
         * 自定义查询条件
         * 实现Specification接口（提供泛型：查询的对象类型）
         * 实现toPredicate方法（构造查询条件）
         *  root：获取需要查询的对象属性
         *  CriteriaBuilder：构造查询条件的，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *
         *  查询条件
         *      查询方式：cb对象
         *      比较的属性名称 ：root对象
         */
        Specification<Customer> spec = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //获取比较的属性
                Path<Object> custName = root.get("custName");
                Path<Object> custPhone = root.get("custPhone");
                //构造查询条件 select * from cst_customer where cust_name = 'moomin'
                Predicate moomin = criteriaBuilder.equal(custName, "moomin");//进行精确匹配（比较的属性，取值）
                Predicate phone = criteriaBuilder.equal(custPhone, 1715616516);
                //将多个查询条件组合在一起
                Predicate and = criteriaBuilder.and(moomin, phone);
                return and;
            }
        };
        Customer one = customerDao.findOne(spec);
        System.out.println(one);

    }

    @Test
    public void testSpecLike() {
        //匿名内部类
        /**
         * 自定义查询条件
         * 实现Specification接口（提供泛型：查询的对象类型）
         * 实现toPredicate方法（构造查询条件）
         *  root：获取需要查询的对象属性
         *  CriteriaBuilder：构造查询条件的，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *
         *  查询条件
         *      查询方式：cb对象
         *      比较的属性名称 ：root对象
         *
         *  equal：直接得到path对象（属性），然后进行比较既可
         *  gt，lt，ge，le，like：得到path对象，根据path指定比较的参数类型，再去进行比较
         *      指定参数类型：path.as(类型的字节码对象)
         */
        Specification<Customer> spec = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //获取比较的属性
                Path<Object> custName = root.get("custName");
                //构造查询条件 select * from cst_customer where cust_name = 'moomin'
                Predicate moomin = criteriaBuilder.equal(custName, "moomin");//进行精确匹配（比较的属性，取值）
                //指定参数类型
                Predicate like = criteriaBuilder.like(custName.as(String.class), "%o%");
                return like;
            }
        };
        List<Customer> all = customerDao.findAll(spec);
        System.out.println(all);

    }
    @Test
    public void testSpecSort() {
        //匿名内部类
        /**
         * 自定义查询条件
         * 实现Specification接口（提供泛型：查询的对象类型）
         * 实现toPredicate方法（构造查询条件）
         *  root：获取需要查询的对象属性
         *  CriteriaBuilder：构造查询条件的，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *
         *  查询条件
         *      查询方式：cb对象
         *      比较的属性名称 ：root对象
         *
         *  equal：直接得到path对象（属性），然后进行比较既可
         *  gt，lt，ge，le，like：得到path对象，根据path指定比较的参数类型，再去进行比较
         *      指定参数类型：path.as(类型的字节码对象)
         */
        Specification<Customer> spec = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //获取比较的属性
                Path<Object> custName = root.get("custName");
                //构造查询条件 select * from cst_customer where cust_name = 'moomin'
                Predicate moomin = criteriaBuilder.equal(custName, "moomin");//进行精确匹配（比较的属性，取值）
                //指定参数类型
                Predicate like = criteriaBuilder.like(custName.as(String.class), "%o%");
                return like;
            }
        };
        //添加排序
        //创建排序对象，需要调用构造方法实例化sort对象
        //第一个参数，排序的顺序
        //排序的属性名称
        Sort sort = new Sort(Sort.Direction.DESC, "custId");
        List<Customer> all = customerDao.findAll(spec, sort);
        for (Customer c :
                all) {
            System.out.println(c);
        }

    }
    @Test
    public void testSpecPage() {
        //匿名内部类
        /**
         * 自定义查询条件
         * 实现Specification接口（提供泛型：查询的对象类型）
         * 实现toPredicate方法（构造查询条件）
         *  root：获取需要查询的对象属性
         *  CriteriaBuilder：构造查询条件的，内部封装了很多的查询条件（模糊匹配，精准匹配）
         *
         *  查询条件
         *      查询方式：cb对象
         *      比较的属性名称 ：root对象
         *
         *  equal：直接得到path对象（属性），然后进行比较既可
         *  gt，lt，ge，le，like：得到path对象，根据path指定比较的参数类型，再去进行比较
         *      指定参数类型：path.as(类型的字节码对象)
         */
        Specification<Customer> spec = null;
        Pageable pageRequest = new PageRequest(0,1);
        /**
         * Pageable
         *  查询页码
         *  每页查询的条数
         *  findAll(Specification,Pageable)
         */
        Page<Customer> all = customerDao.findAll(spec, pageRequest);
        System.out.println(all.getTotalElements());//得到总条数
        System.out.println(all.getContent());//得到数据集合列表
        System.out.println(all.getTotalPages());//得到总页数

    }
}
