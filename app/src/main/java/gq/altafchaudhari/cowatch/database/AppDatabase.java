package gq.altafchaudhari.cowatch.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import gq.altafchaudhari.cowatch.database.dao.DistrictDao;
import gq.altafchaudhari.cowatch.database.dao.StateDao;
import gq.altafchaudhari.cowatch.database.dao.TimeSeriesDao;
import gq.altafchaudhari.cowatch.model.districts.District;
import gq.altafchaudhari.cowatch.model.states.CasesTimeSeries;
import gq.altafchaudhari.cowatch.model.states.Statewise;
import gq.altafchaudhari.cowatch.model.world.Country;
import gq.altafchaudhari.cowatch.database.dao.CountryDao;

@Database(entities = {Country.class, Statewise.class, District.class, CasesTimeSeries.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CountryDao countryDao();
    public abstract StateDao stateDao();
    public abstract DistrictDao districtDao();
    public abstract TimeSeriesDao timeSeriesDao();
}
