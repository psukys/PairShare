package com.sliebald.pairshare.data.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Single {@link Expense} that is related to an {@link ExpenseList} collection in firestore.
 */
public class Expense {

    /**
     * Key for accessing the created value of the Expense in firestore.
     */
    public static final String KEY_CREATED = "created";


    /**
     * Time the {@link Expense} was created.
     */
    @ServerTimestamp
    private Date created;

    /**
     * Firebase ID of the {@link User} creating the {@link Expense}.
     */
    private String userID;

    /**
     * Firebase name of the {@link User} creating the Expense at the time of creation.
     */
    private String userName;

    /**
     * Amount of the expense. Positive values mean the User spent money, negative the {@link User} got
     * money from another {@link User}.
     */
    private double amount;

    /**
     * Optional comment for the {@link Expense}.
     */
    private String comment;

    /**
     * Date the {@link Expense} was made.
     */
    private Date timeOfExpense;


    /**
     * Get when the expense was created.
     *
     * @return Creation {@link Date}
     */
    public Date getCreated() {
        return created;
    }


    /**
     * Get the ID of the {@link User} that created the {@link Expense}.
     *
     * @return Firebase userid of the {@link User}.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Set the id of the {@link User} that created the {@link Expense}.
     *
     * @param userID firebase id of the {@link User}.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Get amount of the expense. Positive values mean the User spent money, negative the
     * {@link User} got money from another {@link User}.
     *
     * @return Expense amount as double value.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set amount of the expense. Positive values mean the User spent money, negative the
     * {@link User} got money from another {@link User}.
     *
     * @param amount Expense amount as double value.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Get the optional comment describing the {@link Expense}.
     *
     * @return Comment as String
     */
    public String getComment() {
        return comment;
    }

    /**
     * Get the optional comment describing the {@link Expense}.
     *
     * @param comment Comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get the {@link Date} the {@link Expense} took place
     *
     * @return {@link Date} of the Expense.
     */
    public Date getTimeOfExpense() {
        return timeOfExpense;
    }

    /**
     * Set the {@link Date} the {@link Expense} took place.
     *
     * @param timeOfExpense {@link Date} of the Expense.
     */
    public void setTimeOfExpense(Date timeOfExpense) {
        this.timeOfExpense = timeOfExpense;
    }

    /**
     * Get the name of the {@link User} creating the Expense at the time of creation.
     *
     * @return name as String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the name of the user creating the Expense at the time of creation
     *
     * @param userName name of the user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
