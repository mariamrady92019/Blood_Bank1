package com.example.bloodbank.activities.BloodBankHome.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bloodbank.R;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/12/2020
 **/
public class AvailableTypesFragment extends Fragment {


    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rl_tool_bar)
    RelativeLayout rlToolBar;
    @BindView(R.id.tv_type_a1)
    TextView tvTypeA1;
    @BindView(R.id.tv_type_a2)
    TextView tvTypeA2;
    @BindView(R.id.tv_type_b1)
    TextView tvTypeB1;
    @BindView(R.id.tv_type_b2)
    TextView tvTypeB2;
    @BindView(R.id.tv_type_o1)
    TextView tvTypeO1;
    @BindView(R.id.tv_type_o2)
    TextView tvTypeO2;
    @BindView(R.id.tv_type_ab1)
    TextView tvTypeAb1;
    @BindView(R.id.tv_type_ab2)
    TextView tvTypeAb2;

    FirebaseAuth mAuth;

    BloodBankModel mBloodBankModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_available_types, container, false);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();
        rlToolBar.setVisibility(View.GONE);
        initData();
        return view;
    }

    void initData() {
        BankCollectionDao.getBankById(mAuth.getUid(), result -> {
                    mBloodBankModel = (BloodBankModel) result;
                    tvTypeA1.setText(String.valueOf(mBloodBankModel.getNumberOf_A()));
                    tvTypeA2.setText(String.valueOf(mBloodBankModel.getNumberOf_A2()));
                    tvTypeB1.setText(String.valueOf(mBloodBankModel.getNumberOf_B()));
                    tvTypeB2.setText(String.valueOf(mBloodBankModel.getNumberOf_B2()));
                    tvTypeAb1.setText(String.valueOf(mBloodBankModel.getNumberOf_AB()));
                    tvTypeAb2.setText(String.valueOf(mBloodBankModel.getNumberOf_AB2()));
                    tvTypeO1.setText(String.valueOf(mBloodBankModel.getNumberOf_O()));
                    tvTypeO2.setText(String.valueOf(mBloodBankModel.getNumberOf_O2()));
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
