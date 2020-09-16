package com.example.bloodbank.fireStoreDataBase.history;

import com.example.bloodbank.Utils.CallbackResult;
import com.example.bloodbank.fireStoreDataBase.FireStoreBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created By Mohamed El Banna On 6/27/2020
 **/
public class HistoryDao {

    private static final String Donner_REF = "history";


    private static CollectionReference getReference() {
        return FireStoreBuilder.getFireStoreInstance().collection(Donner_REF);
    }

    public static void addHistory(HistoryResponse historyResponse, CallbackResult mCallback) {
        DocumentReference mDocumentReference;
        if (historyResponse.getId() != null)
            mDocumentReference = getReference().document(historyResponse.getId());
        else
            mDocumentReference = getReference().document();
        mDocumentReference.set(historyResponse)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        mCallback.onResult(task.isComplete());
                });
    }

    public static void updateHistory(String DonnerId, Map<String, Object> stringObjectMap, OnCompleteListener mlListener) {
        getReference()
                .document(DonnerId)
                .update(stringObjectMap).addOnCompleteListener(mlListener);
    }

    public static void deleteHistory(String id, OnCompleteListener mlListener) {
        getReference().document(id)
                .delete()
                .addOnCompleteListener(mlListener);

    }

    public static void getHistoryList(CallbackResult callbackResult) {
        List<HistoryResponse> mHistoryResponseList = new ArrayList<>();
        getReference().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    mHistoryResponseList.add(document.toObject(HistoryResponse.class));
                }
            }
            callbackResult.onResult(mHistoryResponseList);
        });
    }

    public static void getHistoryById(String donorID, CallbackResult mCallback) {
        getReference()
                .document(donorID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mCallback.onResult(task.getResult().toObject(HistoryResponse.class));
            }
        });
    }

    public static void setApproved(String postId, CallbackResult callbackResult) {
        getReference()
                .document(postId)
                .update("approved", true)
                .addOnCompleteListener(task -> {
                    callbackResult.onResult(task.isComplete());
                });
    }
}
