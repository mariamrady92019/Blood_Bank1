package com.example.bloodbank.fireStoreDataBase.users;

import com.example.bloodbank.Utils.CallbackResult;
import com.example.bloodbank.fireStoreDataBase.FireStoreBuilder;
import com.example.bloodbank.fireStoreDataBase.history.HistoryResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;

import java.util.Map;

public class UsersCollectionDao {

    public static final String USERS_REF = "users";

    public static CollectionReference getUsersRefference() {

        return FireStoreBuilder.getFireStoreInstance().collection(USERS_REF);

    }


    public static void addUserToDataBse(UserResponse user, OnCompleteListener onCompleteListener) {

        getUsersRefference().document(user.getId()).set(user).addOnCompleteListener(onCompleteListener);
    }


    public static void getCurrentUser(String userId, OnCompleteListener onCompleteListener) {

        getUsersRefference()
                .document(userId)
                .get().addOnCompleteListener(onCompleteListener);
    }

    public static void updateUser(String userId, Map<String, Object> stringObjectMap, OnCompleteListener mlListener) {
        getUsersRefference()
                .document(userId)
                .update(stringObjectMap).addOnCompleteListener(mlListener);
    }

    public static void updateIsDonorUser(String userId, String isDonor, CallbackResult mCallback) {
        String key = "donation";
        getUsersRefference()
                .document(userId)
                .update(key, isDonor).addOnCompleteListener(task -> {
            mCallback.onResult(task.isComplete());
        });
    }

    public static void getUserById(String userId, CallbackResult mCallback) {

        getUsersRefference()
                .document(userId)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mCallback.onResult(task.getResult().toObject(UserResponse.class));
            }
        });
    }

    public static void setUserHistory(String userId, HistoryResponse mHistoryResponse, CallbackResult mCallback) {
        getUsersRefference()
                .document(userId)
                .collection("history").add(mHistoryResponse).addOnCompleteListener(task -> {
            mCallback.onResult(task.isComplete());
        });
    }
}



