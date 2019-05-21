package com.sliebald.pairshare.data;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.sliebald.pairshare.data.models.Expense;
import com.sliebald.pairshare.data.models.ExpenseList;
import com.sliebald.pairshare.data.models.ExpenseSummary;
import com.sliebald.pairshare.data.models.User;
import com.sliebald.pairshare.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Repository Class for Pairshare. Responsible for accessing firebase (especially firestore).
 */
public class Repository {

    /**
     * Firestore key for the expense_lists collection. Collection holds {@link ExpenseList}
     * Documents.
     */
    private static final String COLLECTION_KEY_EXPENSE_LISTS = "expense_lists";

    /**
     * Firestore key for the users collection. Collection holds {@link User} Documents.
     */
    private static final String COLLECTION_KEY_USERS = "users";

    /**
     * Firestore key for the expense collections. Sub-collection of a {@link ExpenseList}
     * Document in the expense_lists collection. Holds {@link Expense} Documents.
     */
    private static final String COLLECTION_KEY_EXPENSE = "expenses";

    /**
     * Tag for logging.
     */
    private static final String TAG = Repository.class.getSimpleName();
    // Lock for Singleton instantiation
    private static final Object LOCK = new Object();

    /**
     * Singleton instance of the Repository.
     */
    @SuppressLint("StaticFieldLeak")
    private static Repository sInstance;
    /**
     * {@link FirebaseUser} currently authenticated on the phone running pairshare.
     */
    private FirebaseUser mFbUser;

    /**
     * {@link FirebaseFirestore} Firestore database access.
     */
    private FirebaseFirestore mDb;

    /**
     * Livedata for the currently selected ExpenseList.
     */
    private MutableLiveData<ExpenseList> activeExpenseList;


    /**
     * private constructor used by Singleton pattern.
     */
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
     * If the user already exists, nothing is changed.
     */
    public void checkNewUser() {
        User user = new User();
        user.setMail(mFbUser.getEmail());
        String id = mFbUser.getUid();

        mDb.collection(COLLECTION_KEY_USERS).document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && !document.exists()) {
                    mDb.collection(COLLECTION_KEY_USERS).document(id).set(user);
                }
            }
        });

    }

    /**
     * Testing/Debugging only.
     */
    public void createTestExpenseOverview() {

        Expense expense = new Expense();
        expense.setUserID(mFbUser.getUid());
        expense.setAmount(50.1);
        expense.setComment("This is a test expense");
        mDb.collection(COLLECTION_KEY_EXPENSE_LISTS).document("test").collection(COLLECTION_KEY_EXPENSE).add(expense);
        expense.setUserID(mFbUser.getUid());
        expense.setAmount(150.1);
        expense.setComment("This is a second test expense");

        mDb.collection(COLLECTION_KEY_EXPENSE_LISTS).document("test").collection(COLLECTION_KEY_EXPENSE).add(expense);

    }

    /**
     * Gets a {@link Query} for the expenseLists of the user. Can be used as input to create a
     * {@link FirestoreRecyclerAdapter} for displaying the data in a
     * {@link androidx.recyclerview.widget.RecyclerView}.
     *
     * @return The {@link Query} object
     */
    public Query getExpenseListsAdapter() {
        return mDb.collection(COLLECTION_KEY_EXPENSE_LISTS)
                .whereArrayContains(ExpenseList.KEY_SHARERS, mFbUser.getUid())
                .orderBy(ExpenseList.KEY_MODIFIED);
    }


    /**
     * Create a new ExpenseList for the own user and the invited user based on his email.
     *
     * @param listName Name of the new {@link ExpenseList}.
     * @param invite   Email address of the {@link User} that will be invited to the list.
     * @param callback Returns the result. 0 if operation went through, -1 if the other User
     *                 wasn't found.
     */
    public void createNewExpenseList(String listName, String invite, ResultCallback callback) {
        //TODO: addOnCompleteListener only works when the phone is online. add a check and abort
        // otherwise. Create a new ExpenseList.

        //Get the other invited User.
        Log.d(TAG, "adding expenselist: searching for user");
        mDb.collection(COLLECTION_KEY_USERS).whereEqualTo(User.KEY_MAIL,
                invite.toLowerCase()).get().addOnCompleteListener(task -> {
            Log.d(TAG, "adding expenselist: found a user");

            // if the invited user is found, get his id and add him to the sharerinfo too.
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                Log.d(TAG, "adding expenselist: got user document");

                if (!documentSnapshot.getId().equals(mFbUser.getUid())) {
                    ExpenseList expenseList = new ExpenseList();
                    expenseList.setListName(listName);
                    List<String> sharers = new ArrayList<>(2);
                    sharers.add(mFbUser.getUid());
                    sharers.add(documentSnapshot.getId());
                    expenseList.setSharers(sharers);

                    Map<String, ExpenseSummary> sharerInfo = new HashMap<>();
                    sharerInfo.put(mFbUser.getUid(), new ExpenseSummary());
                    sharerInfo.put(documentSnapshot.getId(), new ExpenseSummary());
                    expenseList.setSharerInfo(sharerInfo);

                    // Add the new expenslist to the collection and report success back.
                    Log.d(TAG, "adding expenselist: adding list");
                    mDb.collection(COLLECTION_KEY_EXPENSE_LISTS).add(expenseList)
                            .addOnSuccessListener(documentReference -> callback.reportResult(0));
                }
            } else {
                callback.reportResult(-1);
            }
        });


    }

    /**
     * Adds the given expense to the currently selected List. Adds the ID of the currently logged
     * in user to the logged expense.
     *
     * @param expense  The {@link Expense} to add.
     */
    public void addExpense(Expense expense) {
        expense.setUserID(mFbUser.getUid());
        Log.d(TAG, PreferenceUtils.getSelectedSharedExpenseListID());
        // TODO: transaction would be better, but only work when online. On small scale it should
        //  be unlikely that something goes wrong here
        // onSuccess/Failure/Complete listener also only works when online.
        DocumentReference affectedListDocument =
                mDb.collection(COLLECTION_KEY_EXPENSE_LISTS).document(PreferenceUtils.getSelectedSharedExpenseListID());

        String userSharerInfo = "sharerInfo." + mFbUser.getUid();
        DocumentReference expenseDocument =
                affectedListDocument.collection(COLLECTION_KEY_EXPENSE).document();

        // add the new expense and update the counters in the parent list as batch operation
        WriteBatch batch = mDb.batch();
        batch.set(expenseDocument, expense);
        batch.update(affectedListDocument, userSharerInfo + ".sumExpenses",
                FieldValue.increment(expense.getAmount()), userSharerInfo + ".numExpenses",
                FieldValue.increment(1));
        batch.commit();
    }

    /**
     * {@link android.content.SharedPreferences.OnSharedPreferenceChangeListener} for updating
     * the livedata if another list was selected. Cannot be a global variable, as it might get
     * garbage collected in that case.
     */
    private SharedPreferences.OnSharedPreferenceChangeListener onPrefChangeListener;

    public LiveData<ExpenseList> getActiveExpenseList() {

        if (activeExpenseList == null) {
            activeExpenseList = new MutableLiveData<>();
            onPrefChangeListener = (sharedPreferences, key) -> {
                updateActiveExpenseList();
            };
            PreferenceUtils.registerActiveListChangedListener(onPrefChangeListener);
            Log.d(TAG, "Added activeListChangedListener");
        }
        updateActiveExpenseList();
        return activeExpenseList;
    }

    private void updateActiveExpenseList() {
        Log.d(TAG, "Active List changed, updating LiveData.");
        mDb.collection(COLLECTION_KEY_EXPENSE_LISTS)
                .document(PreferenceUtils.getSelectedSharedExpenseListID())
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        ExpenseList list = snapshot.toObject(ExpenseList.class);
                        activeExpenseList.postValue(list);
                    }
                });
    }

    /**
     * Interface for reporting results back to the caller. -1= error, 0=success
     */
    public interface ResultCallback {
        void reportResult(int resultCode);
    }

}
