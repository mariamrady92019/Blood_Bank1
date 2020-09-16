package com.example.bloodbank.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;

import com.example.bloodbank.Base.BaseActivity;
import com.example.bloodbank.R;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.events.EventSwitchFragment;

import org.greenrobot.eventbus.EventBus;


public class MainFragment extends BaseActivity {

    CardView cardBloodRequest;
    CardView cardBloodBanks;
    CardView cardBloodHospital;
    CardView cardBloodTypes;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_main, container, false);

        initView(mView);

        return mView;
    }

    private void initView(View view) {
        cardBloodRequest = view.findViewById(R.id.card_blood_request);
        cardBloodBanks = view.findViewById(R.id.card_blood_banks);
        cardBloodHospital = view.findViewById(R.id.card_blood_hospitals);
        cardBloodTypes = view.findViewById(R.id.card_blood_types);

        if (Constants.isAdmin)
            cardBloodRequest.setVisibility(View.GONE);
        else
            cardBloodRequest.setVisibility(View.VISIBLE);

        cardBloodRequest.setOnClickListener(v -> {
            if (Constants.isAdmin)
                return;
            switchFragment(1);
        });

        cardBloodBanks.setOnClickListener(v -> {
            if (Constants.isAdmin)
                switchFragment(2);
            else
                switchFragment(3);
        });

        cardBloodHospital.setOnClickListener(v -> {
            if (Constants.isAdmin)
                switchFragment(3);
            else
                switchFragment(4);
        });

        cardBloodTypes.setOnClickListener(v -> {
            if (Constants.isAdmin)
                switchFragment(1);
            else
                switchFragment(2);
        });
    }

    public void switchFragment(int fragmentTag) {
        EventBus.getDefault().post(new EventSwitchFragment(fragmentTag));
    }
}
