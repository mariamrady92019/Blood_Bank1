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
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.bloodbank.R;
import com.example.bloodbank.fireStoreDataBase.posts.BaseActivityA;
import com.example.bloodbank.firebaseStroge.StrogeBuilder;
import com.example.bloodbank.hospitals.HospitalCollectionDao;
import com.example.bloodbank.hospitals.HospitalModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class AddNewHospitalActivity extends BaseActivityA implements View.OnClickListener {

    protected CircularImageView hospitalImage;
    protected ImageView editImage;
    protected TextInputEditText hospitalName;
    protected TextInputEditText hospitalAdress;
    protected Spinner spinnerCity;
    protected Spinner spinnerArea;
    ArrayAdapter adapterCity;
    ArrayAdapter adapterArea;
    private int selectedArea = -1;
    private int selectedCity = 1;
    protected Button addNewBank;
    int GALLERY_REQUEST = 1;
    private Uri mImageUri1 = null;
    String imageUri;

    HospitalModel mHospitalModel;

    OnCompleteListener onCompleteListener = task -> {
        if (task.isSuccessful()) {
            if (getIntent().hasExtra("hospital")) {
                showMessage("hospital updated successfully", "ok", (dialog, which) -> onBackPressed(), false);
            } else
                showMessage("hospital added successfully", "ok", (dialog, which) -> onBackPressed(), false);
        } else
            Toast.makeText(AddNewHospitalActivity.this, "failed to add hospital", Toast.LENGTH_LONG).show();
        notifyChanges();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_add_new_hospital);
        initView();
        prepareIntent(getIntent());
    }

    private void prepareIntent(Intent intent) {
        if (intent == null)
            return;
        if (intent.hasExtra("hospital")) {
            mHospitalModel = (HospitalModel) intent.getSerializableExtra("hospital");
            if (mHospitalModel.getImageUri() != null)
                Glide.with(this).load(mHospitalModel.getImageUri()).into(hospitalImage);
            imageUri = mHospitalModel.getImageUri();
            hospitalName.setText(mHospitalModel.getHospName());
            hospitalAdress.setText(mHospitalModel.getHospAdress());
            spinnerArea.setSelection(adapterArea.getPosition(mHospitalModel.getArea()));
            spinnerCity.setSelection(adapterArea.getPosition(mHospitalModel.getCity()));
            addNewBank.setText("Edit Hospital");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.editImage) {
            openPickerImages();
        } else if (view.getId() == R.id.addNewHospital) {
            HospitalModel hospital = getHospitalData();
            if (getIntent().hasExtra("hospital"))
                HospitalCollectionDao.updateHospitalById(hospital.getId(), hospital, onCompleteListener);
            else
                HospitalCollectionDao.addHospitalToDataBse(hospital, onCompleteListener);
        }
    }

    private void initView() {
        hospitalImage = findViewById(R.id.hospitalImage);
        editImage = findViewById(R.id.editImage);
        editImage.setOnClickListener(AddNewHospitalActivity.this);
        hospitalName = findViewById(R.id.hospitalName);
        hospitalAdress = findViewById(R.id.hospitalAdress);
        addNewBank = findViewById(R.id.addNewHospital);
        addNewBank.setOnClickListener(this);
        spinnerCity = findViewById(R.id.spinner_city);
        spinnerArea = findViewById(R.id.spinner_area);


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
            hospitalImage.setImageURI(mImageUri1);


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


    public void notifyChanges() {
        ArrayList<HospitalModel> last = HospitalCollectionDao.getListOfHospitals();
        HospitalsFragment.adapter.setNewList(last);
        HospitalsFragment.adapter.notifyDataSetChanged();

    }

    public HospitalModel getHospitalData() {
        HospitalModel model = new HospitalModel();
        model.setHospName(hospitalName.getText().toString());
        model.setHospAdress(hospitalAdress.getText().toString());
        model.setCity((String) adapterCity.getItem(selectedCity));
        model.setArea((String) adapterArea.getItem(selectedArea));
        model.setImageUri(imageUri);
        if (getIntent().hasExtra("hospital"))
            model.setId(mHospitalModel.getId());
        return model;
    }

}
