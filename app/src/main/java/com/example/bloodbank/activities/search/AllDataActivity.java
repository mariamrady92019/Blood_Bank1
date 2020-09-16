package com.example.bloodbank.activities.search;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.example.bloodbank.bloodBank.BankRecyclerAdapter;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.fireStoreDataBase.FireStoreBuilder;
import com.example.bloodbank.fireStoreDataBase.posts.PostsModel;
import com.example.bloodbank.hospitals.HospitalModel;
import com.example.bloodbank.hospitals.HospitalRecyclerAdapter;
import com.example.bloodbank.postsRecycler.PostsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.bloodbank.bloodBank.BankCollectionDao.BANKS_REF;
import static com.example.bloodbank.fireStoreDataBase.posts.PostsCollectionDao.POSTS_REF;
import static com.example.bloodbank.hospitals.HospitalCollectionDao.HOSPITALS_REF;

public class AllDataActivity extends AppCompatActivity {

    private static final String TAG = "AllDataActivity";

    private ImageView ivBloodRequest;
    private ImageView ivBloodBanks;
    //    private ImageView ivBloodTypes;
    private ImageView ivBloodHospitals;

    private RecyclerView rvBloodRequest;
    private RecyclerView rvBloodBanks;
    //    private RecyclerView rvBloodTypes;
    private RecyclerView rvBloodHospitals;

    private PostsAdapter mPostsAdapter;
    private BankRecyclerAdapter mBankAdapter;
    //    private PostsAdapter mPostsAdapter;
    private HospitalRecyclerAdapter mHospitalAdapter;

    public ArrayList<PostsModel> mPostsList;
    public ArrayList<BloodBankModel> mBanksList;
    public ArrayList<HospitalModel> mHospitalsList;

    LinearLayoutManager mLinearLayoutManager;


    public static CollectionReference getHospitalsRefference() {

        return FireStoreBuilder.getFireStoreInstance().collection(HOSPITALS_REF);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_data);

        setup();
    }

    private void setup() {
        initView();
//        getData();
//        setData();
    }

    private void getData() {
        getListOfPosts();
        getListOfBanks();
        getListOfHospitals();
    }


    void initView() {
        mLinearLayoutManager = new LinearLayoutManager(AllDataActivity.this);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        ivBloodRequest = findViewById(R.id.iv_blood_request_arrow);
        ivBloodBanks = findViewById(R.id.iv_blood_bank_arrow);
        ivBloodHospitals = findViewById(R.id.iv_hospital_arrow);

        rvBloodRequest = findViewById(R.id.rv_blood_requests);
        rvBloodRequest.setLayoutManager(mLinearLayoutManager);

        rvBloodBanks = findViewById(R.id.rv_blood_banks);
        rvBloodBanks.setLayoutManager(mLinearLayoutManager);

        rvBloodHospitals = findViewById(R.id.rv_blood_hospitals);
        rvBloodHospitals.setLayoutManager(mLinearLayoutManager);


        mPostsAdapter = new PostsAdapter(getListOfPosts(), AllDataActivity.this, listener);
        mPostsAdapter.notifyDataSetChanged();
        mBankAdapter = new BankRecyclerAdapter(getListOfBanks(), AllDataActivity.this);
        mBankAdapter.notifyDataSetChanged();
        mHospitalAdapter = new HospitalRecyclerAdapter(getListOfHospitals(), AllDataActivity.this);
        mHospitalAdapter.notifyDataSetChanged();


        rvBloodRequest.setAdapter(mPostsAdapter);
        rvBloodBanks.setAdapter(mBankAdapter);
        rvBloodHospitals.setAdapter(mHospitalAdapter);


    }

    private void setData() {
        mPostsAdapter.addItems(mPostsList);
        mHospitalAdapter.addItems(mHospitalsList);
        mBankAdapter.addItems(mBanksList);
    }

    BloodBankModel bank = new BloodBankModel();

    PostsAdapter.MyAdapterListener listener = new PostsAdapter.MyAdapterListener() {
        @Override
        public void donnateButtonListener(View v, int position) {
            PostsModel post = mPostsList.get(position);
            String bloodType = post.getBloodType();
            String userBloodType = Constants.currentUser.getBloodType();


            if (bloodType.toLowerCase().equals("o")) {
                bank.setNumberOf_O(bank.getNumberOf_O() + 1);
                showMessage("donnated" + bloodType + bank.getNumberOf_O(), "ok", AllDataActivity.this);

            }
            if (bloodType.toLowerCase().equals("AB".toLowerCase())) {
                bank.setNumberOf_AB(bank.getNumberOf_AB() + 1);
                 showMessage("donnated" + bloodType + bank.getNumberOf_AB(), "ok", AllDataActivity.this);

            }
            if (bloodType.toLowerCase().equals("A".toLowerCase())) {
                bank.setNumberOf_A(bank.getNumberOf_A() + 1);
                showMessage("donnated" + bloodType
                        + bank.getNumberOf_A(), "ok", AllDataActivity.this);

            }
            if (bloodType.toLowerCase().equals("B".toLowerCase())) {
                bank.setNumberOf_B(bank.getNumberOf_B() + 1);
                showMessage("donnated" + bloodType + bank.getNumberOf_B(), "ok", AllDataActivity.this);

            }
        }
    };


    public AlertDialog showMessage(String message, String posActionName, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, (dialog, which) -> dialog.dismiss());
        return builder.show();
    }

    public ArrayList<HospitalModel> getListOfHospitals() {

        ArrayList<HospitalModel> list = new ArrayList<>();
        getHospitalsRefference().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(HospitalModel.class));
                            }
                            mHospitalsList = list;
                            Log.d(TAG, list.toString());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return list;
    }

    public ArrayList<BloodBankModel> getListOfBanks() {
        ArrayList<BloodBankModel> list = new ArrayList<>();
        FireStoreBuilder.getFireStoreInstance().collection(BANKS_REF).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(BloodBankModel.class));
                            }
                            mBanksList = list;
                            Log.d(TAG, list.toString());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return list;
    }

    public ArrayList<PostsModel> getListOfPosts() {

        ArrayList<PostsModel> list = new ArrayList<>();
        FireStoreBuilder.getFireStoreInstance().collection(POSTS_REF).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(PostsModel.class));
                            }
                            mPostsList = list;
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return list;
    }
}
