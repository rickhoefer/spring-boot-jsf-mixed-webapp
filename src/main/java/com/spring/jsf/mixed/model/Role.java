package com.spring.jsf.mixed.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RoleModel")
@AttributeOverride(name = "id", column = @Column(name = "ROLE_ID"))
public class Role extends AbstractLogEntry<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(unique = true, name = "Role_Name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "User_Role", joinColumns = { //
            @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID")}, //
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")})
    private List<User> users = new ArrayList<>();


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
