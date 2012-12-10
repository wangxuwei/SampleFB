package com.samplefb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "contact")
public class Contact extends BaseEntity {

    private String name;
    private String fbid;
    private String fbtoken;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getFbtoken() {
        return fbtoken;
    }

    public void setFbtoken(String fbtoken) {
        this.fbtoken = fbtoken;
    }
}
