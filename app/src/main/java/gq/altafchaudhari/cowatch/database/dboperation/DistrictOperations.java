package gq.altafchaudhari.cowatch.database.dboperation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import gq.altafchaudhari.cowatch.MyApplication;
import gq.altafchaudhari.cowatch.database.DbClient.DatabaseClient;
import gq.altafchaudhari.cowatch.model.districts.District;

public class DistrictOperations {
    private static String TAG = DistrictOperations.class.getName();

    //save
     public  static void saveDistrict(Context context, District district)
    {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                //creating a user

                //adding to database
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .districtDao()
                        .insert(district);
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
    public static void getDistricts(Context context, GetDistrictListener listener) {

            class GetTask extends AsyncTask<Void, Void, List<District>> {
                @Override
                protected List<District> doInBackground(Void... voids) {
                    try {
                        List<District> taskList = DatabaseClient
                                .getInstance(MyApplication.getInstance())
                                .getAppDatabase()
                                .districtDao()
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
                protected void onPostExecute(List<District> districtList) {
                    super.onPostExecute(districtList);
                    listener.onDistrictsFound(districtList);
                }
            }

            GetTask gt = new GetTask();
            gt.execute();


    }

    //update
    public static void updateDistrict(Context context,final District district) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .districtDao()
                        .update(district);
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
    public static void deleteDistrict(Context context,final District district) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .districtDao()
                        .delete(district);
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
