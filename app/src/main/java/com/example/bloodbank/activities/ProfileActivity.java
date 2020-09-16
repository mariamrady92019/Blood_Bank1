package com.example.bloodbank.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbank.Base.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.fireStoreDataBase.FireStoreBuilder;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import static com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao.USERS_REF;

/**
 * Created By Mohamed El Banna On 6/26/2020
 **/
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnUpdate;



    TextInputEditText etName;
    TextInputEditText etAddress;
    TextInputEditText etEmail;
    TextInputEditText etPhoneNumber;
    TextView tv_user_type;
    TextView tvIsDonor;

    private Spinner spinnerBloodType;

    private int selectedBloodType = -1;
    private ArrayAdapter adapterBloodType;

    protected Helper helper = new Helper();
    FirebaseAuth mAuth;
    UserResponse mResponse = new UserResponse();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        initView();
        setData();
    }

    private void initView() {
        etName = findViewById(R.id.et_user_name);
        etAddress = findViewById(R.id.et_address);
        etEmail = findViewById(R.id.et_email);
        tv_user_type = findViewById(R.id.tv_user_type);
        etEmail.setEnabled(false);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        btnUpdate = findViewById(R.id.btn_update);
        tvIsDonor = findViewById(R.id.tv_is_donor);
        btnUpdate.setOnClickListener(this);

        spinnerBloodType = findViewById(R.id.spinner_blood_type);

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
    }

    private void setData() {
        FireStoreBuilder.getFireStoreInstance()
                .collection(USERS_REF)
                .document(mAuth.getUid())
                .get()
                .addOnSuccessListener(task -> {
                    mResponse = task.toObject(UserResponse.class);
                    if (mResponse != null) {
                        if (mResponse.getName() != null)
                            etName.setText(mResponse.getName());
                        if (mResponse.getBloodType() != null)
                            spinnerBloodType.setSelection(adapterBloodType.getPosition(mResponse.getBloodType()));
                        if (mResponse.getAddress() != null)
                            etAddress.setText(mResponse.getAddress());
                        if (mResponse.getMail() != null)
                            etEmail.setText(mResponse.getMail());
                        if (mResponse.getPhoneNumber() != null)
                            etPhoneNumber.setText(mResponse.getPhoneNumber());
                        if (mResponse.getUserType() != null)
                            tv_user_type.setText(mResponse.getUserType());
                        if (mResponse.getDonation() != null && mResponse.getDonation().equalsIgnoreCase("1"))
                            tvIsDonor.setText("Yes");
                        else tvIsDonor.setText("No");
                    }
                });
    }

    @Override
    public void onClick(View view) {
        UserResponse mResponse = getProfileData();
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("name", mResponse.getName());
        stringObjectMap.put("bloodType", mResponse.getBloodType());
        stringObjectMap.put("address", mResponse.getAddress());
        stringObjectMap.put("phoneNumber", mResponse.getPhoneNumber());
        UsersCollectionDao.updateUser(mAuth.getUid(), stringObjectMap, task -> {
            Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
        });

    }

    private UserResponse getProfileData() {
        UserResponse response = new UserResponse();
        if (etName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        } else response.setName(etName.getText().toString().trim());

        if (etAddress.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
        } else response.setAddress(etAddress.getText().toString().trim());

        if (etPhoneNumber.getText().toString().trim().isEmpty() && !helper.isValidPhoneNumber(etPhoneNumber.getText().toString().trim())) {
            Toast.makeText(this, "Phone number Invalid or Empty", Toast.LENGTH_SHORT).show();
        } else response.setPhoneNumber(etPhoneNumber.getText().toString().trim());

        response.setBloodType(String.valueOf(adapterBloodType.getItem(selectedBloodType)).trim());

        return response;
    }
}
