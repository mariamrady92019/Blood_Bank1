package com.example.bloodbank.activities.BloodBankHome.fragments.pending_requests;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.fireStoreDataBase.posts.BaseActivityA;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/12/2020
 **/
public class PendingRequestsActivity extends BaseActivityA {
    @BindView(R.id.rv_pending_requests)
    RecyclerView mRecyclerView;

    PendingRequestsAdapter mAdapter;

    BloodBankModel mBloodBankModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new PendingRequestsAdapter(new ArrayList<>());

        mRecyclerView.setAdapter(mAdapter);

        BankCollectionDao.getBankById(FirebaseAuth.getInstance().getUid(), result -> {
            mBloodBankModel = (BloodBankModel) result;
            if (mBloodBankModel.getRequests() != null)
                mAdapter.addItems(mBloodBankModel.getRequests());
        });
    }
}
