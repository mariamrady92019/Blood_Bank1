package com.example.bloodbank.constant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.bloodbank.Base.BaseActivity;
import com.example.bloodbank.activities.HomeActivity;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.regex.Pattern;

import static com.example.bloodbank.Base.Helper.getFireBaseAuthenticationInstance;

public class Constants extends BaseActivity {

    public static String postId;
    public static UserResponse currentUser = new UserResponse();
    public static String UserId;
    public static FirebaseUser firebaseUser;

    public static Boolean isAdmin = false;



    public static void strartHomeActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);

    }

    public static FirebaseUser getFirebaseUser() {
        FirebaseAuth fb = getFireBaseAuthenticationInstance();
        Constants.firebaseUser =
                fb.getCurrentUser();
        return Constants.firebaseUser;
    }

    @SuppressLint("RestrictedApi")
    // also suppressed the warning
    public static void setUpAddPOstBtn(FloatingActionButton button) {
        if (Constants.isAdmin == false) {

            button.setVisibility(View.GONE);
        }
    }


    public UserResponse user;

    public UserResponse getCurrentUserFromDataBase(Context c,String userId) {

        DocumentReference docRef = UsersCollectionDao.getUsersRefference().document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(UserResponse.class);
                        showMessage("user getted", "ok", c);
                    } else {
                        showMessage("not found // null", "ok", c);
                    }
                } else {
                    showMessage("nulll", "ok", c);
                }
            }
        });
        return user;
    }

    public static void showDialog(Context context, String action) {
        new AlertDialog.Builder(context)
                .setTitle(action)
                .setMessage("Are You Sure You Want To " + action + " ?!")
                .setNegativeButton("Yes", (dialogInterface, i) -> {

                }).setPositiveButton("No", (dialogInterface, i) -> {
        }).show();
    }

}
