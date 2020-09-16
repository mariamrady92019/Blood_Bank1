package com.example.bloodbank.Base;

import android.content.Context;

import com.example.bloodbank.Utils.CallbackResult;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;

public interface HelperInterFace {

    void createUser(UserResponse user, Context context, CallbackResult callback);

    void loginUser(String email, String password, Context context,CallbackResult callback);

    boolean isEmailValid(String email);

    boolean isValidPhoneNumber(String phone);


}
