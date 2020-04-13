package gq.altafchaudhari.cowatch.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import gq.altafchaudhari.cowatch.model.districts.District;

@Dao
public interface DistrictDao {
        @Query("SELECT * FROM district_table")
        List<District> getAll();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(District district);

        @Delete
        void delete(District district);

        @Update
        void update(District district);
}
