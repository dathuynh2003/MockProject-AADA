package ojt.aada.mockproject.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.RxWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.Year;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import ojt.aada.domain.models.Reminder;
import ojt.aada.mockproject.R;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.MainViewModel;
import ojt.aada.mockproject.utils.Constants;

public class ReminderWorker extends Worker {

    @Inject
    MainViewModel mViewModel;

    private static final String CHANNEL_ID = "1001";
    NotificationManager notificationManager;

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        ((MyApplication) context.getApplicationContext()).appComponent.inject(this);
    }

    @NonNull
    @Override
    public Result doWork() {
        Map<String, Object> data = getInputData().getKeyValueMap();

        Reminder reminder = new Reminder(
                (long) data.get("time"),
                (int) data.get("movieId"),
                (String) data.get("title"),
                (String) data.get("releaseDate"),
                (String) data.get("posterPath"),
                (double) data.get("rating"));

        Log.d("ReminderWorker", "doWork: " + reminder.getTitle());

        try {
            sendNotification(getApplicationContext(), reminder);
            mViewModel.deleteReminder(reminder);

        } catch (IOException e) {
            return Result.failure();
        }

        return Result.success();
    }


    private void sendNotification(Context context, Reminder reminder) throws IOException {

        Bitmap bitmap = Picasso.get().load(Constants.BASE_IMG_URL.concat(reminder.getPosterPath())).get();

        //Create and show the notification
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(context);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_24dp)
                .setLargeIcon(bitmap)
                .setContentTitle(reminder.getTitle())
                .setContentText("Year: " + reminder.getReleaseDate().substring(0, 4) + (" Rate: ") + reminder.getRating() + "/10")
                .setAutoCancel(true);

        Log.d("ReminderWorker", "sendNotification: Notifying with ID " + reminder.getMovieId());
        notificationManager.notify(reminder.getMovieId(), notificationBuilder.build());

    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Reminder Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationManager.createNotificationChannel(channel);
            Log.d("ReminderWorker", "createNotificationChannel: Channel created");
        }
    }
}
