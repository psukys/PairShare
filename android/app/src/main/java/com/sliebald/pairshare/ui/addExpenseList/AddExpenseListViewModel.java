package com.sliebald.pairshare.ui.addExpenseList;

import com.sliebald.pairshare.data.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class AddExpenseListViewModel extends ViewModel implements Repository.ResultCallback {


    private static final String TAG = AddExpenseListViewModel.class.getSimpleName();

    private MutableLiveData<String> errorMessage;

    LiveData<String> getErrorMessage() {
        if (errorMessage == null) {
            errorMessage = new MutableLiveData<>();
        }
        return errorMessage;
    }

    void createExpenseList(String listName, String invite) {
        if (listName.isEmpty()) {
            errorMessage.postValue("The lists name cannot be empty!");
        } else if (invite.isEmpty()) {
            errorMessage.postValue("Enter the email of the person you want to invite.");
        } else {
            Repository.getInstance().createNewExpenseList(listName, invite, this);
        }

    }

    @Override
    public void reportResult(int resultCode) {
        switch (resultCode) {
            case 0:
                errorMessage.postValue("Code" + resultCode);
                break;
            default:
                errorMessage.postValue("Code" + resultCode);
        }
    }
}
