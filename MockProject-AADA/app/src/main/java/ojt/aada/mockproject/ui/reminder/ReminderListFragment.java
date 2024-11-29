package ojt.aada.mockproject.ui.reminder;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ojt.aada.domain.models.Reminder;
import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.FragmentReminderListBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.MainViewModel;

public class ReminderListFragment extends Fragment {

    @Inject
    MainViewModel mViewModel;

    private FragmentReminderListBinding binding;
    private ReminderListRVAdapter adapter;
    private NavController navController;

    public ReminderListFragment() {
        // Required empty public constructor
    }

    public static ReminderListFragment newInstance() {
        ReminderListFragment fragment = new ReminderListFragment();
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
    AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReminderListBinding.inflate(inflater, container, false);
        adapter = new ReminderListRVAdapter();
        alertDialog = new AlertDialog.Builder(requireContext())
                .create();


        mViewModel.getReminderLiveData().observe(getViewLifecycleOwner(), reminders -> {
            adapter.submitList(reminders);
        });

        adapter.setOnClickListener(v -> {
            Reminder reminder = (Reminder) v.getTag();
            mViewModel.getMovieDetail(reminder.getMovieId());
            navController.popBackStack();
        });

        adapter.setDeleteOnClickListener(v -> {
            Reminder reminder = (Reminder) v.getTag();
            showAlertDialog(reminder);
        });

        binding.rvReminder.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void showAlertDialog(Reminder reminder) {
        alertDialog.setTitle("Delete reminder for movie " + reminder.getTitle());
        alertDialog.setMessage("Are you sure you want to delete this reminder?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            mViewModel.deleteReminder(reminder);
            WorkManager.getInstance(requireContext()).cancelAllWorkByTag(String.valueOf(reminder.getMovieId()));
            alertDialog.dismiss();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}