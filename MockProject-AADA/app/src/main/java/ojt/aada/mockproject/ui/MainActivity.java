package ojt.aada.mockproject.ui;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import javax.inject.Inject;

import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.ActivityMainBinding;
import ojt.aada.mockproject.databinding.NavHeaderBinding;
import ojt.aada.mockproject.di.MyApplication;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavHeaderBinding navHeaderBinding;
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

        mViewModel.getReminderLiveData().observe(this, reminders -> {
            if (reminders != null) {
                Log.d("TAG", "onCreate: " + reminders.size());
            }
        });

        View headerView = binding.navView.getHeaderView(0);
        navHeaderBinding = DataBindingUtil.bind(headerView);

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
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_popular).setVisible(false);
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_top_rated).setVisible(false);
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_upcoming).setVisible(false);
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_now_playing).setVisible(false);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
            actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
                handleBackPressed();
            });
        });

        // Get Android ID
        String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mViewModel.getUserProfileLiveData(androidId).observe(this, userProfile -> {
            if (userProfile != null) {
//                headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        headerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                        NavHeaderBinding navHeaderBinding = DataBindingUtil.bind(headerView);
//                        if (navHeaderBinding != null) {
//                            navHeaderBinding.setUser(userProfile);
//                        }
//                    }
//                });
                if (navHeaderBinding != null) {
                    navHeaderBinding.setUser(userProfile);
                }
            }
        });


        navHeaderBinding.editBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_main_fragment_to_profile_fragment);
            getSupportActionBar().hide();
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        if (Navigation.findNavController(this, R.id.nav_host_fragment_content_main).getCurrentDestination().getId() == R.id.profile_fragment) {
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().show();
        }

//        navHeaderBinding.topReminderRecyclerView.setAdapter();


        navHeaderBinding.showAllBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_main_fragment_to_reminder_fragment);

            binding.appBarMain.toolBar.setTitle("Reminders");
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_layout).setVisible(false);
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_search).setVisible(false);
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_popular).setVisible(false);
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_top_rated).setVisible(false);
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_upcoming).setVisible(false);
            binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_now_playing).setVisible(false);

            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);

            actionBarDrawerToggle.setToolbarNavigationClickListener(v1 -> {
                handleBackPressed();
            });

            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Add the OnBackPressedCallback once in onCreate (not inside handleBackPressed)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        });
    }

    //Create options menu to change layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.header_menu, menu);
        menuInflater.inflate(R.menu.filter_menu, menu);

        MenuItem changLayoutItem = menu.findItem(R.id.action_layout);
        MenuItem searchItem = menu.findItem(R.id.action_search);

//        changLayoutItem.setVisible(true);
//        searchItem.setVisible(false);

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
                        changLayoutItem.setVisible(true);
                    }

                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_popular).setVisible(true);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_top_rated).setVisible(true);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_upcoming).setVisible(true);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_now_playing).setVisible(true);

                    searchItem.setVisible(false);
                    break;
                case 1:
                    // Move to tab 1
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    binding.appBarMain.toolBar.setTitle("Favorite");
                    changLayoutItem.setVisible(false);

                    searchItem.setVisible(true);

                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_popular).setVisible(false);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_top_rated).setVisible(false);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_upcoming).setVisible(false);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_now_playing).setVisible(false);
                    break;
                case 2:
                    // Move to tab 2
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    binding.appBarMain.toolBar.setTitle("Settings");
                    changLayoutItem.setVisible(false);

                    searchItem.setVisible(false);

                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_popular).setVisible(false);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_top_rated).setVisible(false);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_upcoming).setVisible(false);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_now_playing).setVisible(false);
                    break;
                case 3:
                    // Move to tab 3
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    binding.appBarMain.toolBar.setTitle("About");
                    changLayoutItem.setVisible(false);

                    searchItem.setVisible(false);

                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_popular).setVisible(false);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_top_rated).setVisible(false);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_upcoming).setVisible(false);
                    binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_now_playing).setVisible(false);
                    break;
            }
        });

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.searchFavMoviesByTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.isEmpty()) {
                    mViewModel.searchFavMoviesByTitle(null);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_layout) {
            if (mIsGrid) {
                item.setIcon(R.drawable.ic_view_list_24dp);
                item.setTitle("Change to List View");
            } else {
                item.setIcon(R.drawable.ic_view_grid_24dp);
                item.setTitle("Change to Grid View");
            }
            mIsGrid = !mIsGrid;
            mViewModel.setIsGrid(mIsGrid);
        } else if (item.getItemId() == R.id.action_menu_popular) {
            Log.d("TAG", "onOptionsItemSelected: popular");
        } else if (item.getItemId() == R.id.action_menu_top_rated) {
            Log.d("TAG", "onOptionsItemSelected: top rated");
        } else if (item.getItemId() == R.id.action_menu_upcoming) {
            Log.d("TAG", "onOptionsItemSelected: upcoming");
        } else if (item.getItemId() == R.id.action_menu_now_playing) {
            Log.d("TAG", "onOptionsItemSelected: now playing");
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        handleBackPressed();
        return true;
    }


    public void handleBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        if (navController.getCurrentDestination().getId() != R.id.main_fragment) {
            navController.popBackStack();
            if (navController.getCurrentDestination().getId() == R.id.profile_fragment) {
                this.getSupportActionBar().show();
            }
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            return;
        }

        if (mViewModel.getCurrentPageLiveData().getValue() != null &&
                mViewModel.getCurrentPageLiveData().getValue() == 0) {
            NavController navTabController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tab);
            if (navTabController.getCurrentDestination().getId() == R.id.movie_detail_fragment) {
                navTabController.popBackStack(R.id.movie_list_fragment, false);
                actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                mViewModel.setSelectedMovieLiveData(null);
                binding.appBarMain.toolBar.getMenu().findItem(R.id.action_layout).setVisible(true);
                binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_popular).setVisible(true);
                binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_top_rated).setVisible(true);
                binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_upcoming).setVisible(true);
                binding.appBarMain.toolBar.getMenu().findItem(R.id.action_menu_now_playing).setVisible(true);
                return;
            }
        }

        //Before finish, set selected movie to null
//        mViewModel.setSelectedMovieLiveData(null);
//        finish();
        moveTaskToBack(true);
//        getOnBackPressedDispatcher().onBackPressed();
    }
}