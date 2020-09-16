package com.example.bloodbank.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.BloodBankHome.BloodBankHome;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.fireStoreDataBase.posts.BaseActivityA;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.example.bloodbank.firebaseStroge.StrogeBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewBankActivity extends BaseActivityA implements View.OnClickListener {

    public static final String INTENT_BANK_ID = "INTENT_BANK_ID";

    protected ImageView bankImage;
    protected TextInputEditText bankName;
    protected TextInputEditText bankAddress;
    protected Spinner spTypeA1Quantity;
    protected Spinner spinnerCity;
    protected Spinner spinnerArea;
    protected Spinner spTypeA2Quantity;
    protected Spinner spTypeB1Quantity;
    protected Spinner spTypeB2Quantity;
    protected Spinner spTypeO1Quantity;
    protected Spinner spTypeO2Quantity;
    protected Spinner spTypeAB1Quantity;
    protected Spinner spTypeAB2Quantity;
    ArrayAdapter adapterCity;
    ArrayAdapter adapterArea;

    ArrayAdapter arrQuantity;
    List<Long> numbersList = new ArrayList<>();
    protected Button addNewBank;
    private static final int GALLERY_REQUEST = 10;
    private Uri mImageUri1 = null;
    String imageUri;
    BloodBankModel mBloodBankModel = new BloodBankModel();

    String registeredBankId;

    private int selectedTypeA1 = -1;
    private int selectedTypeA2 = -1;
    private int selectedTypeB1 = -1;
    private int selectedTypeB2 = -1;
    private int selectedTypeO1 = -1;
    private int selectedTypeO2 = -1;
    private int selectedTypeAB1 = -1;
    private int selectedTypeAB2 = -1;
    private int selectedArea = -1;
    private int selectedCity = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_add_new_bank);
        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(view -> onBackPressed());
        initView();
        prepareIntent(getIntent());

    }

    private void prepareIntent(Intent intent) {
        if (intent == null)
            return;
        if (intent.hasExtra("bank")) {
            mBloodBankModel = (BloodBankModel) intent.getSerializableExtra("bank");
            bankName.setText(mBloodBankModel.getName());
            bankAddress.setText(mBloodBankModel.getAddress());
            spTypeA1Quantity.setSelection(arrQuantity.getPosition(mBloodBankModel.getNumberOf_A()));
            spTypeA2Quantity.setSelection(arrQuantity.getPosition(mBloodBankModel.getNumberOf_A2()));
            spTypeB1Quantity.setSelection(arrQuantity.getPosition(mBloodBankModel.getNumberOf_B()));
            spTypeB2Quantity.setSelection(arrQuantity.getPosition(mBloodBankModel.getNumberOf_B2()));
            spTypeO1Quantity.setSelection(arrQuantity.getPosition(mBloodBankModel.getNumberOf_O()));
            spTypeO2Quantity.setSelection(arrQuantity.getPosition(mBloodBankModel.getNumberOf_O2()));
            spTypeAB1Quantity.setSelection(arrQuantity.getPosition(mBloodBankModel.getNumberOf_AB()));
            spTypeAB2Quantity.setSelection(arrQuantity.getPosition(mBloodBankModel.getNumberOf_AB2()));
            spinnerArea.setSelection(adapterArea.getPosition(mBloodBankModel.getArea()));
            spinnerCity.setSelection(adapterArea.getPosition(mBloodBankModel.getCity()));
            addNewBank.setText("Edit Bank");
        }
        if (intent.hasExtra(INTENT_BANK_ID)) {
            registeredBankId = intent.getStringExtra(INTENT_BANK_ID);
            bankAddress.setText(intent.getStringExtra("address"));
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addNewBank) {
            BloodBankModel bank = getBankData();
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("exist",true);
            UsersCollectionDao.updateUser(FirebaseAuth.getInstance().getUid(), stringObjectMap, task -> { });
            if (getIntent().hasExtra("bank")) {
                BankCollectionDao.updateBankById(bank.getId(), bank, onCompleteListener);
            } else {
                BankCollectionDao.addBankToDataBse(bank, onCompleteListener);
            }
        }
    }


    public void openPickerImages() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri1 = data.getData();
            bankImage.setImageURI(mImageUri1);
            uploadImage1();
        }

    }

    private void uploadImage1() {
        UploadTask uploadTask;
        final StorageReference ref = StrogeBuilder.getStorageRef().child(
                mImageUri1.getLastPathSegment());

        uploadTask = ref.putFile(mImageUri1);

        Task<Uri> urlTask = uploadTask.continueWithTask(
                task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }).addOnCompleteListener(task -> {
            Uri downloadUri = task.getResult();

            imageUri = downloadUri.toString();
        });
    }


    OnCompleteListener onCompleteListener = task -> {
        if (task.isSuccessful()) {
            if (getIntent().hasExtra("bank")) {
                showMessage("bank updated successfully", "ok", (dialog, which) -> onBackPressed(), false);
            } else if (getIntent().hasExtra(INTENT_BANK_ID)) {
                startActivity(new Intent(this, BloodBankHome.class));
            } else
                showMessage("bank added successfully", "ok", (dialog, which) -> onBackPressed(), false);
        } else {
            Toast.makeText(AddNewBankActivity.this, "failed to add bank"
                    , Toast.LENGTH_LONG).show();
        }
        notifyChanges();
    };

    private void initView() {
        bankImage = findViewById(R.id.bankImage);
        bankName = findViewById(R.id.et_bankName);
        bankAddress = findViewById(R.id.et_bank_address);
        addNewBank = findViewById(R.id.addNewBank);
        spTypeA1Quantity = findViewById(R.id.spinner_type_a1);
        spTypeA2Quantity = findViewById(R.id.spinner_type_a2);
        spTypeB1Quantity = findViewById(R.id.spinner_type_b1);
        spTypeB2Quantity = findViewById(R.id.spinner_type_b2);
        spTypeO1Quantity = findViewById(R.id.spinner_type_o1);
        spTypeO2Quantity = findViewById(R.id.spinner_type_o2);
        spTypeAB1Quantity = findViewById(R.id.spinner_type_ab1);
        spTypeAB2Quantity = findViewById(R.id.spinner_type_ab2);
        spinnerCity = findViewById(R.id.spinner_city);
        spinnerArea = findViewById(R.id.spinner_area);

        addNewBank.setOnClickListener(AddNewBankActivity.this);

        for (long i = 1; i <= 25; i++) {
            numbersList.add(i);
        }
        arrQuantity = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, numbersList);

        spTypeA1Quantity.setAdapter(arrQuantity);
        spTypeA1Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeA1 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTypeA2Quantity.setAdapter(arrQuantity);
        spTypeA2Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeA2 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTypeB1Quantity.setAdapter(arrQuantity);
        spTypeB1Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeB1 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTypeB2Quantity.setAdapter(arrQuantity);
        spTypeB2Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeB2 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTypeO1Quantity.setAdapter(arrQuantity);
        spTypeO1Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeO1 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTypeO2Quantity.setAdapter(arrQuantity);
        spTypeO2Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeO2 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTypeAB1Quantity.setAdapter(arrQuantity);
        spTypeAB1Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeAB1 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTypeAB2Quantity.setAdapter(arrQuantity);
        spTypeAB2Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTypeAB2 = i;
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
    }


    public BloodBankModel getBankData() {
        BloodBankModel mBank = new BloodBankModel();
        mBank.setName(bankName.getText().toString());
        mBank.setAddress(bankAddress.getText().toString());
        mBank.setNumberOf_A((Long) arrQuantity.getItem(selectedTypeA1));
        mBank.setNumberOf_A2((Long) arrQuantity.getItem(selectedTypeA2));
        mBank.setNumberOf_B((Long) arrQuantity.getItem(selectedTypeB1));
        mBank.setNumberOf_B2((Long) arrQuantity.getItem(selectedTypeB2));
        mBank.setNumberOf_O((Long) arrQuantity.getItem(selectedTypeO1));
        mBank.setNumberOf_O2((Long) arrQuantity.getItem(selectedTypeO2));
        mBank.setNumberOf_AB((Long) arrQuantity.getItem(selectedTypeAB1));
        mBank.setNumberOf_AB2((Long) arrQuantity.getItem(selectedTypeAB2));
        mBank.setCity((String) adapterCity.getItem(selectedCity));
        mBank.setArea((String) adapterArea.getItem(selectedArea));
        if (getIntent().hasExtra("bank"))
            mBank.setId(mBloodBankModel.getId());
        if (getIntent().hasExtra(INTENT_BANK_ID))
            mBank.setId(registeredBankId);
        return mBank;
    }

    public void notifyChanges() {
        ArrayList<BloodBankModel> last = BankCollectionDao.getListOfBanks();
        BanksFragment.adapter.changeAdapterList(last);
        BanksFragment.adapter.notifyDataSetChanged();

    }
}
