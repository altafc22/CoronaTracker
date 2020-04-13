package gq.altafchaudhari.cowatch.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import gq.altafchaudhari.cowatch.model.world.Country;

@Dao
public interface CountryDao {
        @Query("SELECT * FROM country_table")
        List<Country> getAll();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Country user);

        @Delete
        void delete(Country user);

        @Update
        void update(Country user);
}
