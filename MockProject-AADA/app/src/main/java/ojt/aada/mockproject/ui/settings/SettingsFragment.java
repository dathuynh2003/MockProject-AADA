package ojt.aada.mockproject.ui.settings;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ojt.aada.mockproject.R;
import ojt.aada.mockproject.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    private String curCategoryStr;
    private int curRating;
    private int curReleaseYear;
    private String curSortByStr;

    private ListPreference categoryListPreference;
    private Preference ratingPreference;
    private EditTextPreference releaseYearPreference;
    private ListPreference sortByListPreference;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        readSettingsFromPreferences();

        categoryListPreference = findPreference(Constants.CATEGORY_KEY);
        ratingPreference = findPreference(Constants.RATING_KEY);
        releaseYearPreference = findPreference(Constants.RELEASE_YEAR_KEY);
        sortByListPreference = findPreference(Constants.SORT_BY_KEY);

        if (categoryListPreference != null) {
            categoryListPreference.setOnPreferenceChangeListener((preference, newValue) -> true);
        }

        if (ratingPreference != null) {
            ratingPreference.setSummary(String.valueOf(curRating));
            ratingPreference.setOnPreferenceClickListener(preference -> {
                handleRatingDialog();
                return true;
            });
        }

        if (releaseYearPreference != null) {
            releaseYearPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                try {
                    if (newValue.toString().length() != 4 ||
                            Integer.parseInt(newValue.toString()) > Calendar.getInstance().get(Calendar.YEAR) + 1) {
                        Toast.makeText(requireContext(), "Invalid release year", Toast.LENGTH_SHORT).show();
                        return false;
                    }
//                    curReleaseYear = Integer.parseInt(newValue.toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Invalid release year", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            });
        }

        if (sortByListPreference != null) {
            sortByListPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                curSortByStr = newValue.toString();
                return true;
            });
        }
    }

    private void handleRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_rate, null);
        builder.setView(dialogView);

        SeekBar seekBarRate = dialogView.findViewById(R.id.seekbar_rate);
        AppCompatButton btnYes = dialogView.findViewById(R.id.btn_yes);
        AppCompatButton btnNo = dialogView.findViewById(R.id.btn_no);
        TextView tvCurrentRate = dialogView.findViewById(R.id.tv_current_rate);

        //Set current rating value
        seekBarRate.setProgress(curRating);
        tvCurrentRate.setText(String.valueOf(curRating));

        // Display dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // SeekBar change listener to update the current rate in the TextView
        seekBarRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView with the current progress value
                tvCurrentRate.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Button actions
        btnYes.setOnClickListener(v -> {
            curRating = seekBarRate.getProgress();
            ratingPreference.setSummary(String.valueOf(curRating));

            PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .edit()
                    .putInt(Constants.RATING_KEY, curRating)
                    .apply();

            dialog.dismiss();
        });

        btnNo.setOnClickListener(v -> dialog.dismiss());
    }


    private void readSettingsFromPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        curCategoryStr = sharedPreferences.getString(Constants.CATEGORY_KEY, "Popular Movies");
        curRating = sharedPreferences.getInt(Constants.RATING_KEY, 0);
        curReleaseYear = Integer.parseInt(sharedPreferences.getString(Constants.RELEASE_YEAR_KEY, "1970"));
        curSortByStr = sharedPreferences.getString(Constants.SORT_BY_KEY, "Release Date");
    }
}