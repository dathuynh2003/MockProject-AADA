package ojt.aada.data.datasource.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import ojt.aada.data.datasource.local.entities.ReminderEntity;

@Dao
public interface ReminderDAO {
    @Insert
    void insert(ReminderEntity reminderEntity);
    @Query("DELETE FROM reminder_table WHERE id = :reminderId")
    void delete(long reminderId);
    @Update
    void update(ReminderEntity reminderEntity);
    @Query("SELECT * FROM reminder_table")
    Flowable<List<ReminderEntity>> getAllReminder();
}
