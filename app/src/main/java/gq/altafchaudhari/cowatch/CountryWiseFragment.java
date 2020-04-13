package gq.altafchaudhari.cowatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.broooapps.lineargraphview2.LinearGraphView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import gq.altafchaudhari.cowatch.activities.SearchCountryActivity;
import gq.altafchaudhari.cowatch.adapters.SliderAdapter;
import gq.altafchaudhari.cowatch.database.DbClient.DatabaseClient;
import gq.altafchaudhari.cowatch.database.dboperation.CountryOperations;
import gq.altafchaudhari.cowatch.database.dboperation.GetCountryListener;
import gq.altafchaudhari.cowatch.model.world.Global;
import gq.altafchaudhari.cowatch.model.world.GlobalData;
import gq.altafchaudhari.cowatch.network.Client;
import gq.altafchaudhari.cowatch.network.Service;
import gq.altafchaudhari.cowatch.model.world.Country;
import gq.altafchaudhari.cowatch.adapters.CountryWiseAdapter;
import gq.altafchaudhari.cowatch.utilities.Utils;
import kotlin.Triple;
import retrofit2.Call;
import retrofit2.Callback;

public class CountryWiseFragment extends Fragment implements CountryWiseAdapter.Callback, GetCountryListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  static String TAG = CountryWiseFragment.class.getName();



    private RecyclerView mRecyclerView;
    private CountryWiseAdapter mCountriesAdapter;
    List<Country> countryList;
    private View rootView;
    LinearGraphView linear_graph_view;

    ImageView iv_search;
    SharedPreferenceManager spManager ;

    private TextView confirmedCount,recoveredCount,deathCount,tv_date;
    private ImageView yellowBar,redBar,greenBar;
    Global globalStat;

    AdView mAdView;

    private String mParam1;
    private String mParam2;

    public CountryWiseFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CountryWiseFragment newInstance() {
        CountryWiseFragment fragment = new CountryWiseFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initViews()
    {

        spManager = new SharedPreferenceManager(getActivity());
        mRecyclerView =  rootView.findViewById(R.id.countryWiseRecyclerView);
        confirmedCount= rootView.findViewById(R.id.confirmedCount);
        recoveredCount= rootView.findViewById(R.id.recoveredCount);
        deathCount= rootView.findViewById(R.id.deathCount);
        tv_date= rootView.findViewById(R.id.tv_date);

        yellowBar = rootView.findViewById(R.id.yellowBar);
        redBar = rootView.findViewById(R.id.redBar);
        greenBar = rootView.findViewById(R.id.greenBar);
        linear_graph_view = rootView.findViewById(R.id.linear_graph_view);
        iv_search = rootView.findViewById(R.id.iv_search);

        iv_search.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), SearchCountryActivity.class);
            startActivity(intent);
        });
        loadCaptions();
        loadAds();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_world, container, false);
        initViews();
        if(Utils.isInternetAvailable(getActivity()))
        {
            Log.i(TAG,"Internet available");
            fetchGlobalData();
        }
        else
        {
            //Toast.makeText(getActivity(),"Please connect internet to get updated stats.",Toast.LENGTH_SHORT).show();
        }

        setupData();
        // Inflate the layout for this fragment
        return rootView;
    }
    Timer timer;
    List<String> captionList;
    ViewPager viewPager;
    private class SliderTimer extends TimerTask {

        private Handler handler = new Handler();
        private Runnable runnable;
        private void reverseSlider(){
            /*if (viewPager.getCurrentItem() > 0){
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }else{*/
            viewPager.setCurrentItem(0);
            /*}*/
        }
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < captionList.size() - 1) {

                        handler.removeCallbacks(runnable);
                        handler.removeMessages(0);
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {

                        handler.postDelayed( runnable = new Runnable() {
                            public void run() {
                                reverseSlider();
                                handler.postDelayed(runnable, 500);
                            }
                        }, 500);

                    }

                }

            });
        }
    }
    private void loadCaptions()
    {
        viewPager =  rootView.findViewById(R.id.textSliderViewPager);
        //indicator = findViewById(R.id.indicator);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    Log.d("TouchTest", "Touch down");
                    timer.cancel();
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    Log.d("TouchTest", "Touch up");
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new SliderTimer(),4000,4000);
                }
                return false;
            }
        });

        captionList = new ArrayList<>();
        captionList.add(String.valueOf(Html.fromHtml("Don't hoard groceries and essentials. Please ensure that people who are in need don't face a shortage because of you!")));
        captionList.add(String.valueOf(Html.fromHtml("Be compassionate! Help those in need like the elderly and poor. They are facing a crisis which we can't even imagine!")));
        captionList.add(String.valueOf(Html.fromHtml("Be considerate. While buying essentials remember that you need to share with 130 crore fellow citizens!")));
        captionList.add(String.valueOf(Html.fromHtml("Going out to buy essentials? Social Distancing is KEY! Maintain at least 2 metres distance between each other in the line.")));
        captionList.add(String.valueOf(Html.fromHtml("Plan ahead! Take a minute and check how much supplies you have at home. Planning lets you buy exactly what you need.")));
        captionList.add(String.valueOf(Html.fromHtml("Plan and calculate your essential needs for the next three weeks")));
        captionList.add(String.valueOf(Html.fromHtml("Help out the elderly by bringing them their groceries and other essentials.")));
        captionList.add(String.valueOf(Html.fromHtml("Help out your employees and domestic workers by not cutting their salaries. Show the true Indian spirit!")));
        captionList.add(String.valueOf(Html.fromHtml("Lockdown means LOCKDOWN! Avoid going out unless absolutely necessary. Stay safe!")));
        captionList.add(String.valueOf(Html.fromHtml("Panic mode : OFF! ❌\nESSENTIALS ARE ON! ✔️")));
        captionList.add(String.valueOf(Html.fromHtml("Your essential needs will be taken care of by the government in a timely manner. Please do not hoard.")));
        captionList.add(String.valueOf(Html.fromHtml("Be a true Indian. Show compassion, Be considerate,Help those in need. We will get through this!")));
        captionList.add(String.valueOf(Html.fromHtml("If you have symptoms and suspect you have coronavirus - reach out to your doctor or call state helplines. 📞 Get help.")));
        captionList.add(String.valueOf(Html.fromHtml("Stand against FAKE news and illegit WhatsApp forwards! Do NOT ❌ forward a message until you verify the content it contains.")));
        captionList.add(String.valueOf(Html.fromHtml("If you have any medical queries, reach out to your state helpline, district administration or trusted doctors!")));
        captionList.add(String.valueOf(Html.fromHtml("Wash your hands with soap and water often, especially after a grociery run. Keep the virus at bay.")));
        captionList.add(String.valueOf(Html.fromHtml("There is no evidence that hot weather will stop the virus! You can! Stay home, stay safe.")));
        captionList.add(String.valueOf(Html.fromHtml("Help the medical fraternity by staying at home!")));
        captionList.add(String.valueOf(Html.fromHtml("Avoid going out during the lockdown. Help break the chain of spread.")));
        captionList.add(String.valueOf(Html.fromHtml("Call up your loved ones during the lockdown, support each other through these times.")));
        captionList.add(String.valueOf(Html.fromHtml("The virus does not discriminate. Why do you? DO NOT DISCRIMINATE. We are all Indians!")));
        captionList.add(String.valueOf(Html.fromHtml("Our brothers from the North-East are just as Indian as you! Help everyone during this crisis ❤️")));
        captionList.add(String.valueOf(Html.fromHtml("Get in touch with your local NGO's and district administration to volunteer for this cause.")));
        captionList.add(String.valueOf(Html.fromHtml("This will pass too. Enjoy your time at home and spend quality time with your family! Things will be normal soon.")));
        captionList.add(String.valueOf(Html.fromHtml("#BreakTheChain of unverified WhatsApp forwards which spread wrong information! Do not forward unless you verify it.")));


        viewPager.setAdapter(new SliderAdapter(getActivity(), captionList));
        //indicator.setupWithViewPager(viewPager, true);

        timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 4000);
    }

    private void loadAds()
    {
        mAdView = rootView.findViewById(R.id.adView_banner);
        AdRequest adRequest = new AdRequest.Builder().build(); //for live data
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build(); // for testing purpose "57D25B5906BE98D4AFBB2BD20CE94754"

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                //Toast.makeText(getActivity(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                Log.i(TAG,"Ad Closed");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Toast.makeText(getActivity(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                Log.i(TAG,"Ad Failed! error code:"+errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                //Toast.makeText(getActivity(), "Ad left application!", Toast.LENGTH_SHORT).show();
                Log.i(TAG,"Add left application");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);
    }


    public void setupData()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        countryList = new ArrayList<>();

        Log.i(TAG,"TRYING TO FETCH NEW DATA");
        /*if(Utils.isInternetAvailable())
        fetchGlobalData();*/
        new Handler().postDelayed(() -> CountryOperations.getCountries(getContext(), CountryWiseFragment.this::onCountriesFound),0);
    }

    public void fetchGlobalData(){
        try{
            Service apiService = Client.getRetrofitInstance().create(Service.class);
            Call<GlobalData> newStatCall = apiService.getCountryWiseStats();

            newStatCall.enqueue(new Callback<GlobalData>() {
                @Override
                public void onResponse(@NotNull Call<GlobalData> call, @NotNull retrofit2.Response<GlobalData> response) {
                    try{

                        GlobalData globalData = response.body();
                        Global globalStat = globalData.getGlobal();
                        List<Country> countries =  globalData.getCountries();
                        SharedPreferenceManager spManager = new SharedPreferenceManager(getActivity());
                        spManager.setValue("confirmed",globalStat.getTotalConfirmed());
                        spManager.setValue("recovered",globalStat.getTotalRecovered());
                        spManager.setValue("dead",globalStat.getTotalDeaths());

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date date = dateFormat.parse(globalData.getDate());
                            String d =formatter.format(date);
                            //System.out.println("Formatted: "+formatter.format(d));
                            spManager.setValue("date",d);
                        }
                        catch(Exception e) {
                            Log.i(TAG,"Exception: "+e);
                        }

                        for (int i= 0;i<countries.size();i++) {
                            Log.i(TAG, "XXXXX "+countries.get(i).getCountry());
                            /*DatabaseClient.getInstance(getActivity()).getAppDatabase()
                                    .countryDao()
                                    .insert(countries.get(i));*/
                            CountryOperations.saveCountry(getActivity(),countries.get(i));
                        }

                        GetTask gt = new GetTask();
                        gt.execute();

                       /* @SuppressLint("StaticFieldLeak")
                        class SaveTask extends AsyncTask<Void, Void, Void> {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                for (int i= 0;i<countries.size();i++) {
                                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                                            .countryDao()
                                            .insert(countries.get(i));
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                Log.i(TAG,"All Countries Saved");
                                new Handler().postDelayed(() -> CountryOperations.getCountries(getContext(), CountryWiseFragment.this::onCountriesFound),0);
                            }
                        }
                        SaveTask st = new SaveTask();
                        st.execute();*/


                    }catch (NullPointerException e)
                    {
                        Log.d("Error While Fetch ", e.toString());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<GlobalData> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                }
            });

        }catch (Exception e)
        {
            Log.d("Error",e.getMessage());
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onEmptyViewRetryClick() {

    }
    /*
    @Override
    public void onPinClick(BaseCountry response, Boolean isPinned) {
        FavCountry fav;
        if (isPinned) {
            if (response instanceof Country){
                fav  = Utils.toFavorite((Country) response);
                if(!isCountryAvailable((Country)response))
                    FavCountryOperations.saveCountry(getActivity(),fav);
            }
            else if (response instanceof FavCountry)
            {
                if(!isCountryAvailable((Country) response))
                    FavCountryOperations.saveCountry(getActivity(),(FavCountry) response);
            }

        } else {
            if (response instanceof Country)
                if(isCountryAvailable((Country) response))
                    FavCountryOperations.deleteCountry(getActivity(),(FavCountry) response);
            else if (response instanceof FavCountry)
                if(isCountryAvailable((Country) response))
                    FavCountryOperations.deleteCountry(getActivity(),(FavCountry) response);

        }
        loadCountriesData();
    }
*/

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        if(timer!=null){
            timer.cancel();
        }
        super.onPause();
        //particleView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //particleView.resume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void  setupWorldStats(Global worldStat)
    {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);

        String conf = df.format(worldStat.getTotalConfirmed());
        String recov = df.format(worldStat.getTotalRecovered());
        String ded =df.format(worldStat.getTotalDeaths());


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        String date = spManager.getValue("date",formatter.format(d));

        confirmedCount.setText(conf);
        recoveredCount.setText(recov);
        deathCount.setText(ded);
        deathCount.setText(ded);
        tv_date.setText(date);

        float confirmed = worldStat.getTotalConfirmed()/100;
        float recovered = worldStat.getTotalRecovered()/100;
        float dead = worldStat.getTotalDeaths()/100;
        int total = (int)(confirmed+recovered+dead);

       /* List<DataModel> dataList = new ArrayList<>();
        dataList.add(new DataModel("Confirmed", getActivity().getColor(R.color.yellow), (int)confirmed));
        dataList.add(new DataModel("Two", getActivity().getColor(R.color.green), (int)recovered));
        dataList.add(new DataModel("Three", getActivity().getColor(R.color.red), (int)dead));
        linear_graph_view.setData(dataList, total);*/

        //Glide.with(getContext()).load(R.drawable.yellow_bar).centerCrop().placeholder(R.drawable.yellow_bar).into(yellowBar);
       /*try {
           Glide.with(Objects.requireNonNull(getActivity())).load(R.drawable.yellow_bar).centerCrop().into(yellowBar);
           Glide.with(getActivity()).load(R.drawable.green_bar).centerCrop().into(greenBar);
           Glide.with(getActivity()).load(R.drawable.red_bar).centerCrop().into(redBar);
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }*/

        Triple weight = Utils.provideBarWeights(worldStat);
        LinearLayout.LayoutParams paramOne = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float)weight.getFirst());
        LinearLayout.LayoutParams paramTwo  = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float)weight.getSecond());
        LinearLayout.LayoutParams paramThree = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float)weight.getThird());
        paramOne.rightMargin = Utils.dpToPx(5);
        paramThree.leftMargin = Utils.dpToPx(5);
        yellowBar.setLayoutParams(paramOne);
        greenBar.setLayoutParams(paramTwo);
        redBar.setLayoutParams(paramThree);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCountriesFound(List<Country> countries) {
        if(countries!=null && countries.size()>0) {
            countryList.clear();
            countryList = countries;

            mCountriesAdapter = new CountryWiseAdapter(getContext(), countries);
            mRecyclerView.setAdapter(mCountriesAdapter);
            sortCountryData();
            mCountriesAdapter.notifyDataSetChanged();
            long con =0 ,rec = 0,ded = 0;


            con = spManager.getLongValue("confirmed",0000);
            rec = spManager.getLongValue("recovered",0000);
            ded = spManager.getLongValue("dead",0000);
            Global global = new Global(con,rec,ded);
            setupWorldStats(global);
        }
        else
        {

        }

    }

    private void sortCountryData()
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
    }

    @Override
    public void onRefresh() {
        if(Utils.isInternetAvailable(getActivity()))
        {
            fetchGlobalData();
        }
        else
        {

            Log.i(TAG,"No Internet Available");
        }
    }


    class GetTask extends AsyncTask<Void, Void, List<Country>> {
        @Override
        protected List<Country> doInBackground(Void... voids) {
            try {
                List<Country> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .countryDao()
                        .getAll();
                return taskList;
            }
            catch (Exception e)
            {
                Log.i(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Country> countryList) {
            super.onPostExecute(countryList);
            //listener.onSeriesFound(timeSeriesList);
            new Handler().postDelayed(() -> CountryOperations.getCountries(getContext(), CountryWiseFragment.this::onCountriesFound),0);

        }
    }
}
