package ojt.aada.data.datasource.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ojt.aada.data.datasource.local.dao.MovieDAO;
import ojt.aada.data.datasource.local.entities.MovieEntity;

@Database(entities = {MovieEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract MovieDAO movieDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "movie_database")
                    .build();
        }
        return instance;
    }
}
