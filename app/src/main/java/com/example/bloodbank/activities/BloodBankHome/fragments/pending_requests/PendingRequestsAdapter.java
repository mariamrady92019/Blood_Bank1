package com.example.bloodbank.activities.BloodBankHome.fragments.pending_requests;

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
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/13/2020
 **/
public class PendingRequestsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "DonorsAdapter";

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_ARTIST = 1;


    private List<Map<String, Object>> mArtistsList;

    String patientId;

    public PendingRequestsAdapter(List<Map<String, Object>> sportList) {
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
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_requests, parent, false));
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

    public void addItems(List<Map<String, Object>> sportList) {
        mArtistsList.clear();
        mArtistsList.addAll(sportList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mArtistsList.remove(position);
        notifyItemRemoved(position);
    }


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public class BloodTypeViewHolder extends BaseViewHolder {
        @BindView(R.id.patientName)
        TextView patientName;
        @BindView(R.id.patientAdress)
        TextView patientAdress;
        @BindView(R.id.tv_city)
        TextView tvCity;
        @BindView(R.id.tv_area)
        TextView tvArea;
        @BindView(R.id.tv_blood_type)
        TextView tvBloodType;
        @BindView(R.id.tv_phone_number)
        TextView tvPhoneNumber;
        @BindView(R.id.btn_accept)
        Button btnAccept;

        public BloodTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);


            Map<String, Object> dataBean = mArtistsList.get(position);


            if (dataBean != null) {

                if ((boolean) dataBean.get("approved")) itemView.setVisibility(View.GONE);

                if (!(boolean) dataBean.get("approved"))
                    UsersCollectionDao.getUserById(String.valueOf(dataBean.get("patient_id")), result -> {
                        setPatientId(String.valueOf(dataBean.get("patient_id")));
                        UserResponse mResponse;
                        mResponse = (UserResponse) result;
                        if (mResponse != null) {
                            if (mResponse.getName() != null)
                                patientName.setText(mResponse.getName());
                            if (mResponse.getAddress() != null)
                                patientAdress.setText(mResponse.getAddress());
                            if (mResponse.getBloodType() != null)
                                tvBloodType.setText(mResponse.getBloodType());
                            if (mResponse.getPhoneNumber() != null)
                                tvPhoneNumber.setText(mResponse.getPhoneNumber());
                        }
                    });
            }
            btnAccept.setOnClickListener(view -> {
                Map<String, Object> stringObjectMap = new HashMap<>();
                stringObjectMap.put("patient_id", getPatientId());
                stringObjectMap.put("approved", true);
                List<Map<String, Object>> mList = new ArrayList<>();
                mList.add(stringObjectMap);
                BankCollectionDao.updateBankByRequests(FirebaseAuth.getInstance().getUid(), mList, result -> {
                    Toast.makeText(itemView.getContext(), "Request Approved Successfully", Toast.LENGTH_SHORT).show();
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
