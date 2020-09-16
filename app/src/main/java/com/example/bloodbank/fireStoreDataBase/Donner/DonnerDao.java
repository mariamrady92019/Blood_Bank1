package com.example.bloodbank.fireStoreDataBase.Donner;

import com.example.bloodbank.Utils.CallbackResult;
import com.example.bloodbank.fireStoreDataBase.FireStoreBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created By Mohamed El Banna On 6/27/2020
 **/
public class DonnerDao {

    private static final String Donner_REF = "donor";


    private static CollectionReference getReference() {
        return FireStoreBuilder.getFireStoreInstance().collection(Donner_REF);
    }

    public static void addDonner(DonnerResponse donnerResponse, OnCompleteListener mListener, OnFailureListener mOnFailureListener) {
        DocumentReference mDocumentReference;
        if (donnerResponse.getId() != null)
            mDocumentReference = getReference().document(donnerResponse.getId());
        else
            mDocumentReference = getReference().document();
        mDocumentReference.set(donnerResponse)
                .addOnCompleteListener(mListener).addOnFailureListener(mOnFailureListener);
    }

    public static void updateDonner(String DonnerId, Map<String, Object> stringObjectMap, OnCompleteListener mlListener) {
        getReference()
                .document(DonnerId)
                .update(stringObjectMap).addOnCompleteListener(mlListener);
    }

    public static void deleteDonner(String id, OnCompleteListener mlListener) {
        getReference().document(id)
                .delete()
                .addOnCompleteListener(mlListener);

    }

    public static void getDonnerList(CallbackResult callbackResult) {
        List<DonnerResponse> mDonnerResponseList = new ArrayList<>();
        getReference().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    mDonnerResponseList.add(document.toObject(DonnerResponse.class));
                }
            }
            callbackResult.onResult(mDonnerResponseList);
        });
    }

    public static void getDonorById(String donorID, CallbackResult mCallback) {
        getReference()
                .document(donorID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mCallback.onResult(task.getResult().toObject(DonnerResponse.class));
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
