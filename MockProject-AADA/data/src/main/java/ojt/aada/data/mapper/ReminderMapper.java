package ojt.aada.data.mapper;

import ojt.aada.data.datasource.local.entities.ReminderEntity;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.models.Reminder;

public class ReminderMapper {
    public static ReminderEntity toEntity(Reminder reminder) {
        return new ReminderEntity(reminder.getId(), reminder.getTime(), reminder.getMovieId(), reminder.getTitle(), reminder.getReleaseDate(), reminder.getPosterPath(), reminder.getRating());
    }

    public static Reminder toDomain(ReminderEntity reminderEntity) {
        return new Reminder(reminderEntity.getTime(), reminderEntity.getMovieId(), reminderEntity.getTitle(), reminderEntity.getReleaseDate(), reminderEntity.getPosterPath(), reminderEntity.getRating());
    }
}
