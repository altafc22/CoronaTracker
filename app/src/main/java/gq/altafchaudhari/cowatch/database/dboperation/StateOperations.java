package gq.altafchaudhari.cowatch.database.dboperation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import gq.altafchaudhari.cowatch.MyApplication;
import gq.altafchaudhari.cowatch.database.DbClient.DatabaseClient;
import gq.altafchaudhari.cowatch.model.states.Statewise;

public class StateOperations {
    private static String TAG = StateOperations.class.getName();

    //save
     public static void saveState(Context context, Statewise state)
    {
        try
        {
            class SaveTask extends AsyncTask<Void, Void, Void> {
                @Override
                protected Void doInBackground(Void... voids) {

                    //creating a user
                    //adding to database
                    DatabaseClient.getInstance(context).getAppDatabase()
                            .stateDao()
                            .insert(state);
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
        catch (Exception  ex)
        {

        }
    }

    //get
    public static void getStates(Context context, GetStateListener listener) {

            class GetTask extends AsyncTask<Void, Void, List<Statewise>> {
                @Override
                protected List<Statewise> doInBackground(Void... voids) {
                    try {
                        List<Statewise> taskList = DatabaseClient
                                .getInstance(MyApplication.getInstance())
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
                protected void onPostExecute(List<Statewise> stateList) {
                    super.onPostExecute(stateList);
                    listener.onStatesFound(stateList);
                }
            }

            GetTask gt = new GetTask();
            gt.execute();


    }

    //update
    public static void updateState(Context context,final Statewise sate) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .stateDao()
                        .update(sate);
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
    public static void deleteState(Context context,final Statewise state) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(MyApplication.getInstance()).getAppDatabase()
                        .stateDao()
                        .delete(state);
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
