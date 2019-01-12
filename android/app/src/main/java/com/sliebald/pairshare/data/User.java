package com.sliebald.pairshare.data;

import java.util.Map;

public class User {
    private String uid;

    private String name;

    private String photoUri;

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
}
