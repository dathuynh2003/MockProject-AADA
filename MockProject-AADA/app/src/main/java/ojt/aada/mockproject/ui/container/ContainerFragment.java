package ojt.aada.mockproject.ui.container;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ojt.aada.mockproject.R;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.main.MainViewModel;

public class ContainerFragment extends Fragment {

    @Inject
    MainViewModel mViewModel;
    private NavController navController;

    public ContainerFragment() {
        // Required empty public constructor
    }

    public static ContainerFragment newInstance() {
        ContainerFragment fragment = new ContainerFragment();
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
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        } else {
            Log.d("FATAL", "NavHostFragment not found!");
        }

        mViewModel.getSelectedMovieLiveData().observe(getViewLifecycleOwner(), movie -> {
            if (movie != null) {
                navController.navigate(R.id.action_movieListFragment_to_movieDetailFragment);
            }
        });

        return view;
    }
}