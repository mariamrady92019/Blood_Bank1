package com.example.bloodbank.activities.BloodBankHome.fragments.pathients;

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
import com.example.bloodbank.fireStoreDataBase.PostRequest.PostRequestDao;
import com.example.bloodbank.fireStoreDataBase.PostRequest.PostRequestResponse;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/13/2020
 **/
public class PatientsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "DonorsAdapter";
    PatientsAdapterCallBack mCallBack;

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_ARTIST = 1;

    boolean isPostRequestVisible = false;

    private List<Map<String, Object>> mArtistsList;

    interface PatientsAdapterCallBack {
        void updateBloodBankModel();
    }


    public void setCallBack(PatientsAdapterCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    String patientId;

    BloodBankModel bloodBankModel;

    UserResponse mResponse;

    public PatientsAdapter(List<Map<String, Object>> sportList) {
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
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_patients, parent, false));
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


    public void setBloodBankModel(BloodBankModel bloodBankModel) {
        this.bloodBankModel = bloodBankModel;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public class BloodTypeViewHolder extends BaseViewHolder {


        @BindView(R.id.patientName)
        TextView patientName;
        @BindView(R.id.patientAdress)
        TextView patientAdress;
        @BindView(R.id.tv_blood_type)
        TextView tvBloodType;
        @BindView(R.id.tv_phone_number)
        TextView tvPhoneNumber;
        @BindView(R.id.btn_give)
        Button btnGive;
        @BindView(R.id.btn_post_request)
        Button btnPostRequest;


        public BloodTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);


            Map<String, Object> dataBean = mArtistsList.get(position);

            setBtnPostRequestVisibility(false);

            if (dataBean != null) {

                if (!(boolean) dataBean.get("approved")) itemView.setVisibility(View.GONE);
                if ((boolean) dataBean.get("approved"))
                    UsersCollectionDao.getUserById(String.valueOf(dataBean.get("patient_id")), result -> {
                        setPatientId(String.valueOf(dataBean.get("patient_id")));
                        mResponse = (UserResponse) result;

                        if (mResponse.getName() != null)
                            patientName.setText(mResponse.getName());
                        if (mResponse.getAddress() != null)
                            patientAdress.setText(mResponse.getAddress());
                        if (mResponse.getBloodType() != null)
                            tvBloodType.setText(mResponse.getBloodType());
                        if (mResponse.getPhoneNumber() != null)
                            tvPhoneNumber.setText(mResponse.getPhoneNumber());

                        btnGive.setOnClickListener(view -> {
                            if (getBloodTypeValue(mResponse.getBloodType(), bloodBankModel) > 0) {
                                BankCollectionDao.updateBloodTypeQuantity(bloodBankModel.getId(), getBloodTypeValue(mResponse.getBloodType(), bloodBankModel) - 1, getBloodTypeKey(mResponse.getBloodType()), res -> {
                                    if ((boolean) res) {
                                        Toast.makeText(itemView.getContext(), "Yoh Have Supplied " + mResponse.getName() + "with Blood Type " + mResponse.getBloodType() + " Successfully", Toast.LENGTH_SHORT).show();
                                        mCallBack.updateBloodBankModel();
                                    }
                                });
                            } else {
                                Toast.makeText(itemView.getContext(), "Type not Available You Can Post Request", Toast.LENGTH_SHORT).show();
                                setBtnPostRequestVisibility(true);
                            }
                        });


                        btnPostRequest.setOnClickListener(view -> {
                            PostRequestResponse mPostRequestResponse = new PostRequestResponse();
                            mPostRequestResponse.setBloodBankId(bloodBankModel.getId());
                            mPostRequestResponse.setUserId(mResponse.getId());
                            mPostRequestResponse.setId(bloodBankModel.getId() + mResponse.getId());
                            PostRequestDao.addPostRequest(mPostRequestResponse, result1 -> {
                                if ((boolean) result1) {
                                    Toast.makeText(itemView.getContext(), "Post Add Successfully", Toast.LENGTH_SHORT).show();
                                    setBtnPostRequestVisibility(false);
                                }
                            });
                        });
                    });

            }


        }

        String getBloodTypeKey(String type) {
            if (type.equalsIgnoreCase("A+"))
                return "numberOf_A";
            if (type.equalsIgnoreCase("A-"))
                return "numberOf_A2";
            if (type.equalsIgnoreCase("B+"))
                return "numberOf_B";
            if (type.equalsIgnoreCase("B-"))
                return "numberOf_B2";
            if (type.equalsIgnoreCase("O+"))
                return "numberOf_O";
            if (type.equalsIgnoreCase("O-"))
                return "numberOf_O2";
            if (type.equalsIgnoreCase("AB+"))
                return "numberOf_AB";
            if (type.equalsIgnoreCase("AB-"))
                return "numberOf_AB2";
            return null;
        }

        long getBloodTypeValue(String type, BloodBankModel mBloodBankModel) {
            if (type.equalsIgnoreCase("A+"))
                return mBloodBankModel.getNumberOf_A();
            if (type.equalsIgnoreCase("A-"))
                return mBloodBankModel.getNumberOf_A2();
            if (type.equalsIgnoreCase("B+"))
                return mBloodBankModel.getNumberOf_B();
            if (type.equalsIgnoreCase("B-"))
                return mBloodBankModel.getNumberOf_B2();
            if (type.equalsIgnoreCase("O+"))
                return mBloodBankModel.getNumberOf_O();
            if (type.equalsIgnoreCase("O-"))
                return mBloodBankModel.getNumberOf_O2();
            if (type.equalsIgnoreCase("AB+"))
                return mBloodBankModel.getNumberOf_AB();
            if (type.equalsIgnoreCase("AB-"))
                return mBloodBankModel.getNumberOf_AB2();
            return 0;
        }

        void setBtnPostRequestVisibility(boolean b) {
            if (b) {
                btnPostRequest.setVisibility(View.VISIBLE);
            } else btnPostRequest.setVisibility(View.GONE);
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
