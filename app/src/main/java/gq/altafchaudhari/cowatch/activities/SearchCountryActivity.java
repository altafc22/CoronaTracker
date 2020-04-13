package gq.altafchaudhari.cowatch.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.joaquimley.faboptions.FabOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gq.altafchaudhari.cowatch.R;
import gq.altafchaudhari.cowatch.adapters.searchadapters.SearchCountryAdapter;
import gq.altafchaudhari.cowatch.database.dboperation.CountryOperations;
import gq.altafchaudhari.cowatch.database.dboperation.GetCountryListener;
import gq.altafchaudhari.cowatch.model.world.Country;
import gq.altafchaudhari.cowatch.utilities.Utils;

public class SearchCountryActivity extends AppCompatActivity implements GetCountryListener {

    private static String TAG = SearchCountryActivity.class.getName();
    @BindView(R.id.search_view)
    SearchView search_view;

    @BindView(R.id.iv_btn_back)
    ImageView iv_btn_back;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fab_options)
    FabOptions fab_options;

    private List<Country> countryList;
    private SearchCountryAdapter mAdapter;

    private static int LAST_SORT;
    private static  final int MOST_CONF = 1;
    private static final int MOST_DEATH = 2;
    private static final int A_TO_Z = 3;
    private static final int Z_TO_A = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        iv_btn_back.setOnClickListener(v->{
            finish();
        });

        countryList = new ArrayList<>();
        /*mAdapter = new SearchItemAdapter(this, countryList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);*/
        fetchData();

        fab_options.setOnClickListener(v -> {
           switch (v.getId())
           {
               case R.id.sort_confirmed:
                   //oast.makeText(getBaseContext(), "Confirmed", Toast.LENGTH_SHORT).show();
                   sortMostConfirmedData();
                   break;

               case R.id.sort_deaths:
                   //mFabOptions.setButtonColor(R.id.faboptions_textsms, R.color.colorAccent);
                   //Toast.makeText(getBaseContext(), "Death", Toast.LENGTH_SHORT).show();
                   sortMostDeathData();
                   break;


               case R.id.sort_az:
                   //mFabOptions.setButtonColor(R.id.faboptions_download, R.color.colorAccent);
                   //Toast.makeText(getBaseContext(), "A To Z", Toast.LENGTH_SHORT).show();
                   sortAtoZData();
                   break;


               case R.id.sort_za:
                   //mFabOptions.setButtonColor(R.id.faboptions_share, R.color.colorAccent);
                    //Toast.makeText(getBaseContext(), "Z To A", Toast.LENGTH_SHORT).show();
                   sortZtoAData();
                   break;

               default:
           }
        });

        final View activityRootView = findViewById(R.id.search_activity_layout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
            if (heightDiff > Utils.dpToPx(200)) { // if more than 200 dp, it's probably a keyboard...
                // ... do something here
                fab_options.setVisibility(View.GONE);
            }
            else
            {
                fab_options.setVisibility(View.VISIBLE);
            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "Query: " + newText);
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        search_view.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, "hasFocus: " + hasFocus);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private void enableSearchView(){
        // Associate searchable configuration with the SearchView
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        super.onBackPressed();
        if (!search_view.isIconified()) {
            search_view.setIconified(true);
            return;
        }
    }

    private void fetchData() {
        new Handler().postDelayed(() -> CountryOperations.getCountries(this, this),0);
    }

    @Override
    public void onCountriesFound(List<Country> countries) {
        if(countryList!=null) {

            countryList.clear();
            countryList.addAll(countries);
            for (Country country : countries)
            {
                if(country.getCountry().equals("Total"))
                {
                    countries.remove(country);
                    break;
                }
            }

            mAdapter = new SearchCountryAdapter(this, countryList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            sortMostConfirmedData();

            Log.i(TAG,countryList.size()+"========="+countries.size());
             // refreshing recycler view
            //mAdapter.notifyDataSetChanged();

            Log.i(TAG,mAdapter.getItemCount()+" ADAPTER COUNT");
            enableSearchView();
        }
    }

    private void sortAtoZData()
    {
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                    int result = o1.getCountry().compareTo(o2.getCountry());
                        mAdapter.notifyDataSetChanged();
                       return result;
            }
        });
        LAST_SORT = A_TO_Z;
        mAdapter.notifyDataSetChanged();
    }
    private void sortZtoAData()
    {
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                    int result = o1.getCountry().compareTo(o2.getCountry());
                    return result;
            }
        });
        Collections.reverse(countryList);
        mAdapter.notifyDataSetChanged();
        LAST_SORT = Z_TO_A;
    }

    private void sortMostConfirmedData()
    {
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                int result = o1.getTotalConfirmed().compareTo(o2.getTotalConfirmed());
                //mCountriesAdapter.notifyDataSetChanged();
                return result;
            }
        });
        Collections.reverse(countryList);
        mAdapter.notifyDataSetChanged();
        LAST_SORT = MOST_CONF;
    }

    private void sortMostDeathData()
    {
        Collections.sort(countryList, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                int result = o1.getTotalDeaths().compareTo(o2.getTotalDeaths());
                //mCountriesAdapter.notifyDataSetChanged();
                return result;
            }
        });
        Collections.reverse(countryList);
        mAdapter.notifyDataSetChanged();
        LAST_SORT = MOST_DEATH;
    }
}
