package io.moomin.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sys_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long UserId;
    @Column(name = "user_name")
    private String UserName;

    @Column(name = "user_age")
    private Integer age;

    /*配置多对多映射关系
     * 声明表关系的配置
     * 配置中间表（包含两个外键）
     * */
    @ManyToMany(targetEntity = Role.class,cascade = CascadeType.ALL)
    @JoinTable(name = "sys_user_role",
        //当前对象在中间表中的外键
        joinColumns = {@JoinColumn(name = "sys_user_id", referencedColumnName ="user_id")},
            //对方对象在中间表的外键
            inverseJoinColumns = {@JoinColumn(name = "sys_role_id", referencedColumnName ="role_id")}

    )
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserId=" + UserId +
                ", UserName='" + UserName + '\'' +
                ", age=" + age +
                '}';
    }
}
