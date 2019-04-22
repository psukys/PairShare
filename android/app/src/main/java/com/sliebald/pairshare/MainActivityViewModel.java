package com.sliebald.pairshare;

import androidx.lifecycle.ViewModel;

import com.sliebald.pairshare.data.Repository;

public class MainActivityViewModel extends ViewModel {


    /**
     * Calls the repository to create the user if it doesn't exist.
     */
    public void userLoggedIn() {
        Repository.getInstance().checkNewUser();
    }
}
