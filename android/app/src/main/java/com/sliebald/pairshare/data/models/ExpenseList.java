package com.sliebald.pairshare.data.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExpenseList {

    public static final String KEY_LISTNAME = "listName";
    public static final String KEY_SHARERS = "sharers";
    public static final String KEY_SHARER_INFO = "sharerInfo";
    public static final String KEY_MODIFIED = "modified";

    @ServerTimestamp
    private Date created;
    @ServerTimestamp
    private Date modified;

    private Map<String, ExpenseSummary> sharerInfo;
    private List<String> sharers;
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

    public Map<String, ExpenseSummary> getSharerInfo() {
        return sharerInfo;
    }

    public void setSharerInfo(Map<String, ExpenseSummary> sharerInfo) {
        this.sharerInfo = sharerInfo;
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

    public List<String> getSharers() {
        return sharers;
    }

    public void setSharers(List<String> sharers) {
        this.sharers = sharers;
    }
}
