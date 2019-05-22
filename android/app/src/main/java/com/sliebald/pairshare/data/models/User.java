package com.sliebald.pairshare.data.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * PoJo Object for Users stored in firestore.
 */
public class User {
    /**
     * Firestore key for accessing the mail address of an {@link User}.
     */
    public static final String KEY_MAIL = "mail";

    /**
     * Email address of the {@link User}.
     */
    private String mail;

    /**
     * Username of the {@link User}.
     */
    private String username;

    /**
     * Time the {@link User} was created.
     */
    @ServerTimestamp
    private Date created;

    /**
     * Time the {@link User} was modified.
     */
    @ServerTimestamp
    private Date modified;

    /**
     * Get the mail address of the {@link User}.
     *
     * @return mail address as String.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Set the mail address of the {@link User}.
     *
     * @param mail New mail address as String.
     */
    public void setMail(String mail) {
        this.mail = mail.toLowerCase();
    }

    /**
     * Get the Username of the {@link User}
     *
     * @return username as String.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the {@link User}
     *
     * @param username username as String.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
