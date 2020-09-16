package com.example.bloodbank.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.search.SearchActivity;
import com.example.bloodbank.events.SearchEvent;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.fireStoreDataBase.posts.NewPost;
import com.example.bloodbank.fireStoreDataBase.posts.PostsCollectionDao;
import com.example.bloodbank.fireStoreDataBase.posts.PostsModel;
import com.example.bloodbank.postsRecycler.PostsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {


    protected View rootView;
    protected RecyclerView postsRecycler;
    protected ProgressBar simpleProgressBar;
    protected FloatingActionButton addNewPost;
    public static PostsAdapter adapter;
    protected ImageView searchBy;
    protected LinearLayout searchlayout;
    protected Button sortByName;
    protected Button sortBylocation;
    protected Button sortbloodtype;
    protected LinearLayout sortingButtonsLayout;
    public static ArrayList<PostsModel> listOfPosts;
    RecyclerView.LayoutManager linLayoutManager;
    View view;
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback;
    EditText searchEditText;
    int searchRequest = 0;

    ImageView ivFilter;
    SearchView simpleSearchView;
    TextView tvClearSearch;


    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        listOfPosts = NewPost.postsCollectionDao.getListOfPosts();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listOfPosts == null) {
            Toast.makeText(getActivity(), "list empty", Toast.LENGTH_SHORT);

        } else {
            buildRecycler();
            adapter.changeAdapterList(listOfPosts);
            adapter.notifyDataSetChanged();
            simpleProgressBar.setVisibility(View.GONE);

        }
//        searchEditText();
        Constants.setUpAddPOstBtn(addNewPost);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    BloodBankModel bank = new BloodBankModel();

    PostsAdapter.MyAdapterListener listener = new PostsAdapter.MyAdapterListener() {
        @Override
        public void donnateButtonListener(View v, int position) {
            PostsModel post = listOfPosts.get(position);
            String bloodType = post.getBloodType();
            String userBloodType = Constants.currentUser.getBloodType();


            if (bloodType.toLowerCase().equals("o")) {
                bank.setNumberOf_O(bank.getNumberOf_O() + 1);
                showMessage("donnated" + bloodType + bank.getNumberOf_O(), "ok", getContext());

            }
            if (bloodType.toLowerCase().equals("AB".toLowerCase())) {
                bank.setNumberOf_AB(bank.getNumberOf_AB() + 1);
                 showMessage("donnated" + bloodType + bank.getNumberOf_AB(), "ok", getContext());

            }
            if (bloodType.toLowerCase().equals("A".toLowerCase())) {
                bank.setNumberOf_A(bank.getNumberOf_A() + 1);
                showMessage("donnated" + bloodType
                        + bank.getNumberOf_A(), "ok", getContext());

            }
            if (bloodType.toLowerCase().equals("B".toLowerCase())) {
                bank.setNumberOf_B(bank.getNumberOf_B() + 1);
                showMessage("donnated" + bloodType + bank.getNumberOf_B(), "ok", getContext());

            }


        }
    };


    public void buildRecycler() {

        postsRecycler = (RecyclerView) view.findViewById(R.id.postsRecycler);
        linLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new PostsAdapter(listOfPosts, getContext(), listener);


        postsRecycler.setLayoutManager(linLayoutManager);
        postsRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        postsRecycler.setHasFixedSize(true);

    }

    private void initView(View rootView) {
        simpleProgressBar = (ProgressBar) rootView.findViewById(R.id.simpleProgressBar);
        addNewPost = (FloatingActionButton) rootView.findViewById(R.id.addNewPost);
        addNewPost.setOnClickListener(PostsFragment.this);
        searchlayout = (LinearLayout) rootView.findViewById(R.id.searchlayout);
        postsRecycler = (RecyclerView) rootView.findViewById(R.id.postsRecycler);
        sortByName = (Button) rootView.findViewById(R.id.sortByName);
        sortByName.setOnClickListener(PostsFragment.this);
        sortBylocation = (Button) rootView.findViewById(R.id.sortBylocation);
        sortBylocation.setOnClickListener(PostsFragment.this);
        sortbloodtype = (Button) rootView.findViewById(R.id.sortbloodtype);
        sortbloodtype.setOnClickListener(PostsFragment.this);
        sortingButtonsLayout = (LinearLayout) rootView.findViewById(R.id.sortingButtons_layout);
        ivFilter = (ImageView) rootView.findViewById(R.id.iv_filter);
        simpleSearchView = (SearchView) rootView.findViewById(R.id.search_bar); // inititate a search view
        simpleSearchView.setOnQueryTextListener(this);
        tvClearSearch = rootView.findViewById(R.id.tv_clear);
        tvClearSearch.setPaintFlags(tvClearSearch.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvClearSearch.setOnClickListener(v -> {
            adapter.clearFilter();
            simpleSearchView.clearFocus();
        });

        ivFilter.setOnClickListener(view -> startActivity(new Intent(getContext(), SearchActivity.class)));

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addNewPost) {

            Intent intent = new Intent(getActivity(), NewPost.class);
            startActivity(intent);

        } else if (view.getId() == R.id.sortByName) {
            ArrayList<PostsModel> list = PostsCollectionDao.getPostsOrderdBYNamme();
            adapter.changeAdapterList(list);
            adapter.notifyDataSetChanged();


        } else if (view.getId() == R.id.sortBylocation) {
            ArrayList<PostsModel> list = PostsCollectionDao.getPostsOrderdByAdress();
            adapter.changeAdapterList(list);
            adapter.notifyDataSetChanged();

        } else if (view.getId() == R.id.sortbloodtype) {
            ArrayList<PostsModel> list = PostsCollectionDao.getPostsOrderdByBloodType();
            adapter.changeAdapterList(list);
            adapter.notifyDataSetChanged();

        }

    }

    public AlertDialog showMessage(String message, String posActionName, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(posActionName, (dialog, which) -> dialog.dismiss());
        return builder.show();
    }


    @Subscribe
    public void onSearchEvent(SearchEvent event) {
        if (event != null) {
            adapter.filter(event.getCity(), event.getBloodType());
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query, query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}









