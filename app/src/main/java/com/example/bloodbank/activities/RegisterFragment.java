package com.example.bloodbank.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bloodbank.Base.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.activities.BloodBankHome.BloodBankHome;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UserTypes;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private Button btnRegister;


    private View rootView;
    private TextInputEditText userName;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText phone;
    private View view;
    private Helper helper = new Helper();
    private TextInputEditText address;
    private RadioGroup rgUserType;
    private RadioButton rbType;
    private LinearLayout ll_blood_type;
    private String userType = UserTypes.USER_TYPE_NORMAL.getType();

    private Spinner spinnerBloodType;
    private int selectedBloodType = -1;
    private ArrayAdapter adapterBloodType;

    FirebaseAuth mAuth;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    private void initView(View rootView) {
        userName = rootView.findViewById(R.id.user_name);
        email = rootView.findViewById(R.id.email);
        password = rootView.findViewById(R.id.password);
        phone = rootView.findViewById(R.id.et_phone_number);
        btnRegister = rootView.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(RegisterFragment.this);
        address = rootView.findViewById(R.id.address);
        spinnerBloodType = rootView.findViewById(R.id.spinner_blood_type);
        rgUserType = rootView.findViewById(R.id.rg_user_type);
        rbType = rootView.findViewById(rgUserType.getCheckedRadioButtonId());
        ll_blood_type = rootView.findViewById(R.id.ll_blood_type);
        adapterBloodType = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, rootView.getResources().getStringArray(R.array.bloodTypes_array));
        spinnerBloodType.setAdapter(adapterBloodType);
        spinnerBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBloodType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rgUserType.setOnCheckedChangeListener((group, checkedId) -> {
            rbType = group.findViewById(checkedId);
            if (rbType != null && checkedId > -1) {

                if (rbType.getId() == R.id.rb_user) {
                    userType = UserTypes.USER_TYPE_NORMAL.getType();
                    ll_blood_type.setVisibility(View.VISIBLE);
                } else {
                    userType = UserTypes.USER_TYPE_BLOOD_BANK.getType();
                    ll_blood_type.setVisibility(View.GONE);
                }
            }

        });

    }

    public UserResponse userInformation() {
        String usernameText = userName.getText().toString();
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        String bloodTypeText = String.valueOf(adapterBloodType.getItem(selectedBloodType)).trim();
        String adressText = address.getText().toString();
        String phoneNumber = phone.getText().toString();
        UserResponse mUserResponse = new UserResponse();
        mUserResponse.setName(usernameText);
        mUserResponse.setMail(emailText);
        mUserResponse.setPassword(passwordText);
        mUserResponse.setBloodType(bloodTypeText);
        mUserResponse.setAddress(adressText);
        mUserResponse.setPhoneNumber(phoneNumber);
        switch (rgUserType.getCheckedRadioButtonId()) {
            case R.id.rb_user:
                userType = UserTypes.USER_TYPE_NORMAL.getType();
                ll_blood_type.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_blood_bank:
                userType = UserTypes.USER_TYPE_BLOOD_BANK.getType();
                ll_blood_type.setVisibility(View.GONE);
                break;
        }
        mUserResponse.setUserType(userType);
        if (userType.equals(UserTypes.USER_TYPE_NORMAL.getType()))
            mUserResponse.setExist(true);
        return mUserResponse;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == btnRegister.getId()) {


            UserResponse mUserResponse = userInformation();


            if (mUserResponse.getName().isEmpty()) {
                showMessage("Name Required");
                return;
            }

            if (mUserResponse.getMail().isEmpty()) {
                showMessage("Email Required");
                return;
            }

            if (!helper.isEmailValid(mUserResponse.getMail())) {
                showMessage("InValid Email");
                return;
            }

            if (mUserResponse.getPassword().isEmpty()) {
                showMessage("Password Required");
                return;
            }

            if (mUserResponse.getPassword().length() < 6) {
                showMessage("Password Must Be 6 Digits or More");
                return;
            }

            if (mUserResponse.getAddress().isEmpty()) {
                showMessage("Address Required");
                return;
            }

            if (mUserResponse.getBloodType().isEmpty()) {
                Toast.makeText(getContext(), "Select BloodType", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mUserResponse.getPhoneNumber().isEmpty()) {
                showMessage("PhoneNumber Required");
                return;
            }

            if (!helper.isValidPhoneNumber(mUserResponse.getPhoneNumber())) {
                showMessage("InValid Phone");
                return;
            }


            helper.createUser(mUserResponse, getContext(), result -> {
                if (mUserResponse.getUserType().equals(UserTypes.USER_TYPE_BLOOD_BANK.getType())) {
                    if (!mUserResponse.isExist())
                        startActivity(new Intent(getContext(), AddNewBankActivity.class).putExtra(AddNewBankActivity.INTENT_BANK_ID, mAuth.getUid()).putExtra("address", mUserResponse.getAddress()));
                    else startActivity(new Intent(getContext(), BloodBankHome.class));
                } else
                    Constants.strartHomeActivity(getActivity());
            });

        }
    }

    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Some Thing Wrong Happened", Toast.LENGTH_SHORT).show();
        }
    }


}