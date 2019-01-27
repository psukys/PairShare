package com.sliebald.pairshare.data.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.Map;

public class ExpenseList {


    @ServerTimestamp
    private Date created;
    @ServerTimestamp
    private Date modified;

    private Map<String, ExpenseSummary> sharers;

    private String invite;

    private String listName;


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

    public Map<String, ExpenseSummary> getSharers() {
        return sharers;
    }

    public void setSharers(Map<String, ExpenseSummary> sharers) {
        this.sharers = sharers;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }

}
