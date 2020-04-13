package gq.altafchaudhari.cowatch.database.DbClient;

import android.content.Context;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import gq.altafchaudhari.cowatch.database.AppDatabase;

public class DatabaseClient {
 
    private Context mContext;
    private static DatabaseClient mInstance;
    
    //our app database object
    private AppDatabase appDatabase;
 
    private DatabaseClient(Context context) {
        this.mContext = context;
        
        //creating the app database with Room database builder
        //iScanner is the name of the database
        //appDatabase = Room.databaseBuilder(context, AppDatabase.class, "CoronaCounter").build();

        // it will prevent migration error and remove old db  and create new db
        appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "CoronaCounter")
                .fallbackToDestructiveMigration()
                .build();

        // it will migrate db from one version to new version without data lost
        /*appDatabase =  Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "Sample.db")
                .addMigrations(OLD_VER_TO_NEW_VER)
                .build();*/

    }

    static final Migration OLD_VER_TO_NEW_VER = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };



    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }
 
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}