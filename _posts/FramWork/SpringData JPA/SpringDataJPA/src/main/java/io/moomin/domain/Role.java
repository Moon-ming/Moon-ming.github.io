package io.moomin.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sys_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    /*@ManyToMany(targetEntity = User.class)
    @JoinTable(name = "sys_user_role",
        joinColumns = {@JoinColumn(name = "sys_role_id",referencedColumnName = "role_id")},
            inverseJoinColumns ={@JoinColumn(name = "sys_user_id",referencedColumnName = "user_id")}
    )*/
    private Set<User> users = new HashSet<>();



    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
