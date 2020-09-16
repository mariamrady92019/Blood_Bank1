package com.example.bloodbank.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bloodbank.Base.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.activities.BloodBankHome.BloodBankHome;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UserTypes;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class loginFragment extends Fragment implements View.OnClickListener {


    private View rootView;
    private TextInputEditText userName;
    private TextInputEditText password;
    private Button submit;
    private Helper Helper = new Helper();

    public loginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_loginfragment, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(rootView);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            String usernameText = userName.getText().toString();
            String passwordText = password.getText().toString();

            if (checkValidation(usernameText, passwordText)) {
                Constants.isAdmin = usernameText.contains("admin@admin.com");

                Helper.loginUser(usernameText, passwordText, getActivity(), result -> {
                    if ((boolean) result) {
                        UsersCollectionDao.getUserById(FirebaseAuth.getInstance().getUid(), task2 -> {

                            UserResponse mUserResponse = (UserResponse) task2;
                            if (mUserResponse.getUserType().equals(UserTypes.USER_TYPE_BLOOD_BANK.getType())) {
                                if (!mUserResponse.isExist())
                                    startActivity(new Intent(getContext(), AddNewBankActivity.class).putExtra(AddNewBankActivity.INTENT_BANK_ID, mUserResponse.getId()).putExtra("address", mUserResponse.getAddress()));
                                else
                                    startActivity(new Intent(getContext(), BloodBankHome.class));
                            } else {
                                startActivity(new Intent(getContext(), HomeActivity.class));
                            }

                        });
                    }
                });


            }
        }
    }


    private boolean checkValidation(String usernameText, String passwordText) {


        boolean isValid = true;

        if (passwordText.trim().isEmpty()) {
            this.password.setError("required");
            isValid = false;
        } else if (passwordText.length() < 6) {
            this.password.setError("password should be > 6 chars");
            isValid = false;
        } else {
            this.password.setError(null);
            isValid = true;
        }


        if (Helper.isEmailValid(usernameText.trim()) == false) {
            userName.setError("not valid email");
            isValid = false;

        } else {
            userName.setError(null);
            isValid = true;
        }


        return isValid;


    }


    private void initView(View rootView) {
        userName = rootView.findViewById(R.id.user_name);
        password = rootView.findViewById(R.id.password);
        submit = rootView.findViewById(R.id.submit);
        submit.setOnClickListener(loginFragment.this);
    }
}
