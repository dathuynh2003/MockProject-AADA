package ojt.aada.domain.usecase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.domain.models.Reminder;
import ojt.aada.domain.repositories.LocalReminderRepository;

public class ManageReminderUseCase {
    private final LocalReminderRepository localReminderRepository;
    private CompositeDisposable compositeDisposable;

    @Inject
    public ManageReminderUseCase(LocalReminderRepository localReminderRepository) {
        this.localReminderRepository = localReminderRepository;
        compositeDisposable = new CompositeDisposable();
    }

    public void addReminder(Reminder reminder) {
        Disposable disposable = localReminderRepository.addReminder(reminder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("TAG", "Reminder added successfully"),
                        throwable -> Log.d("TAG", "Error adding reminder: " + throwable)
                );
        compositeDisposable.add(disposable);
    }

    public void deleteReminder(Reminder reminder) {
        Disposable disposable = localReminderRepository.removeReminder(reminder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("TAG", "Reminder deleted successfully"),
                        throwable -> Log.d("TAG", "Error deleting reminder: " + throwable)
                );
        compositeDisposable.add(disposable);
    }

    public LiveData<List<Reminder>> getAllReminder() {
        MutableLiveData<List<Reminder>> reminderListLiveData = new MutableLiveData<>();
        Disposable disposable = localReminderRepository.getAllReminder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        reminderListLiveData::setValue,
                        throwable -> {
                            Log.d("TAG", "getAllReminder: " + throwable);
                        }
                );

        compositeDisposable.add(disposable);
        return reminderListLiveData;
    }
}
