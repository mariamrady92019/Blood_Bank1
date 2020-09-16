package com.example.bloodbank.fireStoreDataBase.posts;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.bloodbank.activities.PostsFragment;
import com.example.bloodbank.fireStoreDataBase.FireStoreBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostsCollectionDao {

    public static final String POSTS_REF = "posts";

    public static CollectionReference getPostsRefference() {

        return FireStoreBuilder.getFireStoreInstance().collection(POSTS_REF);

    }


    public static void addPostToDataBse(PostsModel post, OnCompleteListener onCompleteListener) {
        DocumentReference reference =
                getPostsRefference().document();
        post.setId(reference.getId());
        reference.set(post)
                .addOnCompleteListener(onCompleteListener);
    }


    public ArrayList<PostsModel> getListOfPosts() {

        ArrayList<PostsModel> list = new ArrayList<>();
        FireStoreBuilder.getFireStoreInstance().collection(POSTS_REF).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            new Gson().fromJson(document.toString(), PostsModel.class
                            list.add(document.toObject(PostsModel.class));
                        }
                        PostsFragment.adapter.notifyDataSetChanged();
                        Log.d(TAG, list.toString());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        return list;
    }


    public static ArrayList<PostsModel> getPostsOrderdBYNamme() {
        ArrayList<PostsModel> list = new ArrayList<>();
        getPostsRefference().orderBy("name",
                Query.Direction.ASCENDING).limit(20).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(PostsModel.class));
                        }
                        PostsFragment.adapter.notifyDataSetChanged();
                        Log.d(TAG, list.toString());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        return list;
    }


    public static ArrayList<PostsModel> getPostsOrderdByBloodType() {
        ArrayList<PostsModel> list = new ArrayList<>();
        getPostsRefference().orderBy("bloodType",
                Query.Direction.ASCENDING).limit(20).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(PostsModel.class));
                        }
                        PostsFragment.adapter.notifyDataSetChanged();
                        Log.d(TAG, list.toString());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        return list;
    }

    public static ArrayList<PostsModel> getPostsOrderdByAdress() {
        ArrayList<PostsModel> list = new ArrayList<>();
        getPostsRefference().orderBy("address",
                Query.Direction.ASCENDING).limit(20).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(PostsModel.class));
                        }
                        PostsFragment.adapter.notifyDataSetChanged();
                        Log.d(TAG, list.toString());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        return list;
    }


    public static void updatePostById(String id, PostsModel postsModel,
                                      OnCompleteListener onCompleteListener) {
        getPostsRefference()
                .document(id)
                .set(postsModel).addOnCompleteListener(onCompleteListener);
    }


    public void deletePost(Context con, String id) {
        getPostsRefference().document(id)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(con, "Post deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(con, "failed to delete post", Toast.LENGTH_SHORT).show());

    }

    public void updatePost(String postId, String uID, Runnable runnable) {
        getPostsRefference()
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
