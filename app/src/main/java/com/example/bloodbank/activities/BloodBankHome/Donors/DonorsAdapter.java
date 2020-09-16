package com.example.bloodbank.activities.BloodBankHome.Donors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.Base.BaseViewHolder;
import com.example.bloodbank.R;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerDao;
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/13/2020
 **/
public class DonorsAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_ARTIST = 1;


    private List<String> mArtistsList;


    public DonorsAdapter(List<String> sportList) {
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

    public void addItems(List<String> sportList) {
        mArtistsList.clear();
        mArtistsList.addAll(sportList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mArtistsList.remove(position);
        notifyItemRemoved(position);
    }


    public class BloodTypeViewHolder extends BaseViewHolder {


        @BindView(R.id.post_userName)
        TextView postUserName;
        @BindView(R.id.post_bloodType)
        TextView postBloodType;
        @BindView(R.id.post_adress)
        TextView postAdress;
        @BindView(R.id.bankName)
        TextView bankName;
        @BindView(R.id.bankAdress)
        TextView bankAdress;
        @BindView(R.id.tv_bank_city)
        TextView tvBankCity;
        @BindView(R.id.tv_bank_area)
        TextView tvBankArea;
        @BindView(R.id.btn_approve)
        Button btnApprove;
        @BindView(R.id.btn_cancel)
        Button btnCancel;
        @BindView(R.id.tv_blood_type)
        TextView tvBloodType;

        public BloodTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);


            String dataBean = mArtistsList.get(position);


            if (dataBean != null) {
                DonnerDao.getDonorById(dataBean, result -> {
                    DonnerResponse mDonnerResponse = (DonnerResponse) result;
                    postUserName.setText(mDonnerResponse.getName());
                    postBloodType.setText(mDonnerResponse.getBloodType());
                    postAdress.setText(mDonnerResponse.getAddress());
                    BankCollectionDao.getBankById(mDonnerResponse.getBloodBankId(), result2 -> {
                        BloodBankModel mBloodBankModel = (BloodBankModel) result2;
                        bankName.setText(mBloodBankModel.getName());
                        bankAdress.setText(mBloodBankModel.getAddress());
                    });
                });
            }


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
