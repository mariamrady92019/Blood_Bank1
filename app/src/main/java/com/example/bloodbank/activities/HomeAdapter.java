package com.example.bloodbank.activities;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbank.Base.BaseViewHolder;
import com.example.bloodbank.R;
import com.example.bloodbank.bloodBank.BankCollectionDao;
import com.example.bloodbank.bloodBank.BloodBankModel;
import com.example.bloodbank.constant.Constants;
import com.example.bloodbank.fireStoreDataBase.posts.EditePost;
import com.example.bloodbank.fireStoreDataBase.posts.PostsModel;
import com.example.bloodbank.hospitals.HospitalCollectionDao;
import com.example.bloodbank.hospitals.HospitalModel;
import com.example.bloodbank.postsRecycler.PostsAdapter;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_POST = 1;
    public static final int VIEW_TYPE_HOSPITAL = 2;
    public static final int VIEW_TYPE_BLOOD_BANK = 3;
    public static final int VIEW_TYPE_BLOOD_TYPE = 4;
    public int type;

    private Callback mCallback;


    public MyAdapterListener onClickListener;
    public OptionMenueListener menueListener;

    public interface OptionMenueListener {

        void optionMenueListeners(View v, int position);
    }

    public interface MyAdapterListener {

        void donnateButtonListener(View v, int position);
    }

    private List<Object> response;

    ArrayList<PostsModel> posts = new ArrayList();
    ArrayList<HospitalModel> hospitals = new ArrayList<>();


    ArrayList<BloodBankModel> banks = new ArrayList();
    static PostsAdapter.onoPtionSelected onoPtionSelected;


    public HomeAdapter(List<Object> sportList) {
        response = sportList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_POST:
                return new PostViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
            case VIEW_TYPE_HOSPITAL:
                return new HospitalViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital, parent, false));
            case VIEW_TYPE_BLOOD_BANK:
                return new BloodBankViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_bank, parent, false));
            case VIEW_TYPE_BLOOD_TYPE:
                return new BloodTypeViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blood_type, parent, false));
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (response != null && response.size() > position) {
            if (type == 0) {
                return VIEW_TYPE_POST;
            } else if (type == 1) {
                return VIEW_TYPE_HOSPITAL;
            } else if (type == 2) {
                return VIEW_TYPE_BLOOD_BANK;
            } else if (type == 3) {
                return VIEW_TYPE_BLOOD_TYPE;
            } else {
                return VIEW_TYPE_EMPTY;
            }
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (response != null && response.size() > 0) {
            return response.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Object> sportList) {
        response.addAll(sportList);
        notifyDataSetChanged();
    }

    public interface Callback {
    }


    public class PostViewHolder extends BaseViewHolder {

        TextView userName;
        TextView bloodType;
        TextView adress;
        TextView timeNeeded;
        ImageView optionMenue;
        ImageView userImage;
        Button donnate;

        public PostViewHolder(View itemView) {
            super(itemView);

        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);

            userName = itemView.findViewById(R.id.post_userName);
            adress = itemView.findViewById(R.id.post_adress);
            bloodType = itemView.findViewById(R.id.post_bloodType);
            timeNeeded = itemView.findViewById(R.id.post_timeNeeded);
            optionMenue = itemView.findViewById(R.id.optionMenue);
            optionMenue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menueListener.optionMenueListeners(v, getAdapterPosition());
                }
            });
            userImage = itemView.findViewById(R.id.userImage);

            donnate = itemView.findViewById(R.id.Donnate_btn);
            donnate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.donnateButtonListener(v, getAdapterPosition());
                }
            });
        }

    }

    public class HospitalViewHolder extends BaseViewHolder {

        protected CircularImageView hospitalImage;
        protected TextView hospitalName;
        protected TextView hospitalAdress;
        protected ImageView optionMenue;
        protected CardView card;

        public HospitalViewHolder(View itemView) {
            super(itemView);

        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            hospitalImage = itemView.findViewById(R.id.hospitalImage);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            hospitalAdress = itemView.findViewById(R.id.hospitalAdress);
            optionMenue = itemView.findViewById(R.id.optionMenue);
            card = itemView.findViewById(R.id.card);

            HospitalModel hospitalModel = hospitals.get(position);
            hospitalName.setText(hospitalModel.getHospName());
            hospitalAdress.setText(hospitalModel.getHospAdress());
            Glide.with(itemView.getContext())
                    .load(hospitalModel.getImageUri())
                    .into(hospitalImage);


            if (Constants.isAdmin == true) {
                optionMenue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(itemView.getContext(), optionMenue);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.hospitaloptionmenue);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.deleteHospital:
                                        //handle menu1 click

                                        removeItem(position);

                                        HospitalCollectionDao.deleteHospitalById(
                                                itemView.getContext(), hospitalModel.getId());

                                        return true;
                                    case R.id.editHospital:
                                        //handle menu2 click

                                        return true;

                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();

                    }

                });
            } else {
                optionMenue.setVisibility(View.GONE);
            }

            optionMenue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menueListener.optionMenueListeners(v, getAdapterPosition());
                }
            });


        }

        public void removeItem(int position) {
            hospitals.remove(position);
            notifyItemRemoved(position);

        }
    }

    public class BloodBankViewHolder extends BaseViewHolder {

        TextView bankName;
        TextView adress;
        TextView a_quantity;
        TextView ab_quantity;
        TextView b_quantity;
        TextView o_quantity;

        ImageView optionMenue;
        ImageView bankImage;

        public BloodBankViewHolder(View itemView) {
            super(itemView);

        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            bankName = itemView.findViewById(R.id.bankName);
            adress = itemView.findViewById(R.id.bankAdress);
            a_quantity = itemView.findViewById(R.id.A_TypeQuantity);
            b_quantity = itemView.findViewById(R.id.B_TypeQuantity);
            ab_quantity = itemView.findViewById(R.id.AB_TypeQuantity);
            o_quantity = itemView.findViewById(R.id.O_TypeQuantity);
            bankImage = itemView.findViewById(R.id.bankImage);

            BloodBankModel bank = banks.get(position);
            bankName.setText(bank.getName());
            adress.setText(bank.getAddress());





            if (Constants.isAdmin == true) {
                optionMenue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(itemView.getContext(), optionMenue);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.bankoptionmenue);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.deleteBank:
                                        //handle menu1 click

                                        removeItem(position);
                                        BankCollectionDao bankCollectionDao =
                                                new BankCollectionDao();
                                        bankCollectionDao.deletePost(itemView.getContext(), bank.getId());

                                        return true;
                                    case R.id.editBank:
                                        //handle menu2 click
                                        Intent intent = new Intent(itemView.getContext(), EditePost.class);
                                        intent.putExtra("bankId", bank.getId());
                                        intent.putExtra("this bank", bank);
                                        itemView.getContext().startActivity(intent);

                                        return true;

                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();

                    }

                });
            } else {
                optionMenue.setVisibility(View.GONE);
            }


            optionMenue = itemView.findViewById(R.id.optionMenue);
            optionMenue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menueListener.optionMenueListeners(v, getAdapterPosition());
                }
            });

        }

        public void removeItem(int position) {
            banks.remove(position);
            notifyItemRemoved(position);

        }
    }


    public class BloodTypeViewHolder extends BaseViewHolder {

        protected CircularImageView hospitalImage;
        protected TextView hospitalName;
        protected TextView hospitalAdress;
        protected ImageView optionMenue;
        protected CardView card;

        public BloodTypeViewHolder(View itemView) {
            super(itemView);

        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
//            hospitalImage = (CircularImageView) itemView.findViewById(R.id.hospitalImage);
//            hospitalName = (TextView) itemView.findViewById(R.id.hospitalName);
//            hospitalAdress = (TextView) itemView.findViewById(R.id.hospitalAdress);
//            optionMenue = (ImageView) itemView.findViewById(R.id.optionMenue);
//            card = (CardView) itemView.findViewById(R.id.card);
//
//            HospitalModel hospitalModel = hospitals.get(position);
//            hospitalName.setText(hospitalModel.getHospName());
//            hospitalAdress.setText(hospitalModel.getHospAdress());
//            Glide.with(itemView.getContext())
//                    .load(hospitalModel.getImageUri())
//                    .into(hospitalImage);
//
//
//            if (Constants.isAdmin == true) {
//                optionMenue.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //creating a popup menu
//                        PopupMenu popup = new PopupMenu(itemView.getContext(), optionMenue);
//                        //inflating menu from xml resource
//                        popup.inflate(R.menu.hospitaloptionmenue);
//                        //adding click listener
//                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                switch (item.getItemId()) {
//                                    case R.id.deleteHospital:
//                                        //handle menu1 click
//
//                                        removeItem(position);
//
//                                        HospitalCollectionDao.deleteHospitalById(
//                                                itemView.getContext(), hospitalModel.getId());
//
//                                        return true;
//                                    case R.id.editHospital:
//                                        //handle menu2 click
//
//                                        return true;
//
//                                    default:
//                                        return false;
//                                }
//                            }
//                        });
//                        //displaying the popup
//                        popup.show();
//
//                    }
//
//                });
//            } else {
//                optionMenue.setVisibility(View.GONE);
//            }
//
//            optionMenue.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    menueListener.optionMenueListeners(v, getAdapterPosition());
//                }
//            });


        }

        public void removeItem(int position) {
            hospitals.remove(position);
            notifyItemRemoved(position);

        }
    }


    public class EmptyViewHolder extends BaseViewHolder {

        TextView buttonRetry;

        EmptyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }

    }
}
