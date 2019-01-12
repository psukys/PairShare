package com.sliebald.pairshare.ui.addExpense;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sliebald.pairshare.data.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    void updateUser() {

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null)
            return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(fUser.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    createUser(fUser);
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });


    }

    private void createUser(FirebaseUser fUser) {
        User user = new User();
        user.setName(fUser.getDisplayName());
        if (fUser.getPhotoUrl() != null)
            user.setPhotoUri(fUser.getPhotoUrl().toString());
        user.setUid(fUser.getUid());
        Map<String, String> shares = new HashMap<>();
        shares.put("grgerhewh", "Sbnrbsrege");
        shares.put("stvvfvvdfaate", "44444444");
        user.setShares(shares);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(user);
    }
}
