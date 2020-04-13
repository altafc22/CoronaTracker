package gq.altafchaudhari.cowatch;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import gq.altafchaudhari.cowatch.activities.SearchStateActivity;
import gq.altafchaudhari.cowatch.adapters.DistrictWiseAdapter;
import gq.altafchaudhari.cowatch.database.dboperation.DistrictOperations;
import gq.altafchaudhari.cowatch.database.dboperation.GetDistrictListener;
import gq.altafchaudhari.cowatch.model.districts.District;
import gq.altafchaudhari.cowatch.model.districts.DistrictStats;
import gq.altafchaudhari.cowatch.network.Client;
import gq.altafchaudhari.cowatch.network.Service;
import retrofit2.Call;
import retrofit2.Callback;


public class DistrictFragment extends Fragment implements GetDistrictListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private  static String TAG = IndiaFragment.class.getName();
    private RecyclerView mRecyclerView;
    private DistrictWiseAdapter stateWiseAdapter;
    List<District> districtList;
    private View rootView;

    ImageView iv_search,iv_graph;
    SharedPreferenceManager spManager ;

    private TextView confirmedCount,recoveredCount,deathCount,tv_date;
    private ImageView yellowBar,redBar,greenBar;

    public DistrictFragment() {
        // Required empty public constructor
    }

    public static DistrictFragment newInstance(String param1, String param2) {
        DistrictFragment fragment = new DistrictFragment();
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
    }

    private void setupData()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.scrollToPosition(0);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        districtList = new ArrayList<>();
        Log.i(TAG,"TRYING TO FETCH NEW DATA");
        //if(Utils.isInternetAvailable())
        fetchNewData();
        new Handler().postDelayed(() -> DistrictOperations.getDistricts(getContext(), DistrictFragment.this::onDistrictsFound),0);
    }

    private void fetchNewData(){
        try{
            Service apiService = Client.getIndianRetrofitInstance().create(Service.class);
            Call<List<DistrictStats>> newStatCall = apiService.getDistrictWiseStats();
            Log.i(TAG,"........."+newStatCall.toString());
            newStatCall.enqueue(new Callback<List<DistrictStats>>() {
                @Override
                public void onResponse(@NotNull Call<List<DistrictStats>> call, @NotNull retrofit2.Response<List<DistrictStats>> response) {
                    try{
                        List<DistrictStats> districtStatsList = response.body();

                        Log.i(TAG,"STATE: "+districtStatsList.size());

                       /* for (DistrictStats districtStats: districtStatsList)
                        {
                            Log.i(TAG,"STATE: "+districtStats.getState());
                            *//*List<DistrictV2> districtV2sList = districtStats.getDistrictData();
                            for (DistrictV2 districtV2: districtV2sList)
                            {
                                Log.i(TAG,"CITY: "+districtV2.getDistrict());
                            }*//*
                        }*/

                        /*List<DistrictV2> districtv2s =  districtStats.getDistrictData();
                        *//*  try {
                            Date d = new Date();
                            SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
                            Statewise india = new Statewise();

                            for(Statewise s: states)
                            {
                                if(s.getState().equals("Total"))
                                {
                                    states.remove(s);
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
                        }*//*
                        @SuppressLint("StaticFieldLeak")
                        class SaveTask extends AsyncTask<Void, Void, Void> {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                for (int i= 0;i<districtv2s.size();i++) {
                                    District district = new District();
                                    district.setConfirmed(districtv2s.get(i).getConfirmed());
                                    district.setDistrict(districtv2s.get(i).getDistrict()+);
                                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                                            .districtDao()
                                            .insert(states.get(i));
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                Log.i(TAG,"All State Saved");
                                new Handler().postDelayed(() ->  StateOperations.getStates(getContext(), IndiaFragment.this::onStatesFound),0);
                            }
                        }
                        SaveTask st = new SaveTask();
                        st.execute();*/
                    }catch (NullPointerException e)
                    {
                        Log.i(TAG,e.toString());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<DistrictStats>> call, Throwable t) {
                    Log.i("Error",t.getMessage());
                }
            });

        }catch (Exception e)
        {
            Log.i("Error",e.getMessage());
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDistrictsFound(List<District> districts) {
        if(districts!=null && districts.size()>0) {
            districtList.clear();
            districtList = districts;

            DecimalFormat df = new DecimalFormat("#.#");
            df.setGroupingUsed(true);
            df.setGroupingSize(3);

            /*            District india = new Statewise();
                        for(Statewise s: districtList)
                        {
                            if(s.getState().equals("Total"))
                            {
                                districtList.remove(s);
                                india = s;
                                break;
                            }
                        }*/

            /*spManager.setValue("ind_confirmed",india.getConfirmed());
            spManager.setValue("ind_recovered",india.getRecovered());
            spManager.setValue("ind_dead",india.getDeaths());
            d = dateFormat.parse(india.getLastupdatedtime());
            spManager.setValue("ind_date",d.toString());*/

            /*String conf = df.format(Long.parseLong(india.getConfirmed()));
            String recov = df.format(Long.parseLong(india.getRecovered()));
            String ded =df.format(Long.parseLong(india.getDeaths()));
            Date date = new Date();*/

           /* @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
            tv_date.setText(dateFormat.format(date));*/

            stateWiseAdapter = new DistrictWiseAdapter(getContext(), districts);
            mRecyclerView.setAdapter(stateWiseAdapter);
            //sortStateData();
            stateWiseAdapter.notifyDataSetChanged();
        }
        else
        {

        }
    }
}
