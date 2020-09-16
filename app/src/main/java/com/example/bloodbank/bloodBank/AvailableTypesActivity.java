package com.example.bloodbank.bloodBank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbank.R;

/**
 * Created By Mohamed El Banna On 6/26/2020
 **/
public class AvailableTypesActivity extends AppCompatActivity {

    BloodBankModel mBloodBankModel = new BloodBankModel();

    private TextView tv_TpeA1;
    private TextView tv_TpeA2;
    private TextView tv_TpeB1;
    private TextView tv_TpeB2;
    private TextView tv_TpeO1;
    private TextView tv_TpeO2;
    private TextView tv_TpeAB1;
    private TextView tv_TpeAB2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_types);
        setUp();
    }

    private void setUp() {
        TextView tvBack=findViewById(R.id.tv_back);
        tvBack.setOnClickListener(view -> onBackPressed());
        initView();
        prepareIntent(getIntent());
    }

    private void initView() {
        tv_TpeA1 = findViewById(R.id.tv_type_a1);
        tv_TpeA2 = findViewById(R.id.tv_type_a2);
        tv_TpeB1 = findViewById(R.id.tv_type_b1);
        tv_TpeB2 = findViewById(R.id.tv_type_b2);
        tv_TpeO1 = findViewById(R.id.tv_type_o1);
        tv_TpeO2 = findViewById(R.id.tv_type_o2);
        tv_TpeAB1 = findViewById(R.id.tv_type_ab1);
        tv_TpeAB2 = findViewById(R.id.tv_type_ab2);
    }

    private void prepareIntent(Intent intent) {
        if (intent.hasExtra("bank")) {
            mBloodBankModel = (BloodBankModel) intent.getSerializableExtra("bank");
            tv_TpeA1.setText(String.valueOf(mBloodBankModel.getNumberOf_A()));
            tv_TpeA2.setText(String.valueOf(mBloodBankModel.getNumberOf_A2()));
            tv_TpeB1.setText(String.valueOf(mBloodBankModel.getNumberOf_B()));
            tv_TpeB2.setText(String.valueOf(mBloodBankModel.getNumberOf_B2()));
            tv_TpeAB1.setText(String.valueOf(mBloodBankModel.getNumberOf_AB()));
            tv_TpeAB2.setText(String.valueOf(mBloodBankModel.getNumberOf_AB2()));
            tv_TpeO1.setText(String.valueOf(mBloodBankModel.getNumberOf_O()));
            tv_TpeO2.setText(String.valueOf(mBloodBankModel.getNumberOf_O2()));
        }
    }


}
