package ojt.aada.mockproject.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import javax.inject.Inject;

import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.ActivityMainBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.about.AboutFragment;
import ojt.aada.mockproject.ui.movie.detail.MovieDetailFragment;
import ojt.aada.mockproject.ui.movie.favoritelist.FavoriteListFragment;
import ojt.aada.mockproject.ui.movie.list.MovieListFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean mIsGrid;
    @Inject
    MovieViewModel mViewModel;
    private MovieListFragment movieListFragment;
    private FavoriteListFragment favListFragment;
    private AboutFragment aboutFragment;
    private MovieDetailFragment movieDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplicationContext()).appComponent.inject(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mIsGrid = Boolean.TRUE.equals(mViewModel.getIsGrid().getValue());

        setSupportActionBar(binding.toolBar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.getRoot(), binding.toolBar, R.string.nav_open, R.string.nav_close);
        binding.getRoot().addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        movieListFragment = MovieListFragment.newInstance();
        favListFragment = FavoriteListFragment.newInstance();
        aboutFragment = AboutFragment.newInstance();
        movieDetailFragment = MovieDetailFragment.newInstance();

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(movieListFragment);
        fragmentArrayList.add(favListFragment);
        fragmentArrayList.add(new Fragment());
        fragmentArrayList.add(aboutFragment);
        ViewPagerStateAdapter adapter = new ViewPagerStateAdapter(this, fragmentArrayList);
        binding.viewPager2.setAdapter(adapter);

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

        mViewModel.getCastNCrewLiveData().observe(this, movie -> {
            movieDetailFragment = MovieDetailFragment.newInstance();
            adapter.replaceFragment(0, movieDetailFragment);
        });
    }

    //Create options menu to change layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.header_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mIsGrid) {
            item.setIcon(R.drawable.ic_view_list_24dp);
            item.setTitle("Change to List View");
        } else {
            item.setIcon(R.drawable.ic_view_grid_24dp);
            item.setTitle("Change to Grid View");
        }
        mIsGrid = !mIsGrid;
        mViewModel.setIsGrid(mIsGrid);
        return true;
    }
}