package com.example.bloodbank.activities.search;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbank.R;
import com.example.bloodbank.events.SearchEvent;

import org.greenrobot.eventbus.EventBus;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvBack;
    private Spinner spinnerCity;
    private Spinner spinnerBloodType;
    private Spinner spinnerArea;
    private Button btnSearch;

    private int selectedCity = 1;
    private int selectedBloodType = -1;
    private int selectedArea = -1;

    ArrayAdapter adapterCity;
    ArrayAdapter adapterBloodType;
    ArrayAdapter adapterArea;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        initView();

    }


    private void initView() {
        tvBack = findViewById(R.id.tv_back);
        spinnerCity = findViewById(R.id.spinner_city);
        spinnerBloodType = findViewById(R.id.spinner_blood_type);
        spinnerArea = findViewById(R.id.spinner_area);
        btnSearch = findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(this);
        tvBack.setOnClickListener(this);


        adapterCity = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.array_city));
        spinnerCity.setAdapter(adapterCity);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCity = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterBloodType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.bloodTypes_array));
        spinnerBloodType.setAdapter(adapterBloodType);
        spinnerBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBloodType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterArea = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.areas_array));
        spinnerArea.setAdapter(adapterArea);
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedArea = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
            case R.id.tv_back:
                EventBus.getDefault().
                        post(new SearchEvent(String.valueOf(adapterArea.getItem(selectedArea)).
                                trim(),
                        String.valueOf(adapterBloodType.getItem(selectedBloodType)).trim()));
                finish();
        }
    }
}
