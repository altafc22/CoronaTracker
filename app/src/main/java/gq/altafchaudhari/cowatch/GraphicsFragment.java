package gq.altafchaudhari.cowatch;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import gq.altafchaudhari.cowatch.database.dboperation.GetTimeSeriesListener;
import gq.altafchaudhari.cowatch.database.dboperation.TimeSeriesOperations;
import gq.altafchaudhari.cowatch.model.states.CasesTimeSeries;
import gq.altafchaudhari.cowatch.model.states.IndiaStats;
import gq.altafchaudhari.cowatch.model.states.Statewise;
import gq.altafchaudhari.cowatch.network.Client;
import gq.altafchaudhari.cowatch.network.Service;
import retrofit2.Call;
import retrofit2.Callback;

public class GraphicsFragment extends Fragment implements GetTimeSeriesListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static String TAG = GraphicsFragment.class.getName();

    List<CasesTimeSeries> casesTimeSeriesList;
    View rootView;
    TextView tv_confirmed,tv_death,tv_recovered,tv_total_confirmed,tv_total_recovered,tv_total_death;
    private LineChart confirmed_chart,recovered_chart,death_chart, indian_chart;
    ArrayList<ILineDataSet> indiaDataSets = new ArrayList<>();
    Typeface typeface;
    public GraphicsFragment() {
        // Required empty public constructor
    }

    public static GraphicsFragment newInstance(String param1, String param2) {
        GraphicsFragment fragment = new GraphicsFragment();
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
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_graphics, container, false);
        typeface = ResourcesCompat.getFont(getActivity(), R.font.helvetica);
        initViews();

        casesTimeSeriesList = new ArrayList<>();
        return rootView;
    }

    private void setDefaultChartSettings(LineChart chart)
    {
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.setBackgroundColor(Color.WHITE);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);

        XAxis x = chart.getXAxis();
        x.setEnabled(true);
        x.setTypeface(typeface);
        x.setLabelCount(30,false);
        x.setTextColor(Color.RED);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setAxisLineColor(Color.WHITE);

        YAxis y = chart.getAxisLeft();
        y.setTypeface(typeface);
        y.setLabelCount(6, false);
        y.setTextColor(Color.RED);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.animateXY(2000, 2000);
        // don't forget to refresh the drawing
        chart.invalidate();
    }

    private void moveOffScreen(PieChart chart) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;

        int offset = (int)(height * 0.65); /* percent to move */

        RelativeLayout.LayoutParams rlParams =
                (RelativeLayout.LayoutParams) chart.getLayoutParams();
        rlParams.setMargins(0, -offset, 0, 0);
        chart.setLayoutParams(rlParams);
    }

    private void initViews()
    {
        tv_confirmed = rootView.findViewById(R.id.tv_confirmed);
        tv_death = rootView.findViewById(R.id.tv_death);
        tv_recovered = rootView.findViewById(R.id.tv_recovered);

        tv_total_confirmed = rootView.findViewById(R.id.tv_total_confirmed);
        tv_total_recovered = rootView.findViewById(R.id.tv_total_recovered);
        tv_total_death = rootView.findViewById(R.id.tv_total_death);

        confirmed_chart = rootView.findViewById(R.id.confirmed_chart);
        recovered_chart = rootView.findViewById(R.id.recovered_chart);
        death_chart = rootView.findViewById(R.id.death_chart);
        indian_chart = rootView.findViewById(R.id.indian_chart);

        setDefaultChartSettings(recovered_chart);
        setDefaultChartSettings(confirmed_chart);
        setDefaultChartSettings(death_chart);
        setDefaultChartSettings(indian_chart);
        fetchIndiaData();
        new Handler().postDelayed(() -> TimeSeriesOperations.getTimeSeries(getContext(), GraphicsFragment.this::onSeriesFound),0);
    }

    @Override
    public void onSeriesFound(List<CasesTimeSeries> timeSeries) {
       try {
           casesTimeSeriesList = timeSeries;
           ArrayList<Entry> recovered_values = new ArrayList<>();
           ArrayList<Entry> confirmed_values = new ArrayList<>();
           ArrayList<Entry> death_values = new ArrayList<>();


           List<CasesTimeSeries> latest;
           if(timeSeries.size()>30)
               latest = timeSeries.subList(casesTimeSeriesList.size()-30, casesTimeSeriesList.size());
           else
               latest = casesTimeSeriesList;

           int recov = 0,dead = 0 ,conf = 0;

           for (int i = 0;i<latest.size();i++)
           {
               float confirmed = Float.parseFloat(latest.get(i).getTotalconfirmed());
               float recovered = Float.parseFloat(latest.get(i).getTotalrecovered());
               float death = Float.parseFloat(latest.get(i).getTotaldeceased());

               confirmed_values.add(new Entry(i,confirmed));
               recovered_values.add(new Entry(i,recovered));
               death_values.add(new Entry(i,death));

             /*  recov = recov+Integer.parseInt(latest.get(i).getDailyrecovered());
               conf = conf+Integer.parseInt(latest.get(i).getDailyconfirmed());
               dead = dead+Integer.parseInt(latest.get(i).getDailydeceased());*/

               recov = recov+Integer.parseInt(latest.get(i).getDailyrecovered());
               conf = conf+Integer.parseInt(latest.get(i).getDailyconfirmed());
               dead = dead+Integer.parseInt(latest.get(i).getDailydeceased());
           }
           tv_confirmed.setText(""+(conf-15));
           tv_recovered.setText(""+(recov-17));
           tv_death.setText(""+(dead-11));

           ArrayList<Entry> india_values_one = new ArrayList<>();
           ArrayList<Entry> india_values_two = new ArrayList<>();
           ArrayList<Entry> india_values_three = new ArrayList<>();

           int t_recov = 0,t_dead = 0 ,t_conf = 0;
           for(CasesTimeSeries timeCases: timeSeries )
           {
               t_recov = t_recov+Integer.parseInt(timeCases.getDailyrecovered());
               t_conf = t_conf+Integer.parseInt(timeCases.getDailyconfirmed());
               t_dead = t_dead+Integer.parseInt(timeCases.getDailydeceased());
               india_values_one.add(new Entry(timeSeries.indexOf(timeCases),t_conf));
               india_values_two.add(new Entry(timeSeries.indexOf(timeCases),t_recov));
               india_values_three.add(new Entry(timeSeries.indexOf(timeCases),t_dead));
           }

           tv_total_confirmed.setText(""+(t_conf));
           tv_total_recovered.setText(""+(t_recov));
           tv_total_death.setText(""+(t_dead));

           LineDataSet ld1,ld2,ld3;
           ld1 = new LineDataSet(india_values_one, "Confirmed");
           ld2 = new LineDataSet(india_values_two, "Recovered");
           ld3 = new LineDataSet(india_values_three, "Death");

           setIndiaDataSetStyle(indian_chart,ld1,R.color.colorBlue);
           setIndiaDataSetStyle(indian_chart,ld2,R.color.colorGreen);
           setIndiaDataSetStyle(indian_chart,ld3,R.color.red);
           indiaDataSets.add(ld1);
           indiaDataSets.add(ld2);
           indiaDataSets.add(ld3);
           setIndianData(indian_chart,indiaDataSets);

           setData(confirmed_chart,confirmed_values,R.color.colorBlue,"");
           setData(recovered_chart,recovered_values,R.color.colorGreen,"");
           setData(death_chart,death_values,R.color.colorRed,"");
       }
       catch (Exception e)
       {

       }
    }

    private void fetchIndiaData(){
        try{
            Log.i(TAG,"TRYING FETCH TO CASE SERIES DATA");
            Service apiService = Client.getIndianRetrofitInstance().create(Service.class);
            Call<IndiaStats> newStatCall = apiService.getStateWiseStats();
            newStatCall.enqueue(new Callback<IndiaStats>() {
                @Override
                public void onResponse(@NotNull Call<IndiaStats> call, @NotNull retrofit2.Response<IndiaStats> response) {

                    try{
                        IndiaStats indiaStats = response.body();
                        List<Statewise> states =  indiaStats.getStatewise();
                        List<CasesTimeSeries> casesTimeSeriesList =  indiaStats.getCasesTimeSeries();
                        /*for(Statewise state: states)
                        {
                            StateOperations.saveState(getActivity(),state);
                        }*/
                        for(CasesTimeSeries casesTimeSeries: casesTimeSeriesList)
                        {
                            TimeSeriesOperations.saveTimeSeries(getActivity(),casesTimeSeries);
                        }
                        Log.i(TAG,"Time Series Saved. "+casesTimeSeriesList.size());
                        new Handler().postDelayed(() ->  TimeSeriesOperations.getTimeSeries(getActivity(), GraphicsFragment.this::onSeriesFound),0);

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


    private void setData(LineChart chart, List<Entry> values,int color_resource,String data_set_label) {
        LineDataSet dataSet;
        int color = getResources().getColor(color_resource);
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            dataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            dataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataSet and give it a type
            dataSet = new LineDataSet(values, data_set_label);

            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

            dataSet.setDrawValues(false);
            dataSet.setDrawVerticalHighlightIndicator(true);
            dataSet.setValueTextColor(color);

            dataSet.setCubicIntensity(0.2f);
            dataSet.setDrawFilled(true);
            dataSet.setDrawCircles(true);
            dataSet.setLineWidth(1f);
            dataSet.setCircleRadius(2f);
            dataSet.setCircleColor(color);
            dataSet.setCircleHoleColor(color);
            dataSet.setHighLightColor(Color.rgb(244, 117, 117));
            dataSet.setColor(color);
            dataSet.setFillColor(color);
            dataSet.setFillAlpha(50);
            dataSet.setDrawHorizontalHighlightIndicator(false);
            dataSet.setDrawVerticalHighlightIndicator(false);
            dataSet.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(dataSet);
            data.setValueTypeface(typeface);
            data.setValueTextSize(9f);
            // set data
            chart.setData(data);
        }
    }

    private void setIndianData(LineChart chart,List<ILineDataSet> dataSetList)
    {
        LineData data = new LineData(dataSetList);
        chart.setData(data);
        chart.invalidate();
    }
    private void setIndiaDataSetStyle(LineChart chart,LineDataSet dataSet,int color_resource)
    {
        int color = getResources().getColor(color_resource);
        //dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        dataSet.setDrawValues(false);
        dataSet.setDrawVerticalHighlightIndicator(true);
        dataSet.setValueTextColor(color);

        dataSet.setCubicIntensity(0.2f);
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(true);
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(2f);
        dataSet.setCircleColor(color);
        dataSet.setCircleHoleColor(color);
        dataSet.setHighLightColor(Color.rgb(244, 117, 117));
        dataSet.setColor(color);
        dataSet.setFillColor(color);
        dataSet.setFillAlpha(0);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawVerticalHighlightIndicator(false);
        dataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // create a data object with the data sets
        LineData data = new LineData(dataSet);
        data.setValueTypeface(typeface);
        data.setValueTextSize(9f);
    }

}

