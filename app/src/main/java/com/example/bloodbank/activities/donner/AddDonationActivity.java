package com.example.bloodbank.activities.donner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.bloodbank.R;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerDao;
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerResponse;
import com.example.bloodbank.fireStoreDataBase.history.HistoryDao;
import com.example.bloodbank.fireStoreDataBase.history.HistoryResponse;
import com.example.bloodbank.fireStoreDataBase.posts.BaseActivityA;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created By Mohamed El Banna On 6/27/2020
 **/
public class AddDonationActivity extends BaseActivityA implements View.OnClickListener {
    TextView tvBankName;
    TextInputEditText etDonnerName;
    TextInputEditText etDonnerAddress;
    protected Spinner spinnerCity;
    protected Spinner spinnerArea;
    protected Spinner spinnerBloodType;
    protected Spinner spinnerBloodQuantity;
    Button btnDonation;
    ArrayAdapter adapterCity;
    ArrayAdapter adapterArea;
    ArrayAdapter<CharSequence> adapterBloodTypes;
    ArrayAdapter adapterQuantity;
    List<Long> numbersList = new ArrayList<>();

    private int selectedArea = -1;
    private int selectedCity = 1;
    private int selectedBloodType = -1;
    private int selectedBloodQuantity = -1;

    DonnerResponse mDonnerResponse = new DonnerResponse();
    BloodBankModel mBloodBankModel = new BloodBankModel();
    UserResponse mUserResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donnation);
        setUp();
    }

    private void setUp() {
        initView();
        prepareIntent(getIntent());
        gerCurrentUser();
    }

    private void gerCurrentUser() {
        UsersCollectionDao.getUserById(FirebaseAuth.getInstance().getUid(), result -> {
            mUserResponse = (UserResponse) result;
        });
    }


    private void prepareIntent(Intent intent) {
        if (intent == null)
            return;
        if (intent.hasExtra("donation")) {
            mDonnerResponse = (DonnerResponse) intent.getSerializableExtra("donation");
            etDonnerName.setText(mDonnerResponse.getName());
            spinnerBloodType.setSelection(adapterArea.getPosition(mDonnerResponse.getBloodType()));
            btnDonation.setText("Edit Donation");
        }
        if (intent.hasExtra("bank")) {
            mBloodBankModel = (BloodBankModel) intent.getSerializableExtra("bank");
            tvBankName.setText(mBloodBankModel.getName());
        }
    }

    private void initView() {
        tvBankName = findViewById(R.id.tv_bank_name);
        etDonnerName = findViewById(R.id.et_donner_name);
        etDonnerAddress = findViewById(R.id.et_donner_address);
        spinnerCity = findViewById(R.id.spinner_city);
        spinnerArea = findViewById(R.id.spinner_area);
        spinnerBloodType = findViewById(R.id.spinner_blood_type);
        spinnerBloodQuantity = findViewById(R.id.spinner_type_quantity);
        btnDonation = findViewById(R.id.btn_donation);
        btnDonation.setOnClickListener(this);

        adapterBloodTypes = ArrayAdapter.createFromResource(this, R.array.bloodTypes_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodType.setAdapter(adapterBloodTypes);
        spinnerBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBloodType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        for (long i = 1; i <= 3; i++) {
            numbersList.add(i);
        }

        adapterQuantity = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, numbersList);
        spinnerBloodQuantity.setAdapter(adapterQuantity);
        spinnerBloodQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBloodQuantity = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        DonnerResponse mResponse = getDonnerData();
        mResponse.setId(mUserResponse.getId());
        if (!mUserResponse.getDonation().equalsIgnoreCase("1"))
            BankCollectionDao.updateBloodTypeQuantity(mBloodBankModel.getId(), getBloodTypeValue(mResponse.getBloodType(), mBloodBankModel) + 1, getBloodTypeKey(mResponse.getBloodType()), res -> {
                if ((boolean) res) {
                    UsersCollectionDao.updateIsDonorUser(mUserResponse.getId(), "1", result2 -> {
                        if ((boolean) result2)
                            showDialog();
                        DonnerDao.addDonner(new DonnerResponse(mUserResponse, mBloodBankModel.getId(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en")).format(new Date())), task -> {
                            saveHistory();
                            finish();
                        }, e -> {
                            Toast.makeText(AddDonationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
                    });

                }
            });
        else btnDonation.setVisibility(View.GONE);


//        DonnerDao.addDonner(mResponse, task -> {
//            if (task.isSuccessful()) {
//                showMessage("Thanks For Your Donation And You Have To Wait 3 Months Until Next Donation", "ok", (dialog, which) -> onBackPressed(), false);
//
//                createNotification();
//                UsersCollectionDao.updateIsDonorUser(mUserResponse.getId(), "1", result -> {
//
//                });
//            } else
//                showMessage("failed", "ok", (dialog, which) -> onBackPressed(), false);
//
//        }, e -> {
//            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//        });
        List<String> mStringList = new ArrayList<>();
        mStringList.add(mUserResponse.getId());
        BankCollectionDao.updateBankDonors(mBloodBankModel.getId(), mStringList, result -> {
        });

    }

    String getBloodTypeKey(String type) {
        if (type.equalsIgnoreCase("A+"))
            return "numberOf_A";
        if (type.equalsIgnoreCase("A-"))
            return "numberOf_A2";
        if (type.equalsIgnoreCase("B+"))
            return "numberOf_B";
        if (type.equalsIgnoreCase("B-"))
            return "numberOf_B2";
        if (type.equalsIgnoreCase("O+"))
            return "numberOf_O";
        if (type.equalsIgnoreCase("O-"))
            return "numberOf_O2";
        if (type.equalsIgnoreCase("AB+"))
            return "numberOf_AB";
        if (type.equalsIgnoreCase("AB-"))
            return "numberOf_AB2";
        return null;
    }

    long getBloodTypeValue(String type, BloodBankModel mBloodBankModel) {
        if (type.equalsIgnoreCase("A+"))
            return mBloodBankModel.getNumberOf_A();
        if (type.equalsIgnoreCase("A-"))
            return mBloodBankModel.getNumberOf_A2();
        if (type.equalsIgnoreCase("B+"))
            return mBloodBankModel.getNumberOf_B();
        if (type.equalsIgnoreCase("B-"))
            return mBloodBankModel.getNumberOf_B2();
        if (type.equalsIgnoreCase("O+"))
            return mBloodBankModel.getNumberOf_O();
        if (type.equalsIgnoreCase("O-"))
            return mBloodBankModel.getNumberOf_O2();
        if (type.equalsIgnoreCase("AB+"))
            return mBloodBankModel.getNumberOf_AB();
        if (type.equalsIgnoreCase("AB-"))
            return mBloodBankModel.getNumberOf_AB2();
        return 0;
    }

    void saveHistory() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("action", "You have Donated to " + mUserResponse.getName() + " With Blood Type" + mUserResponse.getBloodType());
        mMap.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en")).format(new Date()));
        mapList.add(mMap);
        HistoryResponse mHistoryResponse = new HistoryResponse();
        mHistoryResponse.setHistory(mapList);
        mHistoryResponse.setId(mUserResponse.getId());
        HistoryDao.addHistory(mHistoryResponse, result1 -> {
        });
        UsersCollectionDao.setUserHistory(mUserResponse.getId(), mHistoryResponse, result -> {
        });
    }

    void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Alert!")
                .setMessage("You are donor Now You can donate again after 3 months ")
                .setCancelable(false)
                .setNegativeButton("Ok", (dialogInterface, i) -> {
                }).show();
    }

//    public void createNotification() {
//
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(getApplicationContext(), "0")
//                        .setSmallIcon(R.mipmap.app_icon)
//                        .setContentTitle(getString(R.string.app_name))
//                        .setContentText("Thanks For Your Donation And You Have To Wait 3 Months Until Next Donation")
//                        .setAutoCancel(true);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(0, notificationBuilder.build());
//    }

    private DonnerResponse getDonnerData() {
        DonnerResponse mDonnerResponse = new DonnerResponse();
        mDonnerResponse.setName(etDonnerName.getText().toString().trim());
        mDonnerResponse.setAddress(etDonnerAddress.getText().toString().trim());
        mDonnerResponse.setBloodType((String) adapterBloodTypes.getItem(selectedBloodType));
        mDonnerResponse.setCreatedAt(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en")).format(new Date()));
        if (getIntent().hasExtra("bank")) mDonnerResponse.setBloodBankId(mBloodBankModel.getId());

        if (getIntent().hasExtra("donation")) mDonnerResponse.setId(this.mDonnerResponse.getId());

        return mDonnerResponse;
    }
}
