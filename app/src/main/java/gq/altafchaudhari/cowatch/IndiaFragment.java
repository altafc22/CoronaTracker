package gq.altafchaudhari.cowatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import gq.altafchaudhari.cowatch.activities.SearchStateActivity;
import gq.altafchaudhari.cowatch.adapters.SliderAdapter;
import gq.altafchaudhari.cowatch.adapters.StateWiseAdapter;
import gq.altafchaudhari.cowatch.database.DbClient.DatabaseClient;
import gq.altafchaudhari.cowatch.database.dboperation.GetStateListener;
import gq.altafchaudhari.cowatch.database.dboperation.StateOperations;
import gq.altafchaudhari.cowatch.model.states.IndiaStats;
import gq.altafchaudhari.cowatch.model.states.Statewise;
import gq.altafchaudhari.cowatch.network.Client;
import gq.altafchaudhari.cowatch.network.Service;
import gq.altafchaudhari.cowatch.utilities.Utils;
import retrofit2.Call;
import retrofit2.Callback;

public class IndiaFragment extends Fragment implements GetStateListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private  static String TAG = IndiaFragment.class.getName();
    private RecyclerView mRecyclerView;
    private StateWiseAdapter stateWiseAdapter;
    List<Statewise> statewiseList;
    private View rootView;

    ImageView iv_search,iv_graph;
    SharedPreferenceManager spManager ;

    AdView mAdView;

    private TextView confirmedCount,recoveredCount,deathCount,tv_date;
    private ImageView yellowBar,redBar,greenBar;
    Statewise indiaStat;

    public IndiaFragment() {
        // Required empty public constructor
    }

    public static IndiaFragment newInstance(String param1, String param2) {
        IndiaFragment fragment = new IndiaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_india, container, false);
        initViews();
        fetchIndiaData();
        setupData();
        // Inflate the layout for this fragment
        return rootView;
    }

    private void initViews()
    {
        spManager = new SharedPreferenceManager(getActivity());
        mRecyclerView =  rootView.findViewById(R.id.recyclerView);
        confirmedCount= rootView.findViewById(R.id.confirmedCount);
        recoveredCount= rootView.findViewById(R.id.recoveredCount);
        deathCount= rootView.findViewById(R.id.deathCount);
        tv_date= rootView.findViewById(R.id.tv_date);

        yellowBar = rootView.findViewById(R.id.yellowBar);
        redBar = rootView.findViewById(R.id.redBar);
        greenBar = rootView.findViewById(R.id.greenBar);
        iv_search = rootView.findViewById(R.id.iv_search);
        iv_graph = rootView.findViewById(R.id.iv_graph);

        iv_search.setOnClickListener(v -> {
            //Toast.makeText(getContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getActivity(), SearchStateActivity.class);
            //intent.putExtra("list", (Serializable) countryList);
            startActivity(intent);
        });
        iv_graph.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
            /*Intent intent=new Intent(getActivity(), SearchCountryActivity.class);
            //intent.putExtra("list", (Serializable) countryList);
            startActivity(intent);*/
        });
        loadCaptions();
        loadAds();
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

    private void setupData()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        statewiseList = new ArrayList<>();
        Log.i(TAG,"TRYING TO FETCH NEW DATA");
        //if(Utils.isInternetAvailable())
        if(Utils.isInternetAvailable(getActivity())) {
            Log.i(TAG, "Internet available");
            fetchIndiaData();
        }
        else
        {
            //Toast.makeText(getActivity(),"Please connect internet to get updated stats.",Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(() -> StateOperations.getStates(getContext(), IndiaFragment.this::onStatesFound),0);
    }

    public void fetchIndiaData(){
        try{
            Service apiService = Client.getIndianRetrofitInstance().create(Service.class);
            Call<IndiaStats> newStatCall = apiService.getStateWiseStats();
            newStatCall.enqueue(new Callback<IndiaStats>() {
                @Override
                public void onResponse(@NotNull Call<IndiaStats> call, @NotNull retrofit2.Response<IndiaStats> response) {

                    try{
                        IndiaStats indiaStats = response.body();
                        List<Statewise> statewiseList =  indiaStats.getStatewise();
                        /*List<CasesTimeSeries> casesTimeSeriesList =  indiaStats.getCasesTimeSeries();
                        for(CasesTimeSeries casesTimeSeries: casesTimeSeriesList)
                        {
                            TimeSeriesOperations.saveTimeSeries(getActivity(),casesTimeSeries);
                        }*/

                        for(Statewise state: statewiseList)
                        {
                            StateOperations.saveState(getActivity(),state);
                        }

                        /*  try {
                            Date d = new Date();
                            SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
                            Statewise india = new Statewise();

                            for(Statewise s: statewiseList)
                            {
                                if(s.getState().equals("Total"))
                                {
                                    statewiseList.remove(s);
                                    india = s;
                                    break;
                                }
                            }
                            spManager.setValue("ind_confirmed",india.getConfirmed());
                            spManager.setValue("ind_recovered",india.getRecovered());
                            spManager.setValue("ind_dead",india.getDeaths());
                            d = dateFormat.parse(india.getLastupdatedtime());
                            spManager.setValue("ind_date",d.toString());
                        }
                        catch(Exception e) {
                            System.out.println("Exception: "+e);
                        }*/
                        GetTask getTask = new GetTask();
                        getTask.execute();
                       /* @SuppressLint("StaticFieldLeak")
                        class SaveTask extends AsyncTask<Void, Void, Void> {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                for (int i= 0;i<statewiseList.size();i++) {
                                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                                            .stateDao()
                                            .insert(statewiseList.get(i));
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                Log.i(TAG,"All State Saved");
                                new Handler().postDelayed(() ->  StateOperations.getStates(getActivity(), IndiaFragment.this::onStatesFound),0);
                            }
                        }
                        SaveTask st = new SaveTask();
                        st.execute();*/
                    }catch (NullPointerException e)
                    {
                        Log.d("Error While Fetch",e.toString());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<IndiaStats> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                }
            });

        }catch (Exception e)
        {
            Log.d("Error",e.getMessage());
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatesFound(List<Statewise> states) {
        if(states!=null && states.size()>0) {
            statewiseList.clear();
            statewiseList = states;

            DecimalFormat df = new DecimalFormat("#.#");
            df.setGroupingUsed(true);
            df.setGroupingSize(3);

            Statewise india = new Statewise();
            for(Statewise s: statewiseList)
            {
                if(s.getState().equals("Total"))
                {
                    statewiseList.remove(s);
                    india = s;
                    break;
                }
            }

            /*spManager.setValue("ind_confirmed",india.getConfirmed());
            spManager.setValue("ind_recovered",india.getRecovered());
            spManager.setValue("ind_dead",india.getDeaths());
            d = dateFormat.parse(india.getLastupdatedtime());
            spManager.setValue("ind_date",d.toString());*/

            String conf = df.format(Long.parseLong(india.getConfirmed()));
            String recov = df.format(Long.parseLong(india.getRecovered()));
            String ded =df.format(Long.parseLong(india.getDeaths()));
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try{
                date = dateFormat.parse(india.getLastupdatedtime());
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            confirmedCount.setText(conf);
            recoveredCount.setText(recov);
            deathCount.setText(ded);
            deathCount.setText(ded);
            tv_date.setText(dateFormat.format(date));

            for(Statewise state: states )
            {
                if(state.getState().equals("Total"))
                {
                    states.remove(state);
                    break;
                }
            }

            stateWiseAdapter = new StateWiseAdapter(getContext(), states);
            mRecyclerView.setAdapter(stateWiseAdapter);
            sortStateData();
            stateWiseAdapter.notifyDataSetChanged();
        }
        else
        { }
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
        timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 4000);
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
        //indicator = findViewById(R.id.indicator);
        captionList = new ArrayList<>();
        captionList.add(String.valueOf(Html.fromHtml("❌किराने का सामान और आवश्यक वस्तुओं को जमाखोरी न करें। कृपया सुनिश्चित करें कि जिन लोगों को आपकी ज़रूरत है वे आपकी वजह से कमी का सामना न करें!")));
        captionList.add(String.valueOf(Html.fromHtml("👴बुजुर्गों और गरीबों की मदद करें। वे एक ऐसे संकट का सामना कर रहे हैं जिसकी हम कल्पना भी नहीं कर सकते हैं!")));
        captionList.add(String.valueOf(Html.fromHtml("विचारशील बनें। आवश्यक वस्तुएं खरीदते समय  याद रखें कि आपको \uD83C\uDDEE\uD83C\uDDF3 130 करोड़ साथी नागरिकों के साथ साझा वस्तुएँ करने की आवश्यकता है!")));
        captionList.add(String.valueOf(Html.fromHtml("आवश्यक वस्तुएं खरीदने के लिए बाहर हो; लाइन में एक दूसरे के बीच कम से कम 2 मीटर की दूरी बनाए रखें, सोशल डिस्टेंस महत्वपूर्ण है।")));
        captionList.add(String.valueOf(Html.fromHtml("आगे की योजना बनाएं! एक मिनट का समय लें और जांचें कि आपके पास घर में कितनी आपूर्ति है। वही चीज़े ख़रीदे जिसकी आपको आवश्यकता है।")));
        captionList.add(String.valueOf(Html.fromHtml("अगले तीन हफ्तों के लिए अपनी आवश्यक जरूरतों की योजना और गणना करें")));
        captionList.add(String.valueOf(Html.fromHtml("अपने किराने का सामान और अन्य आवश्यक सामान लाकर 👴बुजुर्गों की मदद करें।")));
        captionList.add(String.valueOf(Html.fromHtml("अपने कर्मचारियों और घरेलू कर्मचारियों को उनकी तनख्वाह में कटौती न करें। सच्ची भारतीय भावना दिखाएं।")));
        captionList.add(String.valueOf(Html.fromHtml("❌लॉकडाउन का अर्थ है लॉक डाउन! जब तक बिल्कुल जरूरी न हो, 🚶🏻बाहर जाने से बचें। सुरक्षित रहें!")));
        captionList.add(String.valueOf(Html.fromHtml("डरिये नहीं !  🚑आपातकालीन सेवाएं चालू हैं!🚓")));
        captionList.add(String.valueOf(Html.fromHtml("सरकार द्वारा आपकी आवश्यक आवश्यकताओं का समय पर ध्यान रखा जाएगा। कृपया जमाखोरी न करें।")));
        captionList.add(String.valueOf(Html.fromHtml("सच्चे भारतीय बनो। करुणा दिखाओ, विचारशील बनो, जरूरतमंदों की मदद करो। हम इस कठिन समय पर मात करेंगे!")));
        captionList.add(String.valueOf(Html.fromHtml("यदी आप को कोई लक्षण पर संदेह है तो, मदद प्राप्त करने हेतु  तुरंत अपने चिकित्सक से संपर्क करें या राज्य हेल्पलाइन पर कॉल करें।")));
        captionList.add(String.valueOf(Html.fromHtml("झूठी खबरों के झांसे में ना आए! जब तक आप जानकारी को सत्यापित नहीं करते तब तक संदेश कोई भी फॉरवर्ड न करें।")));
        captionList.add(String.valueOf(Html.fromHtml("यदि आपके पास कोई स्वस्थ संबधी समस्या हैं, तो अपने राज्य हेल्पलाइन, जिला प्रशासन या विश्वसनीय 🏥डॉक्टरों तक पहुंचें!")));
        captionList.add(String.valueOf(Html.fromHtml("समय समय पर अपने हाथों को साबुन और 💦पानी से धोएं।")));
        captionList.add(String.valueOf(Html.fromHtml("कोई सबूत नहीं है कि ☀️गर्म मौसम वायरस को रोक देगा!  🏠घर पर रहें, सुरक्षित रहें।")));
        captionList.add(String.valueOf(Html.fromHtml("घर पर रहकर \uD83D\uDC89️ स्वास्थ और \uD83D\uDC6E\uD83C\uDFFC️सुरक्षा कर्मचारियों की मदद करे!")));
        captionList.add(String.valueOf(Html.fromHtml("❌लॉकडाउन के दौरान बाहर जाने से बचें। वायरस फैलने की श्रृंखला को तोड़ने में मदद करें।")));
        captionList.add(String.valueOf(Html.fromHtml("लॉकडाउन के दौरान अपने ❤️प्रियजनों को कॉल करें, इन समयों के माध्यम से एक दूसरे से जुड़े रहें।")));
        captionList.add(String.valueOf(Html.fromHtml("वायरस भेदभाव नहीं करता है। हम भी ऐसा न करे, हम सभी भारतीय हैं! 🇮🇳")));
        captionList.add(String.valueOf(Html.fromHtml("👬 पूर्वोत्तर के हमारे भाई भी हम जैसे ही भारतीय हैं! इस संकट के समय सभी की मदद करें" )));
        captionList.add(String.valueOf(Html.fromHtml("यदि कोई परेशांनी हो तो, अपने स्थानीय एनजीओ और जिला प्रशासन से इस कारण से संपर्क करें।")));
        captionList.add(String.valueOf(Html.fromHtml("यह वक्त भी गुजर जाएगा। घर पर अपने समय का आनंद लें और अपने 👨‍👩‍👧 परिवार के साथ खुशहाल समय बिताएं। जल्द ही हालात सामान्य हो जाएंगे।")));
        captionList.add(String.valueOf(Html.fromHtml("बगैर कोई जानकारी के व्हाट्सप्प फ़ॉर्वर्ड ना करे, ये सायबर अपराध है! जब तक आप इसे सत्यापित नहीं करते हैं, तब तक आगे फॉरवर्ड नहीं करें।")));

        viewPager.setAdapter(new SliderAdapter(getActivity(), captionList));
        //indicator.setupWithViewPager(viewPager, true);

    }

    private void sortStateData()
    {
        Collections.sort(statewiseList, new Comparator<Statewise>() {
            @Override
            public int compare(Statewise o1, Statewise o2) {
                int num1 = Integer.parseInt(o1.getConfirmed());
                int num2 = Integer.parseInt(o2.getConfirmed());
                if(num1==num2)
                    return 0;
                else if(num1>num2)
                    return 1;
                else
                    return -1;
            }
        });
        Collections.reverse(statewiseList);
    }

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


   /* public boolean handleBackPress(){
        if (searchEditText.isVisible) {
            showSearch(false)
            return true;

        }

        return false
    }*/

    class GetTask extends AsyncTask<Void, Void, List<Statewise>> {
        @Override
        protected List<Statewise> doInBackground(Void... voids) {
            try {
                List<Statewise> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .stateDao()
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
        protected void onPostExecute(List<Statewise> statewiseList) {
            super.onPostExecute(statewiseList);
            //listener.onSeriesFound(timeSeriesList);
            new Handler().postDelayed(() -> StateOperations.getStates(getContext(), IndiaFragment.this::onStatesFound),0);

        }
    }
}
