package gq.altafchaudhari.cowatch.database.dboperation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

import gq.altafchaudhari.cowatch.MyApplication;
import gq.altafchaudhari.cowatch.database.DbClient.DatabaseClient;
import gq.altafchaudhari.cowatch.model.states.CasesTimeSeries;

public class TimeSeriesOperations {
    private static String TAG = TimeSeriesOperations.class.getName();

    private PieChart daily;

    //save
     public static void  saveTimeSeries(Context context, CasesTimeSeries timeSeries)
    {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                //creating a user

                //adding to database
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .timeSeriesDao()
                        .insert(timeSeries);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //finish();
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    //get
    public static void getTimeSeries(Context context, GetTimeSeriesListener listener) {

            class GetTask extends AsyncTask<Void, Void, List<CasesTimeSeries>> {
                @Override
                protected List<CasesTimeSeries> doInBackground(Void... voids) {
                    try {
                        List<CasesTimeSeries> taskList = DatabaseClient
                                .getInstance(MyApplication.getInstance())
                                .getAppDatabase()
                                .timeSeriesDao()
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
                protected void onPostExecute(List<CasesTimeSeries> timeSeriesList) {
                    super.onPostExecute(timeSeriesList);
                    listener.onSeriesFound(timeSeriesList);
                }
            }

            GetTask gt = new GetTask();
            gt.execute();


    }

    //update
    public static void updateTimeSeries(Context context,final CasesTimeSeries timeSeries) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .timeSeriesDao()
                        .update(timeSeries);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    //delete
    public static void deleteTimeSeries(Context context,final CasesTimeSeries timeSeries) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .timeSeriesDao()
                        .delete(timeSeries);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

}
