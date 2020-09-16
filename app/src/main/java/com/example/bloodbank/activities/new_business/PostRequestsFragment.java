package com.example.bloodbank.activities.new_business;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.BloodBankHome.fragments.PostRequests.PostRequestsAdapter;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.fireStoreDataBase.PostRequest.PostRequestDao;
import com.example.bloodbank.fireStoreDataBase.PostRequest.PostRequestResponse;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/14/2020
 **/
public class PostRequestsFragment extends Fragment implements PostRequestsAdapter.RequestAdapterCallBack {

    @BindView(R.id.rv_pending_requests)
    RecyclerView mRecyclerView;

    PostRequestsAdapter mAdapter;

    List<PostRequestResponse> mPostRequestResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pending_requests, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new PostRequestsAdapter(new ArrayList<>());
        mAdapter.setCallBack(this);

        mRecyclerView.setAdapter(mAdapter);

        PostRequestDao.getPostRequestList(result -> {
            mPostRequestResponse = (List<PostRequestResponse>) result;
            mAdapter.addItems(mPostRequestResponse);
        });
    }

    @Override
    public void updateBloodBankModel(String bankID) {
//        BankCollectionDao.getBankById(bankID, result -> {
//            mAdapter.setBloodBankModel((BloodBankModel) result);
//        });
    }

    @Override
    public void updateCurrentUserResponse(String userID) {
//        UsersCollectionDao.getUserById(userID, result -> {
//            mAdapter.setCurrentUserResponse((UserResponse) result);
//        });
    }
}
