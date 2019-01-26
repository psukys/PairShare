package com.sliebald.pairshare.data;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sliebald.pairshare.data.models.Expense;
import com.sliebald.pairshare.data.models.ExpenseOverview;
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

    private static final String COLLECTION_KEY_USERS = "users";
    private static final String COLLECTION_KEY_EXPENSE_OVERVIEW = "expenses_overview";
    private static final String COLLECTION_KEY_EXPENSE = "expenses";

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
        user.setMail(mFbUser.getEmail());
        mDb.collection(COLLECTION_KEY_USERS).document(mFbUser.getUid()).set(user);
    }

    public void createTestExpense() {
        ExpenseOverview expenseOverview = new ExpenseOverview();
        Map<String, String> sharer = new HashMap<>();
        sharer.put(mFbUser.getUid(), "1234");
        expenseOverview.setSharer(sharer);
        mDb.collection(COLLECTION_KEY_EXPENSE_OVERVIEW).document("test").set(expenseOverview);

    }


    public void createTestExpenseOverview() {

        Expense expense = new Expense();
        expense.setUserID(mFbUser.getUid());
        expense.setAmount(50.1);
        expense.setComment("This is a test expense");
        mDb.collection(COLLECTION_KEY_EXPENSE_OVERVIEW).document("test").collection(COLLECTION_KEY_EXPENSE).add(expense);
        expense.setUserID(mFbUser.getUid());
        expense.setAmount(150.1);
        expense.setComment("This is a second test expense");

        mDb.collection(COLLECTION_KEY_EXPENSE_OVERVIEW).document("test").collection(COLLECTION_KEY_EXPENSE).add(expense);

    }

}
