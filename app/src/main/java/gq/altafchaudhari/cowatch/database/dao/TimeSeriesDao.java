package gq.altafchaudhari.cowatch.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import gq.altafchaudhari.cowatch.model.states.CasesTimeSeries;

@Dao
public interface TimeSeriesDao {
        @Query("SELECT * FROM time_series")
        List<CasesTimeSeries> getAll();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(CasesTimeSeries timeSeries);

        @Delete
        void delete(CasesTimeSeries timeSeries);

        @Update
        void update(CasesTimeSeries timeSeries);
}
