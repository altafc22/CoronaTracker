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
import gq.altafchaudhari.cowatch.model.world.Country;

public class SearchCountryAdapter extends RecyclerView.Adapter<BaseViewHolder> implements Filterable {

    private static final String TAG = "SearchAdapter";

    private List<Country> mCountryList;
    private List<Country> mFilteredList;
    private Context mContext;

    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    //private Callback mCallback;

    public SearchCountryAdapter(Context context, List<Country> list) {
        this.mCountryList = list;
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
                    mFilteredList = mCountryList;
                    Log.i(TAG,"EMPTY");
                } else {
                    ArrayList<Country> filterList = new ArrayList<>();
                    for (Country country : mCountryList) {
                        if (country.getCountry().toLowerCase().contains(charString)){
                            filterList.add(country);
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
                mFilteredList = (ArrayList<Country>) results.values;
                for(Country country: mFilteredList)
                {
                    Log.i(TAG, "results: " + country.getCountry());
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
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
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
        if (mCountryList != null && mCountryList.size() > 0) {
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

            Object country_obj = mFilteredList.get(position);
            final Country mCountry = (Country) country_obj;

            DecimalFormat decimalFormat = new DecimalFormat("#");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);

            String country = mCountry.getCountry();
            long newConfirmed = mCountry.getNewConfirmed();
            long totalConfirmed = mCountry.getTotalConfirmed();
            long totalRecovered = mCountry.getTotalRecovered();
            long totalDeath = mCountry.getTotalDeaths();

            countryName.setText(country.toUpperCase());
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
