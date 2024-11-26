package ojt.aada.mockproject.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import javax.inject.Inject;

import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.FragmentMainBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.MainViewModel;
import ojt.aada.mockproject.ui.about.AboutFragment;
import ojt.aada.mockproject.ui.container.ContainerFragment;
import ojt.aada.mockproject.ui.movie.detail.MovieDetailFragment;
import ojt.aada.mockproject.ui.movie.favoritelist.FavoriteListFragment;
import ojt.aada.mockproject.ui.settings.SettingsFragment;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private ViewPagerStateAdapter mViewPagerStateAdapter;
    @Inject
    MainViewModel mViewModel;

    private ContainerFragment mContainerFragment;
    //    private MovieListFragment mMovieListFragment;
    private FavoriteListFragment mFavListFragment;
    private SettingsFragment mSettingsFragment;
    private AboutFragment mAboutFragment;
    private MovieDetailFragment mMovieDetailFragment;

    private ArrayList<Fragment> fragmentArrayList;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) requireContext().getApplicationContext()).appComponent.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        mContainerFragment = ContainerFragment.newInstance();
        mFavListFragment = FavoriteListFragment.newInstance();
        mSettingsFragment = SettingsFragment.newInstance();
        mAboutFragment = AboutFragment.newInstance();
        mMovieDetailFragment = MovieDetailFragment.newInstance();

        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(mContainerFragment);
        fragmentArrayList.add(mFavListFragment);
        fragmentArrayList.add(mSettingsFragment);
        fragmentArrayList.add(mAboutFragment);

        mViewPagerStateAdapter = new ViewPagerStateAdapter(requireActivity(), fragmentArrayList);
        binding.viewPager2.setAdapter(mViewPagerStateAdapter);

        ArrayList<String> titleArrayList = new ArrayList<>();
        titleArrayList.add("Movies");
        titleArrayList.add("Favourite");
        titleArrayList.add("Settings");
        titleArrayList.add("About");
        ArrayList<Integer> iconArrayList = new ArrayList<>();
        iconArrayList.add(R.drawable.ic_home_24dp);
        iconArrayList.add(R.drawable.ic_favorite_24dp);
        iconArrayList.add(R.drawable.ic_settings_24dp);
        iconArrayList.add(R.drawable.ic_info_24dp);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager2,
                (tab, pos) -> {
                    tab.setText(titleArrayList.get(pos));
                    tab.setIcon(iconArrayList.get(pos));
                }).attach();

        binding.viewPager2.setOffscreenPageLimit(1);

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("TAG", "onPageSelected: " + position);
                mViewModel.setCurrentPageLiveData(position);
            }
        });

        mViewModel.getSelectedMovieLiveData().observe(getViewLifecycleOwner(), movie -> {
            if (movie == null) {
                return;
            }
            binding.viewPager2.setCurrentItem(0);
        });

        mViewModel.getFavoriteMovies().observe(getViewLifecycleOwner(), movies -> {
            updateFavouriteBadge(movies.size());
        });

        return binding.getRoot();
    }

    private void updateFavouriteBadge(int count) {
        BadgeDrawable badgeDrawable = binding.tabLayout.getTabAt(1).getOrCreateBadge();
        badgeDrawable.setVisible(true);
        String countStr = (count > 99) ? "99+" : String.valueOf(count);
        if (count > 0) {
            badgeDrawable.setText(countStr);
        } else {
            badgeDrawable.setVisible(false);
        }
    }

}