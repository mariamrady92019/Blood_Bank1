package com.example.bloodbank.fireStoreDataBase.posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.PostsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class NewPost extends BaseActivityA implements View.OnClickListener {

    protected TextInputEditText postHeaderName;
    protected EditText postHeaderDescription;
    protected TextInputEditText postHeaderAdress;
    protected Spinner bloodTypesSpinner;
    protected Spinner spinnerCity;
    protected Spinner spinnerArea;
    ArrayAdapter adapterCity;
    ArrayAdapter adapterArea;
    private int selectedArea = -1;
    private int selectedCity = 1;
    ArrayAdapter<CharSequence> adapter;
    protected Button savePost;
    public static PostsCollectionDao postsCollectionDao = new PostsCollectionDao();
    PostsModel mPostsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_new_post);
        TextView tvBack=findViewById(R.id.tv_back);
        tvBack.setOnClickListener(view -> onBackPressed());
        initView();
        initSpinner();
        prepareIntent(getIntent());
    }

    private void prepareIntent(Intent intent) {
        if (intent == null)
            return;
        if (intent.hasExtra("post")) {
            mPostsModel = (PostsModel) intent.getSerializableExtra("post");
            postHeaderName.setText(mPostsModel.getName());
            postHeaderAdress.setText(mPostsModel.getAddress());
            bloodTypesSpinner.setSelection(adapter.getPosition(mPostsModel.getBloodType()));
            spinnerArea.setSelection(adapterArea.getPosition(mPostsModel.getArea()));
            spinnerCity.setSelection(adapterCity.getPosition(mPostsModel.getCity()));
            savePost.setText("Edit Post");
        }
    }

    private void initView() {
        postHeaderName = findViewById(R.id.postHeaderName);
        postHeaderDescription = findViewById(R.id.postHeaderDescription);
        postHeaderAdress = findViewById(R.id.postHeaderAdress);
        bloodTypesSpinner = findViewById(R.id.bloodTypes_spinner);
        spinnerCity = findViewById(R.id.spinner_city);
        spinnerArea = findViewById(R.id.spinner_area);
        savePost = findViewById(R.id.savePost);
        savePost.setOnClickListener(NewPost.this);

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


    public void initSpinner() {


        bloodTypesSpinner = findViewById(R.id.bloodTypes_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.bloodTypes_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        bloodTypesSpinner.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.savePost) {
            PostsModel postsModel = getPostCreated();
            showProgressDialog("please wait", this);
            if (getIntent().hasExtra("post"))
                PostsCollectionDao.updatePostById(postsModel.getId(),postsModel,onCompleteListener);
            else
                PostsCollectionDao.addPostToDataBse(postsModel, onCompleteListener);

        }
    }


    private void addPostToDataBase() {
        PostsModel post = getPostCreated();



    }

    OnCompleteListener onCompleteListener = task -> {
        if (task.isSuccessful()) {
            hideProgressDialog();
            notifyChanges();
            //no needed for it as each post have an id
            // Constants.postId = postsCollectionDao.getPostsRefference().document().getId();

            if (getIntent().hasExtra("post")) {
                showMessage("post updated successfully", "ok", (dialog, which) -> onBackPressed(), false);
            } else
                showMessage("post added successfully", "ok", (dialog, which) -> onBackPressed(), false);
        } else {

            showMessage("failed to add ", "ok", NewPost.this);

        }
    };

    public PostsModel getPostCreated() {
        String name = postHeaderName.getText().toString();
        String adress = postHeaderAdress.getText().toString();
        String bloodType = bloodTypesSpinner.getSelectedItem().toString();
        String city = (String) adapterCity.getItem(selectedCity);
        String area = (String) adapterArea.getItem(selectedArea);
        PostsModel post = new PostsModel(name, bloodType, adress, city, area);
        if (getIntent().hasExtra("post"))
            post.setId(mPostsModel.getId());
        return post;
    }

    public void notifyChanges() {
        ArrayList<PostsModel> last = postsCollectionDao.getListOfPosts();
        PostsFragment.adapter.setNewList(last);
        PostsFragment.adapter.notifyDataSetChanged();

    }
}
