package com.example.bloodbank.bloodBank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.example.bloodbank.activities.AddNewBankActivity;
import com.example.bloodbank.activities.donner.AddDonationActivity;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.fireStoreDataBase.history.HistoryDao;
import com.example.bloodbank.fireStoreDataBase.history.HistoryResponse;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.example.bloodbank.postsRecycler.PostsAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BankRecyclerAdapter extends RecyclerView.Adapter<BankRecyclerAdapter.ViewHolder> {
    public PostsAdapter.OptionMenueListener menueListener;

    ArrayList<BloodBankModel> banks = new ArrayList();
    ArrayList<BloodBankModel> base = new ArrayList();
    ArrayList<BloodBankModel> filtered = new ArrayList();
    UserResponse mUserResponse;

    Context context;
    static PostsAdapter.onoPtionSelected onoPtionSelected;

    public BankRecyclerAdapter(ArrayList banks, Context context) {
        this.banks = banks;
        this.base = banks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blood_bank, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BloodBankModel bank = banks.get(position);
        if (bank != null) {
            holder.bankName.setText(bank.getName());
            holder.adress.setText(bank.getAddress());
            holder.tvCity.setText(bank.getCity());
            holder.tvArea.setText(bank.getArea());
        }

        if (Constants.isAdmin) {
            holder.btn_donate.setVisibility(View.GONE);
            holder.optionMenue.setOnClickListener(view -> {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.optionMenue);
                //inflating menu from xml resource
                popup.inflate(R.menu.bankoptionmenue);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteBank:
                                //handle menu1 click
                                String action = "Delete";
                                new AlertDialog.Builder(context)
                                        .setTitle(action)
                                        .setMessage("Are You Sure You Want To " + action + " ?!")
                                        .setNegativeButton("Ok", (dialogInterface, i) -> {
                                            BankCollectionDao bankCollectionDao = new BankCollectionDao();
                                            bankCollectionDao.deletePost(context, bank.getId());
                                            removeItem(position);
                                        }).setPositiveButton("Cancel", (dialogInterface, i) -> {
                                }).show();

                                return true;
                            case R.id.editBank:
                                context.startActivity(new Intent(context, AddNewBankActivity.class).putExtra("bank", bank));
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            });
        } else {
            holder.optionMenue.setVisibility(View.GONE);
        }

        holder.btn_available_types.setOnClickListener(view ->
                context.startActivity(new Intent(context, AvailableTypesActivity.class).putExtra("bank", bank)));

        UsersCollectionDao.getUserById(FirebaseAuth.getInstance().getUid(), result -> {
            mUserResponse = (UserResponse) result;
            if (mUserResponse.getDonation() != null && mUserResponse.getDonation().equalsIgnoreCase("1"))
                holder.btn_donate.setVisibility(View.GONE);

            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("patient_id", mUserResponse.getId());
            stringObjectMap.put("approved", true);

            if (bank.getRequests() != null && bank.getRequests().contains(stringObjectMap))
                holder.btn_join_blood_bank.setVisibility(View.GONE);
        });

        holder.btn_donate.setOnClickListener(view -> {
            context.startActivity(new Intent(context, AddDonationActivity.class).putExtra("bank", bank));
        });


        holder.btn_join_blood_bank.setOnClickListener(view -> {

            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("patient_id", FirebaseAuth.getInstance().getUid());
            stringObjectMap.put("approved", false);
            List<Map<String, Object>> mList = new ArrayList<>();
            mList.add(stringObjectMap);

            BankCollectionDao.updateBankByRequests(bank.getId(), mList, result -> {
                if ((boolean) result) {
                    Toast.makeText(context, "Request Sent Successfully", Toast.LENGTH_SHORT).show();
                    List<Map<String, Object>> mapList = new ArrayList<>();
                    Map<String, Object> mMap = new HashMap<>();
                    mMap.put("action", "You have Requested to join " + bank.getName() + " Bank");
                    mMap.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en")).format(new Date()));
                    mapList.add(mMap);
                    HistoryResponse mHistoryResponse = new HistoryResponse();
                    mHistoryResponse.setHistory(mapList);
                    mHistoryResponse.setId(mUserResponse.getId());
                    HistoryDao.addHistory(mHistoryResponse, result1 -> {
                    });
                    UsersCollectionDao.setUserHistory(mUserResponse.getId(), mHistoryResponse, result3 -> {
                    });
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    public void addItems(ArrayList<BloodBankModel> newBanks) {
        banks.clear();
        banks.addAll(newBanks);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bankName;
        TextView adress;
        TextView a_quantity;
        TextView ab_quantity;
        TextView b_quantity;
        TextView o_quantity;
        TextView tvCity;
        TextView tvArea;

        ImageView optionMenue;
        ImageView bankImage;

        Button btn_available_types;
        Button btn_donate;
        Button btn_join_blood_bank;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            bankName = itemView.findViewById(R.id.bankName);
            adress = itemView.findViewById(R.id.bankAdress);
            a_quantity = itemView.findViewById(R.id.A_TypeQuantity);
            b_quantity = itemView.findViewById(R.id.B_TypeQuantity);
            ab_quantity = itemView.findViewById(R.id.AB_TypeQuantity);
            o_quantity = itemView.findViewById(R.id.O_TypeQuantity);
            bankImage = itemView.findViewById(R.id.bankImage);
            btn_available_types = itemView.findViewById(R.id.btn_available_types);
            btn_donate = itemView.findViewById(R.id.btn_donate);
            btn_join_blood_bank = itemView.findViewById(R.id.btn_join_blood_bank);
            tvArea = itemView.findViewById(R.id.tv_bank_area);
            tvCity = itemView.findViewById(R.id.tv_bank_city);

            optionMenue = itemView.findViewById(R.id.optionMenue);
            optionMenue.setOnClickListener(v -> menueListener.optionMenueListeners(v, getAdapterPosition()));
        }
    }

    public void clearFilter() {
        if (banks.size() == base.size())
            return;
        changeAdapterList(base);
        notifyDataSetChanged();
    }

    public void setNewList(ArrayList<BloodBankModel> news) {
        this.banks = news;
        notifyDataSetChanged();
    }


    public void changeAdapterList(ArrayList list) {
        this.banks = list;
        notifyDataSetChanged();
    }

    public void restoreItem(BloodBankModel bank, int position) {

        banks.add(position, bank);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        banks.remove(position);
        notifyItemRemoved(position);
    }


    public void filter(String city, String name) {
        ArrayList<BloodBankModel> filteredList = new ArrayList<>();
        for (BloodBankModel row : base) {

            // name match condition. this might differ depending on your requirement
            // here we are looking for name or phone number match
            if (city != null) {
                if (row.getAddress().toLowerCase().contains(city.toLowerCase()))
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
            if (name != null) {
                if (row.getName().toLowerCase().contains(name.toLowerCase()))
                    filteredList.add(row);
            }
        }
        filtered = filteredList;
        changeAdapterList(filtered);
        notifyDataSetChanged();
    }


}
