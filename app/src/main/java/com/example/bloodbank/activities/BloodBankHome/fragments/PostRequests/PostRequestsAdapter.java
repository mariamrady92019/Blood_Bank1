package com.example.bloodbank.activities.BloodBankHome.fragments.PostRequests;

import android.app.AlertDialog;
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
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerDao;
import com.example.bloodbank.fireStoreDataBase.Donner.DonnerResponse;
import com.example.bloodbank.fireStoreDataBase.PostRequest.PostRequestResponse;
import com.example.bloodbank.fireStoreDataBase.history.HistoryDao;
import com.example.bloodbank.fireStoreDataBase.history.HistoryResponse;
import com.example.bloodbank.fireStoreDataBase.users.UserResponse;
import com.example.bloodbank.fireStoreDataBase.users.UserTypes;
import com.example.bloodbank.fireStoreDataBase.users.UsersCollectionDao;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/13/2020
 **/
public class PostRequestsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "DonorsAdapter";

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_ARTIST = 1;

    private List<PostRequestResponse> mArtistsList;


    public interface RequestAdapterCallBack {
        void updateBloodBankModel(String bankID);

        void updateCurrentUserResponse(String userID);
    }

    RequestAdapterCallBack mCallBack;

    public void setCallBack(RequestAdapterCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }


    public PostRequestsAdapter(List<PostRequestResponse> sportList) {
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
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_requests, parent, false));
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

    public void addItems(List<PostRequestResponse> sportList) {
        mArtistsList.clear();
        mArtistsList.addAll(sportList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mArtistsList.remove(position);
        notifyItemRemoved(position);
    }


    public class BloodTypeViewHolder extends BaseViewHolder {

        @BindView(R.id.patientName)
        TextView patientName;
        @BindView(R.id.bankName)
        TextView bankName;
        @BindView(R.id.tv_city)
        TextView tvCity;
        @BindView(R.id.tv_area)
        TextView tvArea;
        @BindView(R.id.tv_blood_type)
        TextView tvBloodType;
        @BindView(R.id.tv_phone_number)
        TextView tvPhoneNumber;
        @BindView(R.id.bankAdress)
        TextView bankAdress;
        @BindView(R.id.btn_donate)
        Button btnDonate;


        public BloodTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);


            PostRequestResponse dataBean = mArtistsList.get(position);

            if (dataBean != null) {
                UsersCollectionDao.getUserById(dataBean.getUserId(), result -> {
                    UserResponse mUserResponse = (UserResponse) result;
                    if (mUserResponse != null) {
                        if (mUserResponse.getName() != null)
                            patientName.setText(mUserResponse.getName());
                        if (mUserResponse.getBloodType() != null)
                            tvBloodType.setText(mUserResponse.getBloodType());
                        if (mUserResponse.getPhoneNumber() != null)
                            tvPhoneNumber.setText(mUserResponse.getPhoneNumber());

                        BankCollectionDao.getBankById(dataBean.getBloodBankId(), result2 -> {
                            BloodBankModel mBloodBankModel = (BloodBankModel) result2;
                            if (mBloodBankModel != null) {
                                if (mBloodBankModel.getName() != null)
                                    bankName.setText(mBloodBankModel.getName());
                                if (mBloodBankModel.getAddress() != null)
                                    bankAdress.setText(mBloodBankModel.getAddress());
                            }
                        });
                        UsersCollectionDao.getUserById(FirebaseAuth.getInstance().getUid(), result3 -> {
                            UserResponse mCurrentUserResponse;
                            mCurrentUserResponse = (UserResponse) result3;
                            if (mCurrentUserResponse != null) {
                                if (mCurrentUserResponse.getUserType() != null
                                        && mCurrentUserResponse.getUserType().equals(UserTypes.USER_TYPE_NORMAL.getType())) {

                                    if (mCurrentUserResponse.getDonation() != null
                                            && mCurrentUserResponse.getDonation().equalsIgnoreCase("1")) {
                                        setDonateButtonVisibility(false);
                                    } else setDonateButtonVisibility(true);

                                } else setDonateButtonVisibility(false);

                                btnDonate.setOnClickListener(view -> {

                                    if (mCurrentUserResponse.getBloodType().equalsIgnoreCase(mUserResponse.getBloodType())) {
                                        if (dataBean.getBloodBankId() != null) {
                                            BankCollectionDao.getBankById(dataBean.getBloodBankId(), result1 -> {
                                                BloodBankModel bankModel = (BloodBankModel) result1;
                                                if (result1 != null)
                                                    BankCollectionDao.updateBloodTypeQuantity(dataBean.getBloodBankId(), getBloodTypeValue(mCurrentUserResponse.getBloodType(), bankModel) + 1, getBloodTypeKey(mCurrentUserResponse.getBloodType()), res -> {
                                                        if ((boolean) res) {

                                                            UsersCollectionDao.updateIsDonorUser(mCurrentUserResponse.getId(), "1", result2 -> {
                                                                if ((boolean) result2)
                                                                    showDialog(null);
                                                                mCurrentUserResponse.setDonation("1");
                                                                DonnerDao.addDonner(new DonnerResponse(mCurrentUserResponse, dataBean.getBloodBankId(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en")).format(new Date())), task -> {
                                                                    saveHistory(mUserResponse);
                                                                    setDonateButtonVisibility(false);
                                                                }, e -> {
                                                                    Toast.makeText(itemView.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                                });
                                                            });
                                                            mCallBack.updateBloodBankModel(dataBean.getBloodBankId());
                                                            mCallBack.updateCurrentUserResponse(mCurrentUserResponse.getId());
                                                        }
                                                    });
                                            });

                                        } else showDialog("Blood Bank Error");

                                    } else showDialog("Blood Type Mis Match");
                                });
                            }
                        });
                    }

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

        void setDonateButtonVisibility(boolean isVisible) {
            if (isVisible)
                btnDonate.setVisibility(View.VISIBLE);
            else
                btnDonate.setVisibility(View.GONE);
        }

        void saveHistory(UserResponse mUserResponse) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> mMap = new HashMap<>();
            mMap.put("action", "You have Donated to " + mUserResponse.getName() + " With Blood Type" + mUserResponse.getBloodType());
            mMap.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en")).format(new Date()));
            mapList.add(mMap);
            HistoryResponse mHistoryResponse = new HistoryResponse();
            mHistoryResponse.setHistory(mapList);
            mHistoryResponse.setId(mUserResponse.getId());
            HistoryDao.addHistory(mHistoryResponse, result1 -> {
            });
        }

        void showDialog(String message) {
            String msg = "You are donor Now You can donate again after 3 months ";
            if (message != null)
                msg = message;
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("Alert!")
                    .setMessage(msg)
                    .setCancelable(false)
                    .setNegativeButton("Ok", (dialogInterface, i) -> {
                    }).show();
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
