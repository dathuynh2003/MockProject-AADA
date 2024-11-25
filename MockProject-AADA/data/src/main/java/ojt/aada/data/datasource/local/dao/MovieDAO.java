package ojt.aada.data.datasource.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ojt.aada.data.datasource.local.entities.MovieEntity;

@Dao
public interface MovieDAO {
    @Insert
    void insert(MovieEntity movieEntity);

    @Query("SELECT * FROM movie_table")
    Flowable<List<MovieEntity>> getAllMovies();

    @Query("DELETE FROM movie_table WHERE id = :id")
    void delete(int id);
}
