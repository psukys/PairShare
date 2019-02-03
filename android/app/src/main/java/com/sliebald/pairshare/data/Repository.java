package com.sliebald.pairshare.data;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.models.Expense;
import com.sliebald.pairshare.data.models.ExpenseList;
import com.sliebald.pairshare.data.models.ExpenseSummary;
import com.sliebald.pairshare.data.models.User;
import com.sliebald.pairshare.ui.selectExpenseList.ExpenseListHolder;
import com.sliebald.pairshare.ui.selectExpenseList.InviteListHolder;
import com.sliebald.pairshare.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class Repository {

    private static final String COLLECTION_KEY_EXPENSE_LISTS = "expense_lists";
    private static final String COLLECTION_KEY_USERS = "users";
    private static final String COLLECTION_KEY_EXPENSE = "expenses";

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
        user.setMail(mFbUser.getEmail());
        mDb.collection(COLLECTION_KEY_USERS).document(mFbUser.getUid()).set(user);
    }

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
     * Gets a {@link FirestoreRecyclerAdapter} that listens on active {@link ExpenseList}s of the
     * current user. The calling Fragment or activity has to manage the related lifecycle
     * operations (adapter.startListening() in onStart() and adapter.stopListening in onStop)
     *
     * @return The {@link FirestoreRecyclerAdapter} to connect with a
     * {@link androidx.recyclerview.widget.RecyclerView}.
     */
    public FirestoreRecyclerAdapter getExpenseListsAdapter() {

        Query query = mDb.collection(COLLECTION_KEY_EXPENSE_LISTS)
                .whereArrayContains(ExpenseList.KEY_SHARERS, mFbUser.getUid())
                .orderBy(ExpenseList.KEY_MODIFIED)
                .limit(50);

        FirestoreRecyclerOptions<ExpenseList> options =
                new FirestoreRecyclerOptions.Builder<ExpenseList>()
                        .setQuery(query, ExpenseList.class)
                        .build();

        return new FirestoreRecyclerAdapter<ExpenseList,
                ExpenseListHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull ExpenseListHolder holder, int position,
                                         @NonNull ExpenseList expenseList) {
                holder.bind(expenseList, getSnapshots().getSnapshot(position).getId());
            }

            @NonNull
            @Override
            public ExpenseListHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.recycler_item_expense_list, group, false);
                return new ExpenseListHolder(view);
            }
        };
    }


    /**
     * Gets a {@link FirestoreRecyclerAdapter} that listens on invitations for {@link ExpenseList}
     * s of the current user. The calling Fragment or activity has to manage the related
     * lifecycle operations (adapter.startListening() in onStart() and adapter.stopListening in
     * onStop)
     *
     * @return The {@link FirestoreRecyclerAdapter} to connect with a
     * {@link androidx.recyclerview.widget.RecyclerView}.
     */
    public FirestoreRecyclerAdapter getPendingInvitationListsQuery() {

        Query query = mDb.collection(COLLECTION_KEY_EXPENSE_LISTS)
                .whereEqualTo(ExpenseList.KEY_INVITE, mFbUser.getUid())
                .orderBy(ExpenseList.KEY_MODIFIED)
                .limit(50);

        FirestoreRecyclerOptions<ExpenseList> options =
                new FirestoreRecyclerOptions.Builder<ExpenseList>()
                        .setQuery(query, ExpenseList.class)
                        .build();

        return new FirestoreRecyclerAdapter<ExpenseList,
                InviteListHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull InviteListHolder holder, int position,
                                         @NonNull ExpenseList expenseList) {
                holder.bind(expenseList, getSnapshots().getSnapshot(position).getId());
            }

            //TODO: adapt viewholderlayout
            @NonNull
            @Override
            public InviteListHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.recycler_item_expense_list, group, false);
                return new InviteListHolder(view);
            }
        };

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
        ExpenseList expenseList = new ExpenseList();
        expenseList.setListName(listName);
        List<String> sharers = new ArrayList<>();
        sharers.add(mFbUser.getUid());
        expenseList.setSharers(sharers);
        Map<String, ExpenseSummary> sharerInfo = new HashMap<>();
        ExpenseSummary newSummary = new ExpenseSummary(0, 0.0, 0.0);
        sharerInfo.put(mFbUser.getUid(), newSummary);
        expenseList.setSharerInfo(sharerInfo);

        mDb.collection(COLLECTION_KEY_USERS).whereEqualTo(User.KEY_MAIL,
                invite.toLowerCase()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {

                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                if (!documentSnapshot.getId().equals(mFbUser.getUid())) {
                    expenseList.setInvite(documentSnapshot.getId());
                    mDb.collection(COLLECTION_KEY_EXPENSE_LISTS).add(expenseList)
                            .addOnSuccessListener(documentReference -> callback.reportResult(0));
                }
            } else {
                //throw exception?
                callback.reportResult(-1);
            }
        });


    }

    /**
     * Adds the given expense to the currently selected List. Adds the ID of the currently logged
     * in user.
     *
     * @param expense  The {@link Expense} to add.
     * @param callback Called with resultcode 0 once successful, resultcode -1 in an error case.
     */
    public void addExpense(Expense expense, ResultCallback callback) {
        expense.setUserID(mFbUser.getUid());
        mDb.collection(COLLECTION_KEY_EXPENSE_LISTS).document(PreferenceUtils.
                getSelectedSharedExpenseListID()).collection(COLLECTION_KEY_EXPENSE).add(expense)
                .addOnSuccessListener(documentReference -> callback.reportResult(0))
                .addOnFailureListener(documentReference -> callback.reportResult(-1));

    }

    public interface ResultCallback {
        void reportResult(int resultCode);
    }


}
