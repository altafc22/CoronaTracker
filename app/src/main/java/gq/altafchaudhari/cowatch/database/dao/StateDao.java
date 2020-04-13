package gq.altafchaudhari.cowatch.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import gq.altafchaudhari.cowatch.model.states.Statewise;

@Dao
public interface StateDao {
        @Query("SELECT * FROM state_table")
        List<Statewise> getAll();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Statewise statewise);

        @Delete
        void delete(Statewise state);

        @Update
        void update(Statewise state);
}
