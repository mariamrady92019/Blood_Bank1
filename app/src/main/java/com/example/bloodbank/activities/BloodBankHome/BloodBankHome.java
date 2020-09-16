package com.example.bloodbank.activities.BloodBankHome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.AddNewHospitalActivity;
import com.example.bloodbank.activities.BloodBankHome.Donors.DonorsActivity;
import com.example.bloodbank.activities.BloodBankHome.fragments.AvailableTypesFragment;
import com.example.bloodbank.activities.BloodBankHome.fragments.PostRequests.PostRequestsActivity;
import com.example.bloodbank.activities.BloodBankHome.fragments.pathients.PatientsActivity;
import com.example.bloodbank.activities.BloodBankHome.fragments.pending_requests.PendingRequestsActivity;
import com.example.bloodbank.activities.MainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/12/2020
 **/
public class BloodBankHome extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.cl_root_view)
    CoordinatorLayout clRootView;

    private ActionBarDrawerToggle mDrawerToggle;
     private FirebaseAuth mAuth;
     final FragmentManager fm = getSupportFragmentManager();
     final Fragment availableTypesFragment = new AvailableTypesFragment();
     Fragment currentFragment = availableTypesFragment;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_home);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initDrawerLayout();
    }

    private void initDrawerLayout() {

        setSupportActionBar(mToolbar);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(mDrawerToggle);
        drawerLayout.setDrawerElevation(0);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float END_SCALE = 0.8f;

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                clRootView.setScaleX(offsetScale);
                clRootView.setScaleY(offsetScale);

            }
        });
        mDrawerToggle.syncState();

        setupNavMenu();


    }

    void setupNavMenu() {
        navView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            switch (item.getItemId()) {
                case R.id.action_available_types:
                    fm.beginTransaction().hide(currentFragment).show(availableTypesFragment).commit();
                    currentFragment = availableTypesFragment;
                    mToolbar.setTitle(availableTypesFragment.getClass().getSimpleName());
                    break;
                case R.id.action_pending_requests:
                    startActivity(new Intent(this, PendingRequestsActivity.class));
                    break;
                case R.id.action_patients:
                    startActivity(new Intent(this, PatientsActivity.class));
                    break;
                case R.id.action_post_request:
                    startActivity(new Intent(this, PostRequestsActivity.class));
                    break;
                case R.id.action_donners:
                    startActivity(new Intent(this, DonorsActivity.class));
                    break;
                case R.id.action_add_hospital:
                    startActivity(new Intent(this, AddNewHospitalActivity.class));
                    break;
                case R.id.action_logout:
                    mAuth.signOut();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
            return true;
        });
//        fm.beginTransaction().add(R.id.frame_content, addNewChildFragment, "4").commit();
//        fm.beginTransaction().add(R.id.frame_content, settingsFragment, "3").commit();
//        fm.beginTransaction().add(R.id.frame_content, lastLocationFragment, "2").hide(lastLocationFragment).commit();
        fm.beginTransaction().add(R.id.frame_content, availableTypesFragment, "1").commit();

    }
}
