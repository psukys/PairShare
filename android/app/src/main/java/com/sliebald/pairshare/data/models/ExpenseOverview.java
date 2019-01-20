package com.sliebald.pairshare.data.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Map;

public class ExpenseOverview {


    @ServerTimestamp
    private Date created;
    @ServerTimestamp
    private Date modified;

    private Map<String, String> sharer;

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

    public Map<String, String> getSharer() {
        return sharer;
    }

    public void setSharer(Map<String, String> sharer) {
        this.sharer = sharer;
    }
}
