package gq.altafchaudhari.cowatch.adapters.searchadapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gq.altafchaudhari.cowatch.BaseViewHolder;
import gq.altafchaudhari.cowatch.R;
import gq.altafchaudhari.cowatch.model.states.Statewise;

public class SearchStateAdapter extends RecyclerView.Adapter<BaseViewHolder> implements Filterable {

    private static final String TAG = "SearchAdapter";

    private List<Statewise> mStateList;
    private List<Statewise> mFilteredList;
    private Context mContext;

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    //private Callback mCallback;

    public SearchStateAdapter(Context context, List<Statewise> list) {
        this.mStateList = list;
        this.mFilteredList = list;
        this.mContext = context;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mStateList;
                    Log.i(TAG,"EMPTY");
                } else {
                    ArrayList<Statewise> filterList = new ArrayList<>();
                    for (Statewise state : mStateList) {
                        if (state.getState().toLowerCase().contains(charString)){
                            filterList.add(state);
                        }
                    }
                    mFilteredList = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                mFilteredList = (ArrayList<Statewise>) results.values;
                for(Statewise state: mFilteredList)
                {
                    Log.i(TAG, "results: " + state.getState());
                }
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mStateList != null && mStateList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
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

            Object state_obj = mFilteredList.get(position);
            final Statewise mState = (Statewise) state_obj;

            DecimalFormat decimalFormat = new DecimalFormat("#");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);

            String state = mState.getState();
            long newConfirmed = Long.parseLong(mState.getDeltaconfirmed());
            long totalConfirmed = Long.parseLong(mState.getConfirmed());
            long totalRecovered = Long.parseLong(mState.getRecovered());
            long totalDeath = Long.parseLong(mState.getDeaths());

            countryName.setText(state.toUpperCase());
            newCasesCount.setText(decimalFormat.format(newConfirmed));
            confirmedCount.setText(decimalFormat.format(totalConfirmed));
            recoveredCount.setText(decimalFormat.format(totalRecovered));
            deathCount.setText(decimalFormat.format(totalDeath));
        }

    }

    public class EmptyViewHolder extends BaseViewHolder {
        /*@BindView(R.id.tv_message)
        TextView messageTextView;
        @BindView(R.id.buttonRetry)
        TextView buttonRetry;*/
        EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            /* buttonRetry.setOnClickListener(v -> mCallback.onEmptyViewRetryClick());*/
        }
        @Override
        protected void clear() {
        }
    }
}
