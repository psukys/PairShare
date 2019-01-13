package com.sliebald.pairshare.data;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sliebald.pairshare.data.models.User;

import java.util.HashMap;
import java.util.Map;

public class Repository {

    /**
     * Tag for logging.
     */
    private static final String TAG = Repository.class.getSimpleName();
    // Lock for Singleton instantiation
    private static final Object LOCK = new Object();
    @SuppressLint("StaticFieldLeak")
    private static Repository sInstance;
    private FirebaseUser mFbUser;
    private FirebaseFirestore mDb;

    private Repository() {
        mFbUser = FirebaseAuth.getInstance().getCurrentUser();
        mDb = FirebaseFirestore.getInstance();
    }

    /**
     * Singleton to make sure only one {@link Repository} is used at a time.
     *
     * @return A new {@link Repository} if none exists. If already an instance exists this is
     * returned instead of creating a new one.
     */
    public synchronized static Repository getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository();
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Gets the currently logged in user and returns the result using the given
     * {@link OnCompleteListener}
     *
     * @param callback The listener called on success of the db request.
     */
    public void getCurrentUser(OnCompleteListener<DocumentSnapshot> callback) {

        if (mFbUser == null)
            return;
        DocumentReference docRef = mDb.collection("users").document(mFbUser.getUid());
        docRef.get().addOnCompleteListener(callback);

    }

    /**
     * Creates a new entry for the currently logged in User in Firestore.
     */
    public void createNewUser() {
        User user = new User();
        user.setName(mFbUser.getDisplayName());
        if (mFbUser.getPhotoUrl() != null)
            user.setPhotoUri(mFbUser.getPhotoUrl().toString());
        user.setUid(mFbUser.getUid());
        user.setMail(mFbUser.getEmail());
        Map<String, String> shares = new HashMap<>();
//        shares.put("1111", "1234");
        //      shares.put("2222", "5678");
        user.setShares(shares);
        mDb.collection("users").document(user.getUid()).set(user);
    }


}
