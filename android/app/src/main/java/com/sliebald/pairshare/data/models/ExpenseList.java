package com.sliebald.pairshare.data.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Data class for a list of {@link Expense}s.
 */
public class ExpenseList {


    /**
     * Firestore keys of the fields in {@link ExpenseList}. Equals the names of the variables.
     */
    //TODO: Use reflection?
    public static final String KEY_LISTNAME = "listName";
    public static final String KEY_SHARERS = "sharers";
    public static final String KEY_SHARER_INFO = "sharerInfo";
    public static final String KEY_MODIFIED = "modified";

    /**
     * Time the {@link ExpenseList} was created.
     */
    @ServerTimestamp
    private Date created;

    /**
     * Time the {@link ExpenseList} was last modified (e.g. changed the sharerInfo).
     */
    @ServerTimestamp
    private Date modified;

    /**
     * {@link Map} of sharers/{@link User}s of this list. Key is the firebase Auth user id, Value
     * the ExpenseSummary for the according user.
     */
    private Map<String, ExpenseSummary> sharerInfo;

    /**
     * List of {@link User}s involved in this {@link ExpenseList}. Needed as firestore cannot
     * check the sharerInfo map, where the keys contain the same info.
     */
    private List<String> sharers;

    /**
     * Name of the List
     */
    private String listName;

    /**
     * Get Time the {@link ExpenseList} was created as {@link Date}.
     *
     * @return {@link Date} the list was created.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Get Time the {@link ExpenseList} was modified as {@link Date} (e.g. sharerInfo updated).
     *
     * @return {@link Date} the list was modified.
     */
    public Date getModified() {
        return modified;
    }

    /**
     * Set Time the {@link ExpenseList} was modified as {@link Date} (e.g. sharerInfo updated).
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }


    /**
     * Get the {@link Map} of sharers/{@link User}s of this list. Key is the firebase Auth user id,
     * Value the ExpenseSummary for the according user.
     *
     * @return Map of users sharing the List
     */
    public Map<String, ExpenseSummary> getSharerInfo() {
        return sharerInfo;
    }

    /**
     * Set the map of users sharing this {@link ExpenseList}.
     *
     * @param sharerInfo The {@link Map} of {@link User}s sharing this {@link ExpenseList}.
     */
    public void setSharerInfo(Map<String, ExpenseSummary> sharerInfo) {
        this.sharerInfo = sharerInfo;
    }

    /**
     * Get the name of the {@link ExpenseList}.
     *
     * @return Name of the {@link ExpenseList}.
     */
    public String getListName() {
        return listName;
    }

    /**
     * Set the name of the {@link ExpenseList}.
     *
     * @param listName New name of the {@link ExpenseList}.
     */
    public void setListName(String listName) {
        this.listName = listName;
    }

    /**
     * Get the List of {@link User}s involved in this {@link ExpenseList}. Needed as firestore
     * cannot check the sharerInfo map, where the keys contain the same info.
     *
     * @return List of Users involved in List.
     */
    public List<String> getSharers() {
        return sharers;
    }

    /**
     * Set the List of {@link User}s involved in this {@link ExpenseList}. Needed as firestore
     * cannot check the sharerInfo map, where the keys contain the same info.
     *
     * @param sharers List of involved sharers.
     */
    public void setSharers(List<String> sharers) {
        this.sharers = sharers;
    }
}
