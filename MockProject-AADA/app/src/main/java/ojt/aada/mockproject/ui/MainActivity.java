package ojt.aada.mockproject.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import javax.inject.Inject;

import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.ActivityMainBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.about.AboutFragment;
import ojt.aada.mockproject.ui.container.ContainerFragment;
import ojt.aada.mockproject.ui.main.ViewPagerStateAdapter;
import ojt.aada.mockproject.ui.movie.detail.MovieDetailFragment;
import ojt.aada.mockproject.ui.movie.favoritelist.FavoriteListFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean mIsGrid;
    @Inject
    MainViewModel mViewModel;

    private ArrayList<Fragment> fragmentArrayList;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplicationContext()).appComponent.inject(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mIsGrid = Boolean.TRUE.equals(mViewModel.getIsGrid().getValue());

        setSupportActionBar(binding.appBarMain.toolBar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.getRoot(), binding.appBarMain.toolBar, R.string.nav_open, R.string.nav_close);
        binding.getRoot().addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mViewModel.getSelectedMovieLiveData().observe(this, movie -> {
//            adapter.replaceFragment(0, movieDetailFragment);
            if (movie == null) {
                binding.appBarMain.toolBar.setTitle("Movies");
                return;
            }
            binding.appBarMain.toolBar.setTitle(movie.getTitle());
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_layout).setVisible(false);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
            actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
                handleBackPressed();
            });
        });

        // Add the OnBackPressedCallback once in onCreate (not inside handleBackPressed)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        });

        mViewModel.getCurrentPageLiveData().observe(this, integer -> {
            switch (integer) {
                case 0:
                    if (mViewModel.getSelectedMovieLiveData().getValue() != null) {
                        // Other tab (1,2,3) move to tab 0 but still display in Detail Fragment
                        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                        binding.appBarMain.toolBar.setTitle(mViewModel.getSelectedMovieLiveData().getValue().getTitle());
//                        binding.appBarMain.toolBar.getMenu().findItem(R.id.action_layout).setVisible(false);
                    } else {
                        // Other tab (1,2,3) move to tab 0 was displaying in List Fragment
                        binding.appBarMain.toolBar.setTitle("Movies");
                        binding.appBarMain.toolBar.getMenu().findItem(R.id.action_layout).setVisible(true);
                    }
                    break;
                case 1:
                    // Move to tab 1
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    binding.appBarMain.toolBar.setTitle("Favorite");
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_layout).setVisible(false);
                    break;
                case 2:
                    // Move to tab 2
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    binding.appBarMain.toolBar.setTitle("Settings");
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_layout).setVisible(false);
                    break;
                case 3:
                    // Move to tab 3
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    binding.appBarMain.toolBar.setTitle("About");
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_layout).setVisible(false);
                    break;
            }
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

    @Override
    public boolean onSupportNavigateUp() {
        handleBackPressed();
        return true;
    }


    public void handleBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        if (mViewModel.getCurrentPageLiveData().getValue() != null &&
                mViewModel.getCurrentPageLiveData().getValue() == 0) {
            NavController navTabController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tab);
            if (navTabController.getCurrentDestination().getId() == R.id.movie_detail_fragment) {
                navTabController.popBackStack(R.id.movie_list_fragment, false);
                actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                mViewModel.setSelectedMovieLiveData(null);
                binding.appBarMain.toolBar.getMenu().findItem(R.id.action_layout).setVisible(true);
                return;
            }
        }

        //Before finish, set selected movie to null
        mViewModel.setSelectedMovieLiveData(null);
        finish();
//        getOnBackPressedDispatcher().onBackPressed();
    }
}