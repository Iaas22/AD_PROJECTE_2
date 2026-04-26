package com.example.tienda.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Role")
@EntityListeners(AuditingEntityListener.class)


public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) 
    private Long roleid;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private RoleName name;

    @Column(length = 200)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();


    public Role() {}

    public void setIDRole(Long id) {
        this.roleid = id;
    }

    public Long getIDRole() {
        return roleid;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public RoleName getName() {
        return name;
    }

    public void setDescr(String description) {
        this.description = description;
    }

    public String getDescr() {
        return description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
