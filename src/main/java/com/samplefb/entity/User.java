package com.samplefb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "principal")
public class User extends BaseEntity {

    @Column(name = "user_name")
    private String username;
    @Column(name = "full_name")
    private String fullName;
    private String password;

    @Transient
    private Socialdentity socialdentity;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Socialdentity getSocialdentity() {
        return socialdentity;
    }

    public void setSocialdentity(Socialdentity socialdentity) {
        this.socialdentity = socialdentity;
    }
}
