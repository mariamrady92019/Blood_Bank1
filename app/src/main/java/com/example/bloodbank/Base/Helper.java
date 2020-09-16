package com.example.bloodbank.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bloodbank.Utils.CallbackResult;
import com.example.bloodbank.activities.AddNewBankActivity;
import com.example.bloodbank.activities.BloodBankHome.BloodBankHome;
import com.example.bloodbank.activities.HomeActivity;
import com.example.bloodbank.activities.MainActivity;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UserTypes;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.regex.Pattern;

public class Helper extends BaseActivity implements HelperInterFace {
    //here FireBaseAuthintcation object
    static FirebaseAuth fb;
    UserResponse user = new UserResponse();
    static UserResponse mResponse;

    public static FirebaseAuth getFireBaseAuthenticationInstance() {
        if (fb == null) {
            fb = FirebaseAuth.getInstance();
        }
        return fb;
    }


    public static UserResponse selectUserFromDataBaseById(String userid) {
        mResponse = new UserResponse();
        UsersCollectionDao.getCurrentUser(userid, task -> {
            if (task.isSuccessful()) {
                mResponse = ((DocumentSnapshot) task.getResult()).toObject(UserResponse.class);
            }
        });
        return mResponse;

    }

    public void createUser(UserResponse user, Context context, CallbackResult callback) {
        //get insttance to work with
        fb = getFireBaseAuthenticationInstance();

        showProgressDialog("please wait...", context);

        //function from documentation to create user for authentication only,need to add to database
        fb.createUserWithEmailAndPassword(user.getMail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user1 = fb.getCurrentUser();

                        user.setId(user1.getUid());
                        Constants.currentUser.setId(user1.getUid());
                        addUserToDataBase(user, context);
                        callback.onResult(user1.getUid());

                    } else {
                        hideProgressDialog();
                        String message = task.getException().getLocalizedMessage();
                        // If sign in fails, display a message to the user.
                        if (message.contains("in use by another account")) {
                            showMessage("email already used before", "ok", context).show();
                        } else {
                            showMessage("failed conect to server", "ok", context).show();

                        }


                    }
                });

    }


    private void addUserToDataBase(UserResponse user, Context context) {

        Constants.currentUser = user;

        UsersCollectionDao.addUserToDataBse(user, task -> {

            hideProgressDialog();
            if (task.isSuccessful()) {

                showMessage("user Created successFully", "ok", (dialogInterface, i) -> Constants.strartHomeActivity(context), context).show();
            } else {
                hideProgressDialog();

                showMessage("" + task.getException().getLocalizedMessage(), "ok", context).show();
            }
        });
    }

    public MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    //logined User by already Exist Account
    public void loginUser(String email, String password, Context context,CallbackResult callback) {

        showProgressDialog("please wait...", context);
        fb = getFireBaseAuthenticationInstance();
        fb.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        hideProgressDialog();
                        // Sign in success, update UI with the signed-in user's information
                        Constants.currentUser.setId(FirebaseAuth.getInstance().getUid());
                        Toast.makeText(context, "logined successfully",
                                Toast.LENGTH_LONG).show();
                        callback.onResult(task.isComplete());

                    } else {
                        hideProgressDialog();
                        showMessage("user might not exist",
                                "ok", context).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    public boolean isValidPhoneNumber(String phone) {
        if (!phone.trim().equals("") && phone.length() > 10) {
            return Patterns.PHONE.matcher(phone).matches();
        }
        return false;
    }

    public boolean isEmailValid(String email) {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }


}
