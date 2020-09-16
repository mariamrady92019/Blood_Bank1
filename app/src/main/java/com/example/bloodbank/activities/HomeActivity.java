package com.example.bloodbank.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.BloodBankHome.fragments.history.HistoryActivity;
import com.example.bloodbank.activities.donner.DonnerFragment;
import com.example.bloodbank.activities.new_business.PostRequestsFragment;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.events.EventSwitchFragment;
import com.example.bloodbank.fireStoreDataBase.posts.BaseActivityA;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.firebaseStroge.StrogeBuilder;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HomeActivity extends BaseActivityA
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    protected TabLayout tab;
    protected FrameLayout homeFragmentContainer;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private static final int REQUEST_IMAGE_FROMGALLERY = 112;
    protected ImageView menueBtn;
    Switch switch_id;
    UserResponse user = null;

    FirebaseAuth mAuth;

    View hView;
    TextView tvUserName;
    TextView tvBloodType;
    TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_home);
        EventBus.getDefault().register(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        switch_id = findViewById(R.id.switch_id);

        mAuth = FirebaseAuth.getInstance();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initView();

        tab = findViewById(R.id.tab_layout);
        tab.addTab(tab.newTab().setIcon(R.drawable.ic_home).setText("Home").setTag(0));
        if (!Constants.isAdmin)
            tab.addTab(tab.newTab().setIcon(R.drawable.ic_request).setText("Patients").setTag(1));
        tab.addTab(tab.newTab().setIcon(R.drawable.ic_blood).setText("Donation").setTag(2));
        tab.addTab(tab.newTab().setIcon(R.drawable.banks).setText("Banks").setTag(3));
        tab.addTab(tab.newTab().setIcon(R.drawable.hospital).setText("Hospitals").setTag(4));
        initSelectedTab();


        setDefualtFragment(new MainFragment());

    }

    private void initView() {
        navigationView = findViewById(R.id.navigatioView);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setOnClickListener(HomeActivity.this);
        tab = findViewById(R.id.tab_layout);
        homeFragmentContainer = findViewById(R.id.homeFragment_container);
        menueBtn = findViewById(R.id.menue_btn);
        menueBtn.setOnClickListener(HomeActivity.this);

        hView = navigationView.getHeaderView(0);


        if (!Constants.isAdmin) {
            navigationView.getMenu().findItem(R.id.addBank).setVisible(false);
            navigationView.getMenu().findItem(R.id.addHospital).setVisible(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void initSelectedTab() {
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ((int) tab.getTag() == 0) {
                    setDefualtFragment(new MainFragment());
                } else if ((int) tab.getTag() == 1) {
                    setDefualtFragment(new PostRequestsFragment());
                } else if ((int) tab.getTag() == 2) {
                    setDefualtFragment(new DonnerFragment());
                } else if ((int) tab.getTag() == 3) {
                    setDefualtFragment(new BanksFragment());
                } else if ((int) tab.getTag() == 4) {
                    setDefualtFragment(new HospitalsFragment());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if ((int) tab.getTag() == 0) {
                    setDefualtFragment(new MainFragment());
                } else {
                    setDefualtFragment(new PostRequestsFragment());
                }
            }
        });


    }

    @Subscribe
    public void onFragmentSwitched(EventSwitchFragment fragment) {
        if (fragment != null) {
            tab.getTabAt(fragment.getFragmentTag()).select();
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (tab.getSelectedTabPosition() == 0) {
                super.onBackPressed();
            } else {
                tab.getTabAt(0).select();
            }
        }

    }

    public static Uri downloadUri;
    CircularImageView nav_Image;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap imageBitmap = null;
        nav_Image = navigationView
                .findViewById(R.id.profileImageHeader);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            //encodeBitmapAndSaveToFirebase(imageBitmap);
        } else if (requestCode == REQUEST_IMAGE_FROMGALLERY &&
                resultCode == RESULT_OK) {

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(HomeActivity.
                        this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(HomeActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
        }


        nav_Image.setImageBitmap(imageBitmap);
        uploadImageTOStorage(Constants.currentUser.getName());

    }


    public void uploadImageTOStorage(String currentUserImage) {
        StorageReference images = StrogeBuilder.getStorageRef()
                .child(currentUserImage + ".jpg");
        nav_Image.setDrawingCacheEnabled(true);
        nav_Image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) nav_Image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = images.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(HomeActivity.this, "uploaded successfully", Toast.LENGTH_SHORT);
        });

        // uploadTask = images.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            return images.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                downloadUri = task.getResult();
            }
        });

    }

    public void selectphotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_FROMGALLERY);
    }


    public void setDefualtFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeFragment_container, fragment)
                .commit();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.banks) {
            tab.getTabAt(3).select();

        } else if (id == R.id.hospitals) {
            tab.getTabAt(4).select();
        } else if (id == R.id.addBank) {
            Intent intent = new Intent(HomeActivity.this, AddNewBankActivity.class);
            startActivity(intent);
        } else if (id == R.id.addHospital) {
            Intent intent = new Intent(HomeActivity.this, AddNewHospitalActivity.class);
            startActivity(intent);
        } else if (id == R.id.map) {
            setDefualtFragment(new MapFragment());
        } else if (id == R.id.reports) {
            Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            mAuth.signOut();
            Constants.isAdmin = false;
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.profile) {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.navigatioView) {

        } else if (view.getId() == R.id.profileImageHeader) {
            selectphotoFromGallery();
        } else if (view.getId() == R.id.menue_btn) {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.openDrawer(GravityCompat.START);
            else drawerLayout.closeDrawer(GravityCompat.END);

        }
    }

}









