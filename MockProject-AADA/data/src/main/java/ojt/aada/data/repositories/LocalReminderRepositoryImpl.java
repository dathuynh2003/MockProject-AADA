package ojt.aada.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.data.datasource.local.AppDatabase;
import ojt.aada.data.datasource.local.dao.ReminderDAO;
import ojt.aada.data.datasource.local.entities.ReminderEntity;
import ojt.aada.data.datasource.remote.MovieRetrofitAPI;
import ojt.aada.data.mapper.ReminderMapper;
import ojt.aada.domain.models.Reminder;
import ojt.aada.domain.repositories.LocalReminderRepository;

public class LocalReminderRepositoryImpl implements LocalReminderRepository {

    private ReminderDAO mReminderDAO;

    @Inject
    public LocalReminderRepositoryImpl(Application application) {
        mReminderDAO = AppDatabase.getInstance(application).reminderDAO();
    }

    @Override
    public Completable addReminder(Reminder reminder) {
        return Completable.fromAction(() -> mReminderDAO.insert(ReminderMapper.toEntity(reminder)))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable removeReminder(Reminder reminder) {
        return Completable.fromAction(() -> mReminderDAO.delete(reminder.getMovieId()))
                .subscribeOn(Schedulers.io());
    }


    @Override
    public Completable updateReminder(Reminder reminder) {
        ReminderEntity reminderEntity = ReminderMapper.toEntity(reminder);
        return Completable.fromAction(() -> mReminderDAO
                        .updateReminderByMovieId(reminderEntity.getTime(), reminderEntity.getMovieId()))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<Reminder>> getAllReminder() {
        return mReminderDAO.getAllReminder()
                .map(reminderEntities -> {
                    List<Reminder> reminders = new ArrayList<>();
                    for (ReminderEntity reminderEntity : reminderEntities) {
                        reminders.add(ReminderMapper.toDomain(reminderEntity));
                    }
                    return reminders;
                });
    }
}
