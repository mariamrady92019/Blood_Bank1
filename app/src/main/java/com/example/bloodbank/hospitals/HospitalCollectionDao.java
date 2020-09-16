package com.example.bloodbank.hospitals;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bloodbank.activities.HospitalsFragment;
import com.example.bloodbank.fireStoreDataBase.FireStoreBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HospitalCollectionDao {

    public static final String HOSPITALS_REF = "hospitals";

    public static CollectionReference getHospitalsRefference() {

        return FireStoreBuilder.getFireStoreInstance().collection(HOSPITALS_REF);

    }


    public static void addHospitalToDataBse(HospitalModel hospital, OnCompleteListener onCompleteListener) {
        DocumentReference reference =
                getHospitalsRefference().document();
        hospital.setId(reference.getId());
        reference.set(hospital)
                .addOnCompleteListener(onCompleteListener);
    }


    public static ArrayList<HospitalModel> getListOfHospitals() {

        ArrayList<HospitalModel> list = new ArrayList<>();
        getHospitalsRefference().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(HospitalModel.class));
                            }
                            HospitalsFragment.adapter.notifyDataSetChanged();
                            Log.d(TAG, list.toString());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return list;


    }


    public static void updateHospitalById(String id, HospitalModel hospital, OnCompleteListener onCompleteListener) {
        getHospitalsRefference()
                .document(id)
                .set(hospital).addOnCompleteListener(onCompleteListener);
    }


    public static void deleteHospitalById(Context con, String id) {
        getHospitalsRefference().document(id)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(con, "hospital deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(con, "failed to delete hospital", Toast.LENGTH_SHORT).show());

    }

    public static void updatePost(String postId, String uID, Runnable runnable) {
        getHospitalsRefference()
                .document(postId)
                .update("requests", FieldValue.arrayUnion(uID))
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        Log.d("OnComplete", String.valueOf(task.isComplete()));
                    }
                }).addOnFailureListener(e -> Log.d("OnFailure", String.valueOf(e.getLocalizedMessage())));
        runnable.run();
    }
}