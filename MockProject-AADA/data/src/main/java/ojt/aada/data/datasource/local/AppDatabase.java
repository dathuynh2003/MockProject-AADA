package ojt.aada.data.datasource.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ojt.aada.data.datasource.local.dao.MovieDAO;
import ojt.aada.data.datasource.local.dao.ReminderDAO;
import ojt.aada.data.datasource.local.entities.MovieEntity;
import ojt.aada.data.datasource.local.entities.ReminderEntity;

@Database(entities = {MovieEntity.class, ReminderEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract MovieDAO movieDAO();

    public abstract ReminderDAO reminderDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "movie_database")
                    .fallbackToDestructiveMigration() // Allow destructive migrations
                    .build();
        }
        return instance;
    }
}
