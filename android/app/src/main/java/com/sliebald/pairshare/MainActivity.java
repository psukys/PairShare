package com.sliebald.pairshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sliebald.pairshare.data.models.ExpenseList;
import com.sliebald.pairshare.databinding.ActivityMainBinding;
import com.sliebald.pairshare.utils.ExpenseListUtils;
import com.sliebald.pairshare.utils.KeyboardUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    /**
     * Result key for sign in activity.
     */
    private static final int RC_SIGN_IN = 53252;

    private AppBarConfiguration mAppBarConfiguration;

    private NavController mNavController;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private MainActivityViewModel mViewModel;
    private ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        //Log user in if he isn't already logged in.
        mFirebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build());
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                                .setAvailableProviders(providers).build(),
                        RC_SIGN_IN);
            }
        };
        // set addEntry and summary as tld destinations --> no back button
        mNavController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        Set<Integer> tlds = new HashSet<>();
        tlds.add(R.id.addExpense_dest);
        tlds.add(R.id.selectExpenseList_dest);


        mAppBarConfiguration = new AppBarConfiguration.Builder(tlds).build();
        setupActionBar(mNavController);
        setupBottomNavMenu(mNavController);

        mViewModel.getActiveExpenseList().observe(this, new Observer<ExpenseList>() {
            @Override
            public void onChanged(ExpenseList expenseList) {

                double expenseDiff =
                        ExpenseListUtils.getExpenseDifferenceFor(mFirebaseAuth.getUid(),
                                expenseList);

                String title = expenseList.getListName() + ": ";
                String completeSummaryString = title + expenseDiff + "€";

                Spannable spannable = new SpannableString(completeSummaryString);

                spannable.setSpan(new ForegroundColorSpan(ExpenseListUtils.getExpenseDifferenceColor(expenseDiff)), title.length(),
                        completeSummaryString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                mBinding.toolbar.setSubtitle(spannable);
//                mBinding.toolbar.setSubtitle(expenseList.getListName() + " " + expenseDiff + "€");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (response == null) {
                //TODO: according to the documentation this should happen if back is pressed in
                // the login activity. But it doesn't seem to work (emulator)
                // possible that a new login form is started before this is called and then
                // finish is called once a login is successful
                finish();
            }

            if (resultCode == RESULT_OK) {
                mViewModel.userLoggedIn();
            }


        }
    }


    /**
     * Setup the bottom navigation with the navController.
     *
     * @param navController The navController for the navigation mechanism
     */
    private void setupBottomNavMenu(NavController navController) {
        final BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNav, navController);
        // Hide the bottom navigation if the keyboard is open.
        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    int activeFragment =
                            Objects.requireNonNull(navController.getCurrentDestination()).getId();
                    // in addExpenseList_dest the bottomNav is never shown.
                    if (activeFragment == R.id.addExpenseList_dest)
                        return;
                    if (isOpen) {
                        bottomNav.setVisibility(View.GONE);
                    } else {
                        bottomNav.setVisibility(View.VISIBLE);
                    }
                });

    }

    /**
     * Setup the actionbar with the navController and toolbar.
     *
     * @param navController The navController for the navigation mechanism
     */
    private void setupActionBar(NavController navController) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        KeyboardUtils.hideKeyboard(this, mBinding.getRoot());
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            AuthUI.getInstance().signOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(authStateListener);

    }

}


