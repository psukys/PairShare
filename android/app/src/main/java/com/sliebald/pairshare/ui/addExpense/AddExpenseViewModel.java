package com.sliebald.pairshare.ui.addExpense;

import java.util.Calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class AddExpenseViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<Calendar> calendar;

    LiveData<Calendar> getCalendar() {
        if (calendar == null) {
            calendar = new MutableLiveData<>();
            calendar.setValue(Calendar.getInstance());
        }
        return calendar;
    }

    void setDate(int year, int month, int dayOfMonth) {
        Calendar cal = calendar.getValue();
        if (cal != null) {
            cal.set(year, month, dayOfMonth);
            calendar.setValue(cal);
        }
    }
}
