package ojt.aada.mockproject.ui.movie.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.LoadState;
import androidx.paging.PagingData;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ojt.aada.domain.models.Movie;
import ojt.aada.mockproject.databinding.FragmentMovieListBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.MainViewModel;
import ojt.aada.mockproject.utils.Constants;

public class MovieListFragment extends Fragment {

    @Inject
    MainViewModel mViewModel;

    private FragmentMovieListBinding binding;
    private boolean mIsGrid;
    private PagingData<Movie> movies;
    private MovieListRVAdapter mMovieListRVAdapter;

    private String curCategoryStr;
    private int curRating;
    private int curReleaseYear;
    private String curSortByStr;

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
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
        mIsGrid = Boolean.TRUE.equals(mViewModel.getIsGrid().getValue());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMovieListBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        curCategoryStr = sharedPreferences.getString(Constants.CATEGORY_KEY, "Popular Movies");
        curRating = sharedPreferences.getInt(Constants.RATING_KEY, 0);
        curReleaseYear = Integer.parseInt(sharedPreferences.getString(Constants.RELEASE_YEAR_KEY, "1970"));
        curSortByStr = sharedPreferences.getString(Constants.SORT_BY_KEY, "Release Date");

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        setupRecyclerView(mIsGrid);

        //  Observe the remote movie list from the view model and update the recycler view
        mViewModel.getRemoteMovieList(curCategoryStr, curSortByStr, curRating, curReleaseYear)
                .observe(getViewLifecycleOwner(), movies -> {
            this.movies = movies;
            mMovieListRVAdapter.submitData(getLifecycle(), movies);
            Log.d("TAG", "onCreateView: " + mMovieListRVAdapter.snapshot().getItems().size());
            binding.pbLoading.setVisibility(View.GONE);
        });


        //  Observe the isGrid value from the view model and update the recycler view layout
        mViewModel.getIsGrid().observe(getViewLifecycleOwner(), isGrid -> {
            setupRecyclerView(isGrid);
            if (movies != null) {
                mMovieListRVAdapter.submitData(getLifecycle(), movies);
            }
        });

        //  Observe the updated movie from the view model and update the item in the recycler view
        mViewModel.getUpdatedMovieLiveData().observe(getViewLifecycleOwner(), movie -> {
//            int position = mMovieListRVAdapter.getCurrentList().indexOf(movie);
            for (int i = 0; i < mMovieListRVAdapter.getCurrentList().size(); i++) {
                if (mMovieListRVAdapter.getCurrentList().get(i).getId() == movie.getId()) {
                    mMovieListRVAdapter.getCurrentList().get(i).setFavorite(movie.isFavorite());
                    mMovieListRVAdapter.notifyItemChanged(i);
                    break;
                }
            }
        });

        return binding.getRoot();
    }

    /**
     * Setup the recycler view with the grid or linear layout
     *
     * @param isGrid
     */
    public void setupRecyclerView(boolean isGrid) {
        mMovieListRVAdapter = new MovieListRVAdapter(isGrid);
        LinearLayoutManager layoutManager;
        if (isGrid) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getContext());
        }

        mMovieListRVAdapter.addLoadStateListener(loadState -> {
//            if (loadState.getAppend() instanceof LoadState.Error) {
//                LoadState.Error error = (LoadState.Error) loadState.getAppend();
//                Log.d("TAG", "setupRecyclerView: " + error.getError().getMessage());
//            } else if (loadState.getAppend() instanceof LoadState.Loading) {
//                Log.d("TAG", "setupRecyclerView: Loading");
//            } else {
//                Log.d("TAG", "setupRecyclerView: Loaded");
//            }
            binding.movieRv.post(() -> {
                mMovieListRVAdapter.setLoading(loadState.getAppend() instanceof LoadState.Loading);
            });
            return null;
        });

        //  Set the onSelectedItemListener to the recycler view adapter
        mMovieListRVAdapter.setOnSelectedItemListener(v -> {
            Movie movie = (Movie) v.getTag();
            mViewModel.setSelectedMovieLiveData(movie);
        });

        //  Set the onFavClickListener to the recycler view adapter
        mMovieListRVAdapter.setOnFavClickListener(v -> {
            Movie movie = (Movie) v.getTag();
            movie.setFavorite(!movie.isFavorite());
            mViewModel.updateMovie(movie);
        });

        binding.movieRv.setLayoutManager(layoutManager);
        binding.movieRv.setAdapter(mMovieListRVAdapter);

    }

    /**
     * SharedPreferenceChangeListener to listen to the changes in the settings
     */
    private final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sp, key) -> {
        curCategoryStr = sp.getString(Constants.CATEGORY_KEY, "Popular Movies");
        curRating = sp.getInt(Constants.RATING_KEY, 0);
        curReleaseYear = Integer.parseInt(sp.getString(Constants.RELEASE_YEAR_KEY, "1970"));
        curSortByStr = sp.getString(Constants.SORT_BY_KEY, "Release Date");
        mViewModel.setIsCallApi(true);

        if (Constants.CATEGORY_KEY.equals(key)) {
            mViewModel.getRemoteMovieList(curCategoryStr, curSortByStr, curRating, curReleaseYear);
            binding.movieRv.scrollToPosition(0);
        } else if (Constants.RATING_KEY.equals(key)) {
            mViewModel.getRemoteMovieList(curCategoryStr, curSortByStr, curRating, curReleaseYear);
        } else if (Constants.RELEASE_YEAR_KEY.equals(key)) {
            mViewModel.getRemoteMovieList(curCategoryStr, curSortByStr, curRating, curReleaseYear);
        } else if (Constants.SORT_BY_KEY.equals(key)) {
            mViewModel.getRemoteMovieList(curCategoryStr, curSortByStr, curRating, curReleaseYear);
        }
    };
}