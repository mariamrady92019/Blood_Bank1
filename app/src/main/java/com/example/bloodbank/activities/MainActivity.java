package com.example.bloodbank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.BloodBankHome.BloodBankHome;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UserTypes;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button loginBtn;
    protected Button signInBtn;
    protected LinearLayout fragmentContainer;


    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
        setDefualtFragment();


        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            UsersCollectionDao.getCurrentUser(mAuth.getUid(), task -> {
                if (task.isSuccessful()) {
                    UserResponse mUserResponse = ((DocumentSnapshot) task.getResult()).toObject(UserResponse.class);
                    if (mUserResponse == null)
                        return;
                    if (mUserResponse.getUserType().equals(UserTypes.USER_TYPE_BLOOD_BANK.getType())) {
                        startActivity(new Intent(MainActivity.this, BloodBankHome.class));
                        finish();
                    } else {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    }
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_btn) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.fragment_container, new loginFragment())
                    .commit();

        } else if (view.getId() == R.id.signIn_btn) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .commit();
        }
    }

    private void initView() {
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(MainActivity.this);
        signInBtn = (Button) findViewById(R.id.signIn_btn);
        signInBtn.setOnClickListener(MainActivity.this);
        fragmentContainer = (LinearLayout) findViewById(R.id.fragment_container);


    }


    public void setDefualtFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("")
                .replace(R.id.fragment_container, new loginFragment())
                .commit();

    }


}
