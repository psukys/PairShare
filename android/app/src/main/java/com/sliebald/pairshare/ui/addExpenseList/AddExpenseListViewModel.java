package com.sliebald.pairshare.ui.addExpenseList;

import com.sliebald.pairshare.data.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class AddExpenseListViewModel extends ViewModel implements Repository.ResultCallback {
//TODO: externalize Strings

    private static final String TAG = AddExpenseListViewModel.class.getSimpleName();

    private MutableLiveData<String> errorMessage;
    private MutableLiveData<Boolean> operationSuccessful;


    LiveData<String> getErrorMessage() {
        if (errorMessage == null) {
            errorMessage = new MutableLiveData<>();
        }
        return errorMessage;
    }

    LiveData<Boolean> getOperationSuccessful() {
        if (operationSuccessful == null) {
            operationSuccessful = new MutableLiveData<>();
        }
        return operationSuccessful;
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
                operationSuccessful.postValue(true);
                break;
            default:
                errorMessage.postValue("Something went wrong on the server side, try again later.");
        }
    }
}
