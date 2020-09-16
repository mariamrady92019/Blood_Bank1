package com.example.bloodbank.fireStoreDataBase.posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.PostsFragment;
import com.example.bloodbank.constant.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class EditePost extends AppCompatActivity implements View.OnClickListener {

    protected EditText postHeaderName;
    protected EditText postHeaderDescription;
    protected EditText postHeaderAdress;
    protected Spinner bloodTypesSpinner;
    protected Button UpdatePost;
    String postName, bloodType, adress, timeNedded;
    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_edite_post);
        TextView tvBack=findViewById(R.id.tv_back);
        tvBack.setOnClickListener(view -> onBackPressed());
        initView();
        initSpinner();
        recievePostDat(getIntent());
        justStarted();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.UpdatePost) {
            PostsModel postsModel = getPostUpdated();

            PostsCollectionDao.updatePostById(postId, postsModel, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditePost.this, "updatedSuccesfully",
                            Toast.LENGTH_LONG).show();
                    notifyChanges();
                    onBackPressed();
                } else
                    Toast.makeText(EditePost.this, "failed to update",
                            Toast.LENGTH_LONG).show();
            });


        }
    }

    public void notifyChanges() {
        PostsCollectionDao postsCollectionDao = new PostsCollectionDao();
        ArrayList<PostsModel> last = postsCollectionDao.getListOfPosts();
        PostsFragment.adapter.changeAdapterList(last);
        PostsFragment.adapter.notifyDataSetChanged();
    }

    private void initView() {
        postHeaderName = (EditText) findViewById(R.id.postHeaderName);
        postHeaderDescription = (EditText) findViewById(R.id.postHeaderDescription);
        postHeaderAdress = (EditText) findViewById(R.id.postHeaderAdress);
        bloodTypesSpinner = (Spinner) findViewById(R.id.bloodTypes_spinner);
        UpdatePost = (Button) findViewById(R.id.UpdatePost);
        UpdatePost.setOnClickListener(EditePost.this);
    }

    public void initSpinner() {


        bloodTypesSpinner = (Spinner) findViewById(R.id.bloodTypes_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bloodTypes_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        bloodTypesSpinner.setAdapter(adapter);
    }


    public PostsModel getPostUpdated() {
        PostsModel post = new PostsModel();
        post.setName(postHeaderName.getText().toString());
        post.setAddress(postHeaderAdress.getText().toString());
        post.setBloodType(bloodTypesSpinner.getSelectedItem().toString());
         post.setId(postId);
        post.setImageloadedName(Constants.currentUser.getName());


        return post;

    }

    public void recievePostDat(Intent intent) {
        if (intent == null)
            return;
        if (intent.hasExtra("postId")) {
            postId = intent.getStringExtra("postId");
            PostsModel postsModel = (PostsModel) intent.getSerializableExtra("this post");
            if (postsModel.getName() != null)
                postName = postsModel.getName();
            if (postsModel.getBloodType() != null)
                bloodType = postsModel.getBloodType();
            if (postsModel.getAddress() != null)
                adress = postsModel.getAddress();

        }
    }

    void justStarted() {
        postHeaderName.setText(postName);
        postHeaderAdress.setText(adress);
        postHeaderDescription.setText(timeNedded);


        if (bloodType.toLowerCase().equals("o")) {
            bloodTypesSpinner.setSelection(0);
        }
        if (bloodType.toLowerCase().equals("a")) {
            bloodTypesSpinner.setSelection(1);
        }
        if (bloodType.toLowerCase().equals("b")) {
            bloodTypesSpinner.setSelection(2);
        }
        if (bloodType.toLowerCase().equals("ab")) {
            bloodTypesSpinner.setSelection(3);
        }


    }

}
