package com.example.bloodbank.bloodBank;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.bloodbank.Utils.CallbackResult;
import com.example.bloodbank.activities.BanksFragment;
import com.example.bloodbank.fireStoreDataBase.FireStoreBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BankCollectionDao {
    public static final String BANKS_REF = "banks";

    private static CollectionReference getBanksRefference() {
        return FireStoreBuilder.getFireStoreInstance().collection(BANKS_REF);
    }


    public static void addBankToDataBse(BloodBankModel bank, OnCompleteListener onCompleteListener) {
        DocumentReference reference;
        if (bank.getId() != null)
            reference = getBanksRefference().document(bank.getId());
        else
            reference = getBanksRefference().document();

        reference.set(bank)
                .addOnCompleteListener(onCompleteListener);
    }


    public static ArrayList<BloodBankModel> getListOfBanks() {
        ArrayList<BloodBankModel> list = new ArrayList<>();
        FireStoreBuilder.getFireStoreInstance().collection(BANKS_REF).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(BloodBankModel.class));
                        }
                        BanksFragment.adapter.notifyDataSetChanged();
                        Log.d(TAG, list.toString());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        return list;
    }

    public static void getBankById(String bankID, CallbackResult mCallback) {
        getBanksRefference().document(bankID).get().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                mCallback.onResult(task.getResult().toObject(BloodBankModel.class));
            }
        });
    }


    public static void updateBankById(String id, BloodBankModel bank, OnCompleteListener onCompleteListener) {
        getBanksRefference()
                .document(id)
                .set(bank).addOnCompleteListener(onCompleteListener);
    }

    public static void updateBankByRequests(String bankId, List<Map<String, Object>> mList, CallbackResult mCallback) {
        getBanksRefference()
                .document(bankId)
                .update("requests", mList)
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        mCallback.onResult((task.isComplete()));
                        Log.d("OnComplete", String.valueOf(task.isComplete()));
                    }
                }).addOnFailureListener(e -> Log.d("OnFailure", String.valueOf(e.getLocalizedMessage())));

    }

    public static void updateBankDonors(String bankId, List<String> mList, CallbackResult mCallback) {
        getBanksRefference()
                .document(bankId)
                .update("donors", mList)
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        mCallback.onResult((task.isComplete()));
                        Log.d("OnComplete", String.valueOf(task.isComplete()));
                    }
                }).addOnFailureListener(e -> Log.d("OnFailure", String.valueOf(e.getLocalizedMessage())));

    }

    public static void updateBloodTypeQuantity(String bankId, long val, String key, CallbackResult mCallback) {
        getBanksRefference()
                .document(bankId)
                .update(key, val)
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        mCallback.onResult((task.isComplete()));
                        Log.d("OnComplete", String.valueOf(task.isComplete()));
                    }
                }).addOnFailureListener(e -> Log.d("OnFailure", String.valueOf(e.getLocalizedMessage())));

    }


    public void deletePost(Context con, String id) {
        getBanksRefference().document(id)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(con, "bank deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(con, "failed to delete bank", Toast.LENGTH_SHORT).show());

    }


}
