package com.sliebald.pairshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    /**
     * Result key for sign in activity.
     */
    private static final int RC_SIGN_IN = 53252;

    private AppBarConfiguration mAppBarConfiguration;

    private NavController mNavController;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        tlds.add(R.id.selectExpense_dest);


        mAppBarConfiguration = new AppBarConfiguration.Builder(tlds).build();
        setupActionBar(mNavController);
        setupBottomNavMenu(mNavController);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (response == null) {
                //TODO: according to the documentation this should happen if back is pressed in
                // the login activity. But it doesn't seem to work (emulator)
                finish();
            }
//        Repository.getInstance().createTestExpenseOverview();
//        Repository.getInstance().createTestExpense();

//            if (resultCode == RESULT_OK) {
//                // Successfully signed in
//                // ...
//            }


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
        return mNavController.navigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


