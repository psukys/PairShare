package com.sliebald.pairshare;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.HashSet;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set addEntry and summary as tld destinations --> no back button
        mNavController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        Set<Integer> tlds = new HashSet<>();
        tlds.add(R.id.addExpense_dest);
        tlds.add(R.id.summary_dest);


        mAppBarConfiguration = new AppBarConfiguration.Builder(tlds).build();
        setupActionBar(mNavController);
        setupBottomNavMenu(mNavController);


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
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {
                            bottomNav.setVisibility(View.GONE);
                        } else {
                            bottomNav.setVisibility(View.VISIBLE);
                        }
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
}
