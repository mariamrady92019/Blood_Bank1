package com.example.bloodbank.activities;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.Base.BaseActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.activities.search.SearchActivity;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BankRecyclerAdapter;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.events.SearchEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BanksFragment extends BaseActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    SearchView simpleSearchView;
    protected View rootView;
    protected RecyclerView BanksRecycler;
    protected FloatingActionButton addNewBank;
    View view;
    RecyclerView.LayoutManager linLayoutManager;
    public static BankRecyclerAdapter adapter;
    ArrayList<BloodBankModel> listOfBanks;


    ImageView ivFilter;

    TextView tvClearSearch;

    public BanksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_banks, container, false);
        initView(view);
        listOfBanks = BankCollectionDao.getListOfBanks();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listOfBanks == null) {

        } else {

            buildRecycler(listOfBanks);
            adapter.changeAdapterList(listOfBanks);
            adapter.notifyDataSetChanged();
            Constants.setUpAddPOstBtn(addNewBank);
        }


    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addNewBank) {
            Intent intent = new Intent(getActivity(), AddNewBankActivity.class);
            super.startActivity(intent);
        }
    }

    private void initView(View rootView) {

        BanksRecycler = rootView.findViewById(R.id.Banks_Recycler);
        BanksRecycler.setOnClickListener(BanksFragment.this);
        addNewBank = rootView.findViewById(R.id.addNewBank);
        addNewBank.setOnClickListener(BanksFragment.this);
        ivFilter = rootView.findViewById(R.id.iv_filter);
        simpleSearchView = rootView.findViewById(R.id.search_bar); // inititate a search view
        simpleSearchView.setOnQueryTextListener(this);
        tvClearSearch = rootView.findViewById(R.id.tv_clear);
        tvClearSearch.setPaintFlags(tvClearSearch.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvClearSearch.setOnClickListener(v -> {
            adapter.clearFilter();
            simpleSearchView.clearFocus();
        });

        ivFilter.setOnClickListener(view -> startActivity(new Intent(getContext(), SearchActivity.class)));

    }

    public void buildRecycler(ArrayList<BloodBankModel> list) {
        BanksRecycler = view.findViewById(R.id.Banks_Recycler);
        linLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new BankRecyclerAdapter(listOfBanks, getActivity());
        BanksRecycler.setLayoutManager(linLayoutManager);
        BanksRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // BanksRecycler.setHasFixedSize(true);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query, query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Subscribe
    public void onSearchEvent(SearchEvent event) {
        if (event != null) {
            adapter.filter(event.getCity(), event.getBloodType());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
