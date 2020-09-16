package com.example.bloodbank.hospitals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbank.R;
import com.example.bloodbank.activities.AddNewHospitalActivity;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.postsRecycler.PostsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class HospitalRecyclerAdapter extends RecyclerView.Adapter<HospitalRecyclerAdapter.ViewHolder> {

    ArrayList<HospitalModel> hospitals = new ArrayList<>();
    ArrayList<HospitalModel> base = new ArrayList<>();
    ArrayList<HospitalModel> filtered = new ArrayList<>();

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();


    Context context;
    public PostsAdapter.OptionMenueListener menueListener;


    public HospitalRecyclerAdapter(ArrayList<HospitalModel> list, Context context) {
        this.context = context;
        this.base = list;
        this.hospitals = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hospital, parent, false);

        return new HospitalRecyclerAdapter.ViewHolder(view);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HospitalModel hospitalModel = hospitals.get(position);
        holder.hospitalName.setText(hospitalModel.getHospName());
        holder.hospitalAdress.setText(hospitalModel.getHospAdress());
        holder.tvCity.setText(hospitalModel.getCity());
        holder.tvArea.setText(hospitalModel.getArea());
        Glide.with(context)
                .load(hospitalModel.getImageUri())
                .into(holder.hospitalImage);


        if (Constants.isAdmin) {
            holder.btnRequest.setVisibility(View.GONE);
            holder.optionMenue.setOnClickListener(view -> {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.optionMenue);
                //inflating menu from xml resource
                popup.inflate(R.menu.hospitaloptionmenue);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.deleteHospital:
                            //handle menu1 click
                            String action = "Delete";
                            new AlertDialog.Builder(context)
                                    .setTitle(action)
                                    .setMessage("Are You Sure You Want To " + action + " ?!")
                                    .setNegativeButton("Ok", (dialogInterface, i) -> {
                                        removeItem(position);
                                        HospitalCollectionDao.deleteHospitalById(context, hospitalModel.getId());
                                    }).setPositiveButton("Cancel", (dialogInterface, i) -> {
                            }).show();


                            return true;
                        case R.id.editHospital:
                            //handle menu2 click
                            context.startActivity(new Intent(context, AddNewHospitalActivity.class).putExtra("hospital", hospitalModel));
                            return true;

                        default:
                            return false;
                    }
                });
                //displaying the popup
                popup.show();

            });
        } else {
            holder.optionMenue.setVisibility(View.GONE);
        }

        if (hospitalModel.getRequests() != null) {
            for (String id : hospitalModel.getRequests()) {
                if (id.equals(mFirebaseAuth.getUid())) {
                    holder.btnRequest.setClickable(false);
                    holder.btnRequest.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gray)));
                    holder.btnRequest.setText("requested");
                }
            }
        }

        holder.btnRequest.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Blood Request")
                    .setMessage("Are You Sure You Want To Request Blood From " + hospitalModel.getHospName() + " ?!")
                    .setNegativeButton("Ok", (dialogInterface, i) -> {
                        HospitalCollectionDao.updatePost(hospitalModel.getId(), mFirebaseAuth.getUid(), () -> {
                            holder.btnRequest.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gray)));
                            holder.btnRequest.setText("requested");
                        });
                        holder.btnRequest.setClickable(false);
                    }).setPositiveButton("Cancel", (dialogInterface, i) -> {
            }).show();

        });


    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public void addItems(ArrayList<HospitalModel> newHospital) {
        hospitals.clear();
        hospitals.addAll(newHospital);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        protected CircularImageView hospitalImage;
        protected TextView hospitalName;
        protected TextView hospitalAdress;
        protected ImageView optionMenue;
        protected CardView card;
        TextView tvCity;
        TextView tvArea;
        Button btnRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hospitalImage = (CircularImageView) itemView.findViewById(R.id.hospitalImage);
            hospitalName = (TextView) itemView.findViewById(R.id.hospitalName);
            hospitalAdress = (TextView) itemView.findViewById(R.id.hospitalAdress);
            optionMenue = (ImageView) itemView.findViewById(R.id.optionMenue);
            card = (CardView) itemView.findViewById(R.id.card);
            tvArea = itemView.findViewById(R.id.tv_area);
            tvCity = itemView.findViewById(R.id.tv_city);
            btnRequest = itemView.findViewById(R.id.btn_request);
            optionMenue.setOnClickListener(v -> menueListener.optionMenueListeners(v, getAdapterPosition()));

        }


    }

    public void clearFilter() {
        if (hospitals.size() == base.size())
            return;
        changeAdapterList(base);
        notifyDataSetChanged();
    }

    public void setNewList(ArrayList<HospitalModel> news) {
        this.hospitals = news;
        notifyDataSetChanged();
    }


    public void changeAdapterList(ArrayList list) {
        this.hospitals = list;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        hospitals.remove(position);
        notifyItemRemoved(position);

    }

    public void filter(String city, String name) {
        ArrayList<HospitalModel> filteredList = new ArrayList<>();
        for (HospitalModel row : base) {

            // name match condition. this might differ depending on your requirement
            // here we are looking for name or phone number match
            if (city != null) {
                if (row.getHospAdress().toLowerCase().contains(city.toLowerCase()))
                    filteredList.add(row);
            }
            if (name != null) {
                if (row.getHospName().toLowerCase().contains(name.toLowerCase()))
                    filteredList.add(row);
            }
            if (name != null && row.getArea() != null) {
                if (row.getArea().toLowerCase().contains(city.toLowerCase()))
                    filteredList.add(row);
            }
            if (name != null && row.getCity() != null) {
                if (row.getCity().toLowerCase().contains(name.toLowerCase()))
                    filteredList.add(row);
            }
        }
        filtered = filteredList;
        changeAdapterList(filtered);
        notifyDataSetChanged();
    }

}
