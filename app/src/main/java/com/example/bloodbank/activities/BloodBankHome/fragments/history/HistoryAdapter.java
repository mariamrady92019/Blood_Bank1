package com.example.bloodbank.activities.BloodBankHome.fragments.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.Base.BaseViewHolder;
import com.example.bloodbank.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Mohamed El Banna On 7/13/2020
 **/
public class HistoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "DonorsAdapter";

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_ARTIST = 1;


    private List<Map<String, Object>> mArtistsList;


    public HistoryAdapter(List<Map<String, Object>> sportList) {
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
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
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


    public class BloodTypeViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_content)
        TextView tvContent;

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
                if (dataBean.get("date") != null)
                    tvDate.setText(String.valueOf(dataBean.get("date")));
                if (dataBean.get("action") != null)
                    tvContent.setText(String.valueOf(dataBean.get("action")));
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
