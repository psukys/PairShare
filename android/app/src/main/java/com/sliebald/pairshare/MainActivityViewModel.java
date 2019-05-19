package com.sliebald.pairshare;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sliebald.pairshare.data.Repository;
import com.sliebald.pairshare.data.models.ExpenseList;

public class MainActivityViewModel extends ViewModel {


    /**
     * Calls the repository to create the user if it doesn't exist.
     */
    public void userLoggedIn() {
        Repository.getInstance().checkNewUser();
    }


    private LiveData<ExpenseList> currentExpenseList;

    public MainActivityViewModel() {
        this.currentExpenseList = Repository.getInstance().getActiveExpenseList();
    }

    LiveData<ExpenseList> getActiveExpenseList() {
        return currentExpenseList;
    }

}
