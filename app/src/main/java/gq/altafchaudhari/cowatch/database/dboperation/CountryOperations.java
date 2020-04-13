package gq.altafchaudhari.cowatch.database.dboperation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import gq.altafchaudhari.cowatch.MyApplication;
import gq.altafchaudhari.cowatch.model.world.Country;
import gq.altafchaudhari.cowatch.database.DbClient.DatabaseClient;

public class CountryOperations {
    private static String TAG = CountryOperations.class.getName();

    //save Country
     public static void saveCountry(Context context, Country country)
    {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                //creating a user

                //adding to database
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .countryDao()
                        .insert(country);
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

    //get Country
    public static void getCountries(Context context, GetCountryListener listener) {

            class GetTask extends AsyncTask<Void, Void, List<Country>> {
                @Override
                protected List<Country> doInBackground(Void... voids) {
                    try {
                        List<Country> taskList = DatabaseClient
                                .getInstance(MyApplication.getInstance())
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
                    listener.onCountriesFound(countryList);
                }
            }

            GetTask gt = new GetTask();
            gt.execute();


    }

    //update
    public static void updateCountry(Context context,final Country country) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .countryDao()
                        .update(country);
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
    public static void deleteCountry(Context context,final Country country) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .countryDao()
                        .delete(country);
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
