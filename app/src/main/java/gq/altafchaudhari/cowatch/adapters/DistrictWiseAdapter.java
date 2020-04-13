package gq.altafchaudhari.cowatch.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gq.altafchaudhari.cowatch.BaseViewHolder;
import gq.altafchaudhari.cowatch.R;
import gq.altafchaudhari.cowatch.model.districts.District;


public class DistrictWiseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "StateWiseAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    private Callback mCallback;
    private List<District> districtList;
    Context mContext;

    public DistrictWiseAdapter(Context context, List<District> list) {
        districtList = list;
        mContext = context;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_state, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_list, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (districtList != null && districtList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }
    @Override
    public int getItemCount() {
        if (districtList != null && districtList.size() > 0) {
            return districtList.size();
        } else {
            return 1;
        }
    }
    public void addItems(List<District> statewiseList) {
        this.districtList.addAll(statewiseList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.countryName)
        TextView countryName;
        @BindView(R.id.newCasesCount)
        TextView newCasesCount;
        @BindView(R.id.confirmedCount)
        TextView confirmedCount;
        @BindView(R.id.recoveredCount)
        TextView recoveredCount;
        @BindView(R.id.deathCount)
        TextView deathCount;
        @BindView(R.id.pinImageView)
        ImageView pinImageView;
        @BindView(R.id.pinnedImageView)
        ImageView pinnedImageView;/*
        @BindView(R.id.cardRootView)
        ConstraintLayout cardRootView;*/

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        protected void clear() {
            countryName.setText("----");
            newCasesCount.setText("000");
            confirmedCount.setText("000");
            recoveredCount.setText("000");
            deathCount.setText("000");
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onBind(int position) {
            super.onBind(position);

            Object sate_obj = districtList.get(position);
            final District district_obj = (District) sate_obj;

            DecimalFormat decimalFormat = new DecimalFormat("#");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);

            String district = district_obj.getDistrict();
            long newConfirmed = Long.parseLong(district_obj.getDelta_confirmed());
            long totalConfirmed = Long.parseLong(district_obj.getConfirmed());
            /*long totalRecovered = Long.parseLong(statewise.getRecovered());
            long totalDeath = Long.parseLong(statewise.getDeaths());*/

            newCasesCount.setVisibility(View.GONE);
            countryName.setText(district.toUpperCase());
            newCasesCount.setText(decimalFormat.format(newConfirmed));
            confirmedCount.setText(decimalFormat.format(totalConfirmed));
            /*recoveredCount.setText(decimalFormat.format(totalRecovered));
            deathCount.setText(decimalFormat.format(totalDeath));*/

        }
    }



    public class EmptyViewHolder extends BaseViewHolder {
       /* @BindView(R.id.tv_message)
        TextView messageTextView;
        @BindView(R.id.buttonRetry)
        TextView buttonRetry;*/
        EmptyViewHolder(View itemView) {
            super(itemView);
           /* ButterKnife.bind(this, itemView);
            buttonRetry.setOnClickListener(v -> mCallback.onEmptyViewRetryClick());*/
        }
        @Override
        protected void clear() {
        }
    }



}
