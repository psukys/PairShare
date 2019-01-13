package com.sliebald.pairshare.data.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Map;

/**
 * PoJo Object for Users stored in firestore.
 */
public class User {
    private String uid;

    private String name;

    private String photoUri;

    private String mail;

    @ServerTimestamp
    private Date created;
    @ServerTimestamp
    private Date modified;

    private Map<String, String> shares;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public Map<String, String> getShares() {
        return shares;
    }

    public void setShares(Map<String, String> shares) {
        this.shares = shares;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
