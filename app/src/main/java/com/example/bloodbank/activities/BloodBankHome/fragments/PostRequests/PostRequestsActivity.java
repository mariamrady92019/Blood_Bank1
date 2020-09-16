package com.example.bloodbank.activities.BloodBankHome.fragments.PostRequests;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.fireStoreDataBase.PostRequest.PostRequestDao;
import com.example.bloodbank.fireStoreDataBase.PostRequest.PostRequestResponse;
import com.example.bloodbank.fireStoreDataBase.posts.BaseActivityA;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/12/2020
 **/
public class PostRequestsActivity extends BaseActivityA implements PostRequestsAdapter.RequestAdapterCallBack {
    @BindView(R.id.rv_pending_requests)
    RecyclerView mRecyclerView;

    PostRequestsAdapter mAdapter;

    List<PostRequestResponse> mPostRequestResponse;

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

        mAdapter = new PostRequestsAdapter(new ArrayList<>());
        mAdapter.setCallBack(this);

        mRecyclerView.setAdapter(mAdapter);

        PostRequestDao.getPostRequestList(result -> {
            mPostRequestResponse = (List<PostRequestResponse>) result;
            if (mPostRequestResponse != null)
                mAdapter.addItems(mPostRequestResponse);
        });
    }

    @Override
    public void updateBloodBankModel(String bankID) {

    }

    @Override
    public void updateCurrentUserResponse(String userID) {

    }
}
