package com.example.bloodbank.activities.donner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.Base.BaseActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerDao;
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerResponse;

import java.util.ArrayList;
import java.util.List;

public class DonnerFragment extends BaseActivity {

    RecyclerView mRecyclerView;
    DonnerAdapter mAdapter;
    LinearLayoutManager linearLayoutManager ;

    View view;

    public DonnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blood_types, container, false);
        initView();
        return view;
    }


    void initView() {
        mRecyclerView = view.findViewById(R.id.rv_blood_types);

     linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new DonnerAdapter(new ArrayList<>());

        mRecyclerView.setAdapter(mAdapter);

        DonnerDao.getDonnerList(result -> {
            mAdapter.addItems((List<DonnerResponse>) result);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }
}
