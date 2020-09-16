package com.example.bloodbank.activities.BloodBankHome.fragments.history;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.example.bloodbank.fireStoreDataBase.history.HistoryDao;
import com.example.bloodbank.fireStoreDataBase.history.HistoryResponse;
import com.example.bloodbank.fireStoreDataBase.posts.BaseActivityA;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/12/2020
 **/
public class HistoryActivity extends BaseActivityA {
    @BindView(R.id.rv_pending_requests)
    RecyclerView mRecyclerView;

    HistoryAdapter mAdapter;

    HistoryResponse mHistoryResponse;

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

        mAdapter = new HistoryAdapter(new ArrayList<>());

        mRecyclerView.setAdapter(mAdapter);

        HistoryDao.getHistoryById(FirebaseAuth.getInstance().getUid(), result -> {
            mHistoryResponse = (HistoryResponse) result;
            if (mHistoryResponse != null && mHistoryResponse.getHistory() != null)
                mAdapter.addItems(mHistoryResponse.getHistory());
        });
    }
}
