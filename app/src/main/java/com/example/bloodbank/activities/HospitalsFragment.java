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
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.events.SearchEvent;
import com.example.bloodbank.hospitals.HospitalCollectionDao;
import com.example.bloodbank.hospitals.HospitalModel;
import com.example.bloodbank.hospitals.HospitalRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HospitalsFragment extends BaseActivity implements View.OnClickListener, SearchView.OnQueryTextListener {


    protected RecyclerView hospitalsRecycler;
    protected FloatingActionButton addNewHospitals;
    View view;
    ArrayList<HospitalModel> listOfHospitals;
    RecyclerView.LayoutManager layoutManager;
    public static HospitalRecyclerAdapter adapter;


    ImageView ivFilter;
    SearchView simpleSearchView;
    TextView tvClearSearch;


    public HospitalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_hospitals, container, false);
        initView(view);
        listOfHospitals = HospitalCollectionDao.getListOfHospitals();
        EventBus.getDefault().register(this);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (listOfHospitals == null) {
            //showMessage("null","ok",getActivity());
        } else {
            buildRecycler(listOfHospitals);
            adapter.changeAdapterList(listOfHospitals);
            adapter.notifyDataSetChanged();
        }

        Constants.setUpAddPOstBtn(addNewHospitals);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addNewHospitals) {
            Intent intent = new Intent(getActivity(), AddNewHospitalActivity.class);
            super.startActivity(intent);
        }
    }

    private void initView(View rootView) {
        hospitalsRecycler = (RecyclerView) rootView.findViewById(R.id.hospitalsRecycler);
        hospitalsRecycler.setOnClickListener(this);

        addNewHospitals = (FloatingActionButton) rootView.findViewById(R.id.addNewHospitals);
        addNewHospitals.setOnClickListener(HospitalsFragment.this);
        simpleSearchView = (SearchView) rootView.findViewById(R.id.search_bar); // inititate a search view
        ivFilter = (ImageView) rootView.findViewById(R.id.iv_filter);
        simpleSearchView.setOnQueryTextListener(this);
        tvClearSearch = rootView.findViewById(R.id.tv_clear);
        tvClearSearch.setPaintFlags(tvClearSearch.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvClearSearch.setOnClickListener(v -> {
            adapter.clearFilter();
            simpleSearchView.clearFocus();
        });

        ivFilter.setOnClickListener(view -> startActivity(new Intent(getContext(), SearchActivity.class)));

    }


    public void buildRecycler(ArrayList<HospitalModel> list) {
        hospitalsRecycler = (RecyclerView) view.findViewById(R.id.hospitalsRecycler);
        layoutManager = new LinearLayoutManager(getActivity());

        adapter = new HospitalRecyclerAdapter(list, getActivity());
        hospitalsRecycler.setAdapter(adapter);
        hospitalsRecycler.setLayoutManager(layoutManager);

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
