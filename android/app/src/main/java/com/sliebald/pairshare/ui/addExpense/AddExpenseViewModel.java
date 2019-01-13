package com.sliebald.pairshare.ui.addExpense;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.sliebald.pairshare.data.Repository;

import java.util.Calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class AddExpenseViewModel extends ViewModel {
    private static final String TAG = AddExpenseViewModel.class.getSimpleName();

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

    /**
     * Get the current User.
     */
    void getUser() {

        Repository.getInstance().getCurrentUser(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Repository.getInstance().createNewUser();
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }

}
