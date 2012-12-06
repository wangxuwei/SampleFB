package com.samplefb.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "socialdentity")
public class Socialdentity extends BaseEntity {
    private Long   user_id;
    private String fbid;
    private String fbToken;
    private String fbPhoto;
    private String fbSignedRequest;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getFbPhoto() {
        return fbPhoto;
    }

    public void setFbPhoto(String fbPhoto) {
        this.fbPhoto = fbPhoto;
    }

    public String getFbSignedRequest() {
        return fbSignedRequest;
    }

    public void setFbSignedRequest(String fbSignedRequest) {
        this.fbSignedRequest = fbSignedRequest;
    }

}
