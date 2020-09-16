package com.example.bloodbank.postsRecycler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.fireStoreDataBase.posts.NewPost;
import com.example.bloodbank.fireStoreDataBase.posts.PostsCollectionDao;
import com.example.bloodbank.fireStoreDataBase.posts.PostsModel;
import com.example.bloodbank.firebaseStroge.StrogeBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    ArrayList<PostsModel> base = new ArrayList();
    ArrayList<PostsModel> posts = new ArrayList();
    private ArrayList<PostsModel> postsListFiltered;
    Context context;
    static onoPtionSelected onoPtionSelected;
    PostsCollectionDao postsCollectionDao = new PostsCollectionDao();


    public MyAdapterListener onClickListener;
    public OptionMenueListener menueListener;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();


    public PostsAdapter(ArrayList posts, Context context, MyAdapterListener listener) {
        this.posts = posts;
        this.base = posts;
        this.context = context;
        this.onClickListener = listener;
    }


    public void changeAdapterList(ArrayList list) {
        this.posts = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new ViewHolder(view);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Constants.isAdmin)
            holder.donnate.setVisibility(View.GONE);
        PostsModel post = posts.get(position);
        holder.userName.setText(post.getName());
        holder.adress.setText(post.getAddress());
        holder.bloodType.setText(post.getBloodType());
        holder.tvCity.setText(post.getCity());
        holder.tvArea.setText(post.getArea());

        StorageReference imageRef = StrogeBuilder.getStorageRef().
                child("mariam" + ".jpg");

        imageRef.getBytes(1024 * 1024).
                addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.userImage.setImageBitmap(bitmap);
                    }
                });

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)

        // String s = HomeActivity.downloadUri.toString();
        // Glide.with(context)
        // .load()
        // .into(holder.userImage);

        if (Constants.isAdmin) {

            holder.optionMenue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, holder.optionMenue);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.optionmenue);
                    //adding click listener
                    popup.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.deletepost:
                                //handle menu1 click

                                String action = "Delete";
                                new AlertDialog.Builder(context)
                                        .setTitle(action)
                                        .setMessage("Are You Sure You Want To " + action + " ?!")
                                        .setNegativeButton("Ok", (dialogInterface, i) -> {
                                            removeItem(position);

                                            postsCollectionDao.deletePost(context, post.getId());
                                        }).setPositiveButton("Cancel", (dialogInterface, i) -> {
                                }).show();
                                return true;
                            case R.id.editPost:
                                //handle menu2 click
                                context.startActivity(new Intent(context, NewPost.class).putExtra("post", post));
                                return true;

                            default:
                                return false;
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });
        } else {
            holder.optionMenue.setVisibility(View.GONE);
        }
        if (post.getRequests() != null) {
            for (String id : post.getRequests()) {
                if (id.equals(mFirebaseAuth.getUid())) {
                    holder.donnate.setClickable(false);
                    holder.donnate.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gray)));
                    holder.donnate.setText("requested");
                }
            }
        }

        holder.donnate.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Blood Request")
                    .setMessage("Are You Sure You Want To Request Blood From " + post.getName() + " ?!")
                    .setNegativeButton("Ok", (dialogInterface, i) -> {
                        postsCollectionDao.updatePost(post.getId(), mFirebaseAuth.getUid(), () -> {
                            holder.donnate.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gray)));
                            holder.donnate.setText("requested");
                        });
                        holder.donnate.setClickable(false);
                    }).setPositiveButton("Cancel", (dialogInterface, i) -> {
            }).show();

        });

        if (Constants.isAdmin) {
            holder.donnate.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addItems(ArrayList<PostsModel> newNotes) {
        posts.clear();
        posts.addAll(newNotes);
        notifyDataSetChanged();
    }


    public void setNewList(ArrayList<PostsModel> newNotes) {
        this.posts = newNotes;
        notifyDataSetChanged();
    }

    public interface onoPtionSelected {

        void Click(int id);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView bloodType;
        TextView adress;
        TextView timeNeeded;
        ImageView optionMenue;
        ImageView userImage;
        Button donnate;
        TextView tvCity;
        TextView tvArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.post_userName);
            adress = itemView.findViewById(R.id.post_adress);
            bloodType = itemView.findViewById(R.id.post_bloodType);
            timeNeeded = itemView.findViewById(R.id.post_timeNeeded);
            optionMenue = itemView.findViewById(R.id.optionMenue);
            optionMenue.setOnClickListener(v -> menueListener.optionMenueListeners(v, getAdapterPosition()));
            userImage = itemView.findViewById(R.id.userImage);
            donnate = itemView.findViewById(R.id.Donnate_btn);
            tvArea = itemView.findViewById(R.id.tv_area);
            tvCity = itemView.findViewById(R.id.tv_city);
        }

    }

    public interface OptionMenueListener {

        void optionMenueListeners(View v, int position);
    }

    public interface MyAdapterListener {

        void donnateButtonListener(View v, int position);
    }

    public void addFilterItems(ArrayList<PostsModel> sportList) {
        posts.clear();
        posts.addAll(sportList);
        notifyDataSetChanged();
    }

    public void clearFilter() {
        if (posts.size() == base.size())
            return;
        changeAdapterList(base);
        notifyDataSetChanged();
    }


    public void restoreItem(PostsModel post, int position) {

        posts.add(position, post);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        posts.remove(position);
        notifyItemRemoved(position);

    }

    public void filter(String city, String bloodType) {
        ArrayList<PostsModel> filteredList = new ArrayList<>();
        for (PostsModel row : base) {

            if (city != null) {
                if (row.getAddress().toLowerCase().contains(city.toLowerCase()))
                    filteredList.add(row);
            }
            if (bloodType != null) {
                if (row.getBloodType().toLowerCase().equalsIgnoreCase(bloodType.toLowerCase()))
                    filteredList.add(row);
            }

            if (city != null) {
                if (row.getName().toLowerCase().contains(city.toLowerCase()))
                    filteredList.add(row);
            }
            if (city != null && row.getArea() != null) {
                if (row.getArea().toLowerCase().contains(city.toLowerCase()))
                    filteredList.add(row);
            }
            if (city != null && row.getCity() != null) {
                if (row.getCity().toLowerCase().contains(city.toLowerCase()))
                    filteredList.add(row);
            }

        }
        postsListFiltered = filteredList;
        changeAdapterList(postsListFiltered);
        notifyDataSetChanged();
    }


}
