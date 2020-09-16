package com.example.bloodbank.activities.donner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.Base.BaseViewHolder;
import com.example.bloodbank.R;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerDao;
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerResponse;

import java.util.List;

public class DonnerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "DonnerAdapter";

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_ARTIST = 1;

    private List<DonnerResponse> mArtistsList;
    BloodBankModel mBloodBankModel = new BloodBankModel();

    public DonnerAdapter(List<DonnerResponse> sportList) {
        this.mArtistsList = sportList;
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_ARTIST:
                return new BloodTypeViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_type, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mArtistsList != null && mArtistsList.size() > 0) {
            return VIEW_TYPE_ARTIST;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mArtistsList != null && mArtistsList.size() > 0) {
            return mArtistsList.size();
        } else {
            return 0;
        }
    }

    public void addItems(List<DonnerResponse> sportList) {
        mArtistsList.clear();
        mArtistsList.addAll(sportList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mArtistsList.remove(position);
        notifyItemRemoved(position);
    }


    public class BloodTypeViewHolder extends BaseViewHolder {

        TextView tvDonnerName;
        TextView tvDonnerAddress;
        TextView tvDonnerBloodType;
        TextView tvBankName;
        TextView tvBankAddress;
        TextView tvBankCity;
        TextView tvBankArea;
        Button btnCancel;
        Button btnApprove;

        public BloodTypeViewHolder(View itemView) {
            super(itemView);
            tvDonnerName = itemView.findViewById(R.id.post_userName);
            tvDonnerAddress = itemView.findViewById(R.id.post_adress);
            tvDonnerBloodType = itemView.findViewById(R.id.post_bloodType);
            tvBankName = itemView.findViewById(R.id.bankName);
            tvBankAddress = itemView.findViewById(R.id.bankAdress);
            tvBankCity = itemView.findViewById(R.id.tv_bank_city);
            tvBankArea = itemView.findViewById(R.id.tv_bank_area);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
            btnApprove = itemView.findViewById(R.id.btn_approve);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);

            DonnerResponse dataBean = mArtistsList.get(position);


            if (dataBean != null) {
                if (Constants.isAdmin) {
                    tvDonnerName.setText(dataBean.getName());
                    tvDonnerAddress.setText(dataBean.getAddress());
                    tvDonnerBloodType.setText(dataBean.getBloodType());
                    BankCollectionDao.getBankById(dataBean.getBloodBankId(), result -> {
                        mBloodBankModel = (BloodBankModel) result;
                        if (mBloodBankModel != null) {
                            tvBankName.setText(mBloodBankModel.getName());
                            tvBankAddress.setText(mBloodBankModel.getAddress());
                            tvBankCity.setText(mBloodBankModel.getCity());
                            tvBankArea.setText(mBloodBankModel.getArea());
                        }
                    });
                    if (!dataBean.isApproved()) {
                        btnApprove.setVisibility(View.VISIBLE);
                        btnApprove.setOnClickListener(view -> {
                            DonnerDao.setApproved(dataBean.getId(), result -> {
                                if ((boolean) result) {
                                    Toast.makeText(itemView.getContext(), "Approved", Toast.LENGTH_SHORT).show();
                                    btnApprove.setVisibility(View.GONE);
                                }
                            });
                        });
                    }
                }
                {
                    if (dataBean.isApproved()) {
                        tvDonnerName.setText(dataBean.getName());
                        tvDonnerAddress.setText(dataBean.getAddress());
                        tvDonnerBloodType.setText(dataBean.getBloodType());
                        BankCollectionDao.getBankById(dataBean.getBloodBankId(), result -> {
                            mBloodBankModel = (BloodBankModel) result;
                            if (mBloodBankModel != null) {
                                tvBankName.setText(mBloodBankModel.getName());
                                tvBankAddress.setText(mBloodBankModel.getAddress());
                                tvBankCity.setText(mBloodBankModel.getCity());
                                tvBankArea.setText(mBloodBankModel.getArea());
                            }
                        });
                    } else {
                        if (!Constants.isAdmin)
                            itemView.setVisibility(View.GONE);
                    }
                }
            }

            btnCancel.setOnClickListener(view -> {
                if (dataBean != null)
                    DonnerDao.deleteDonner(dataBean.getId(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(itemView.getContext(), "Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                            removeItem(position);
                        }
                    });
            });
        }


    }

    public class EmptyViewHolder extends BaseViewHolder {


        EmptyViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        protected void clear() {

        }

    }
}
