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
import gq.altafchaudhari.cowatch.model.states.Statewise;


public class StateWiseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = "StateWiseAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    private Callback mCallback;
    private List<Statewise> statewiseList;
    Context mContext;

    public StateWiseAdapter(Context context, List<Statewise> list) {
        statewiseList = list;
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
                                .inflate(R.layout.layout_empty, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (statewiseList != null && statewiseList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }
    @Override
    public int getItemCount() {
        if (statewiseList != null && statewiseList.size() > 0) {
            return statewiseList.size();
        } else {
            return 1;
        }
    }
    public void addItems(List<Statewise> statewiseList) {
        this.statewiseList.addAll(statewiseList);
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

            Object sate_obj = statewiseList.get(position);
            final Statewise statewise = (Statewise) sate_obj;

            DecimalFormat decimalFormat = new DecimalFormat("#");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);

            String state = statewise.getState();
            long newConfirmed = Long.parseLong(statewise.getDeltaconfirmed());
            long totalConfirmed = Long.parseLong(statewise.getConfirmed());
            long totalRecovered = Long.parseLong(statewise.getRecovered());
            long totalDeath = Long.parseLong(statewise.getDeaths());

            //newCasesCount.setVisibility(View.GONE);

            countryName.setText(state.toUpperCase());
            newCasesCount.setText(decimalFormat.format(newConfirmed));
            confirmedCount.setText(decimalFormat.format(totalConfirmed));
            recoveredCount.setText(decimalFormat.format(totalRecovered));
            deathCount.setText(decimalFormat.format(totalDeath));

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
