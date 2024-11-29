package ojt.aada.domain.repositories;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ojt.aada.domain.models.Reminder;

public interface LocalReminderRepository {
    Completable addReminder(Reminder reminder);
    Completable removeReminder(Reminder reminder);
    Completable updateReminder(Reminder reminder);
    Flowable<List<Reminder>> getAllReminder();
}
