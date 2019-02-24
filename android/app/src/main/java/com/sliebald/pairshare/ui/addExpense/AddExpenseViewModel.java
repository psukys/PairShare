package com.sliebald.pairshare.ui.addExpense;

import com.sliebald.pairshare.data.Repository;
import com.sliebald.pairshare.data.models.Expense;

import java.util.Calendar;
import java.util.Objects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class AddExpenseViewModel extends ViewModel implements Repository.ResultCallback {
    private static final String TAG = AddExpenseViewModel.class.getSimpleName();

    private MutableLiveData<Calendar> calendar;

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

    LiveData<Calendar> getCalendar() {
        if (calendar == null) {
            calendar = new MutableLiveData<>();
            calendar.setValue(Calendar.getInstance());
        }
        return calendar;
    }

    /**
     * Updates the calendar with the given year/month/day
     *
     * @param year       Year to set.
     * @param month      Month to set.
     * @param dayOfMonth Day to set.
     */
    void setDate(int year, int month, int dayOfMonth) {
        Calendar cal = calendar.getValue();
        if (cal != null) {
            cal.set(year, month, dayOfMonth);
            calendar.setValue(cal);
        }
    }

    /**
     * Add an expense to the currently selected list.
     *
     * @param amount  Amount of money spent.
     * @param comment Comment for the expense (e.g. reason).
     */
    void addExpense(Double amount, String comment) {
        Expense expense = new Expense();
        expense.setComment(comment);
        expense.setAmount(amount);
        expense.setTimeOfExpense(Objects.requireNonNull(calendar.getValue()).getTime());
        Repository.getInstance().addExpense(expense, this);
    }

    @Override
    public void reportResult(int resultCode) {
        if (resultCode == 0) {
            operationSuccessful.postValue(true);
        } else if (resultCode == -1) {
            errorMessage.postValue("Could not add expense to list, try again later.");
        }
    }

}
