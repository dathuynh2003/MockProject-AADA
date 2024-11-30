package ojt.aada.mockproject.ui.movie.detail;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ojt.aada.domain.models.Movie;
import ojt.aada.domain.models.Reminder;
import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.FragmentMovieDetailBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.MainViewModel;
import ojt.aada.mockproject.utils.Validator;
import ojt.aada.mockproject.workers.ReminderWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {
    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/original";

    private FragmentMovieDetailBinding binding;
    private CastnCrewRVAdapter adapter;

    private Calendar calendar;

    @Inject
    MainViewModel mViewModel;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance() {
        MovieDetailFragment fragment = new MovieDetailFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) requireContext().getApplicationContext()).appComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        adapter = new CastnCrewRVAdapter();
        binding.castNCrewRecyclerView.setAdapter(adapter);

        mViewModel.getSelectedMovieLiveData().observe(getViewLifecycleOwner(), movie -> {
            if (movie == null) return;
            binding.setDetail(movie);
            Picasso.get().load(BASE_IMG_URL.concat(movie.getPosterPath()))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.error_image_black_24)
                    .into(binding.movieDetailPoster);

            // When selected movie in List
            if (movie.getReminder() != null) {
                Date reminderDate = new Date(movie.getReminder().getTime());
                binding.reminderText.setText(Validator.convertDate(reminderDate, "yyyy/MM/dd HH:mm"));
            }
        });

        mViewModel.getReminderLiveData().observe(getViewLifecycleOwner(), reminders -> {
            boolean isReminder = false;
            // When notify was set in MovieDetailFragment, update UI
            for (Reminder reminder : reminders) {
                if (reminder.getMovieId() == mViewModel.getSelectedMovieLiveData().getValue().getId()) {
                    mViewModel.getSelectedMovieLiveData().getValue().setReminder(reminder);
                    Date reminderDate = new Date(reminder.getTime());
                    isReminder = true;
                    binding.reminderText.setText(Validator.convertDate(reminderDate, "yyyy/MM/dd HH:mm"));
                    break;
                }
            }

            // When notify was push to user, the reminder will be deleted
            if (!isReminder) {
                binding.reminderText.setText("");
            }
        });

//        mViewModel.getReminderLiveData().observe(getViewLifecycleOwner(), reminders -> {
//            boolean isReminder = false;
//            for (Reminder reminder : reminders) {
//                if (reminder.getMovieId() == mViewModel.getSelectedMovieLiveData().getValue().getId()) {
//                    mViewModel.getSelectedMovieLiveData().getValue().setReminder(reminder);
//                    Date reminderDate = new Date(reminder.getTime());
//                    isReminder = true;
//                    binding.reminderText.setVisibility(View.VISIBLE);
//                    binding.reminderText.setText(Validator.convertDate(reminderDate, "yyyy/MM/dd HH:mm"));
//                    break;
//                }
//            }
//
//            if (!isReminder) {
//                binding.reminderText.setText("");
//            }
//        });

        mViewModel.getCastNCrewLiveData().observe(getViewLifecycleOwner(), castNCrew -> {
            if (castNCrew != null) {
                adapter.submitList(castNCrew);
            }
            binding.castNCrewPb.setVisibility(View.GONE);
        });
        // Set the favorite star click listener
        // This will update the movie favorite status
        binding.favStar.setOnClickListener(v -> {
            Movie movie = binding.getDetail();
            movie.setFavorite(!movie.isFavorite());
            mViewModel.updateMovie(binding.getDetail());

            // Update the star icon
//            binding.setDetail(movie);
        });

//        // Observe the updated movie
//        mViewModel.getUpdatedMovieLiveData().observe(getViewLifecycleOwner(), movie -> {
//            if (mViewModel.getSelectedMovieLiveData().getValue().getId() == movie.getId()) {
//                binding.setDetail(mViewModel.getSelectedMovieLiveData().getValue());
//            }
//        });

        binding.reminderButton.setOnClickListener(v -> {
            showDateTimePicker(binding.getDetail());
        });

        return binding.getRoot();
    }


    /**
     * Show the date and time picker dialog
     *
     * @param movie the movie to set the reminder
     */
    private void showDateTimePicker(Movie movie) {
        calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            showTimePicker(movie);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // Once date and time are set, call this to schedule the alarm
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        datePickerDialog.setTitle(dateFormat.format(calendar.getTime()));
        datePickerDialog.show();
    }

    /**
     * Show the time picker dialog
     *
     * @param movie the movie to set the reminder
     */
    private void showTimePicker(Movie movie) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            // Check if the selected date and time are in the past
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                Toast.makeText(getContext(), "Please select a future date and time.", Toast.LENGTH_SHORT).show();
            } else {
                // Create or update reminder in the database
                if (movie.getReminder() == null) {
                    Reminder newReminder = new Reminder(calendar.getTimeInMillis(), movie.getId(), movie.getTitle(), movie.getReleaseDate(), movie.getPosterPath(), movie.getRating());
                    mViewModel.addReminder(newReminder);
                    scheduleReminder(requireContext(), newReminder);
                } else {
//                    Reminder reminder = movie.getReminder();
//                    reminder.setTime(calendar.getTimeInMillis());
                    // Clone newReminder to get new pointer
                    Reminder newReminder = new Reminder(calendar.getTimeInMillis(), movie.getId(), movie.getTitle(), movie.getReleaseDate(), movie.getPosterPath(), movie.getRating());
                    mViewModel.updateReminder(newReminder);
                    scheduleReminder(requireContext(), newReminder);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.setTitle("Set Time");
        timePickerDialog.show();
    }

    private void scheduleReminder(Context context, Reminder reminder) {
        long delay = reminder.getTime() - System.currentTimeMillis();
        if (delay > 0) {
            @SuppressLint("RestrictedApi")
            Data inputData = new Data.Builder()
                    .putAll(reminder.toMap())
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .addTag(String.valueOf(reminder.getMovieId()))
                    .setInputData(inputData)
                    .build();

            WorkManager.getInstance(requireActivity())
                    .beginUniqueWork(String.valueOf(reminder.getMovieId()), ExistingWorkPolicy.REPLACE, workRequest)
                    .enqueue();
        }
    }
}