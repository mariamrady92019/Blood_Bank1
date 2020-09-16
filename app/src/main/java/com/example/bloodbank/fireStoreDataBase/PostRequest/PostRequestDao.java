package com.example.bloodbank.fireStoreDataBase.PostRequest;

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
public class PostRequestDao {

    private static final String POST_REQUEST = "PostRequest";


    private static CollectionReference getReference() {
        return FireStoreBuilder.getFireStoreInstance().collection(POST_REQUEST);
    }

    public static void addPostRequest(PostRequestResponse postResponse, CallbackResult callbackResult) {
        DocumentReference mDocumentReference;
        if (postResponse.getId() != null)
            mDocumentReference = getReference().document(postResponse.getId());
        else
            mDocumentReference = getReference().document();
        mDocumentReference.set(postResponse)
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        callbackResult.onResult(task.isComplete());
                    }
                });
    }

    public static void updatePostRequest(String PostRequestId, Map<String, Object> stringObjectMap, OnCompleteListener mlListener) {
        getReference()
                .document(PostRequestId)
                .update(stringObjectMap).addOnCompleteListener(mlListener);
    }

    public static void deletePostRequest(String id, OnCompleteListener mlListener) {
        getReference().document(id)
                .delete()
                .addOnCompleteListener(mlListener);

    }

    public static void getPostRequestList(CallbackResult callbackResult) {
        List<PostRequestResponse> mPostRequestResponseList = new ArrayList<>();
        getReference().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    mPostRequestResponseList.add(document.toObject(PostRequestResponse.class));
                }
            }
            callbackResult.onResult(mPostRequestResponseList);
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
