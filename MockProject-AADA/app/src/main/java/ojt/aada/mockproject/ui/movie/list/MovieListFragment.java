package ojt.aada.mockproject.ui.movie.list;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.LoadState;
import androidx.paging.PagingData;
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
import ojt.aada.mockproject.ui.main.MainViewModel;

public class MovieListFragment extends Fragment {

    @Inject
    MainViewModel mViewModel;

    private FragmentMovieListBinding binding;
    private boolean mIsGrid;
    private PagingData<Movie> movies;
    private MovieListRVAdapter mMovieListRVAdapter;

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

        setupRecyclerView(mIsGrid);

        mViewModel.getRemoteMovieList().observe(getViewLifecycleOwner(), movies -> {
            this.movies = movies;
            mMovieListRVAdapter.submitData(getLifecycle(), movies);
            Log.d("TAG", "onCreateView: " + mMovieListRVAdapter.snapshot().getItems().size());
            binding.pbLoading.setVisibility(View.GONE);
        });


        mViewModel.getIsGrid().observe(getViewLifecycleOwner(), isGrid -> {
            setupRecyclerView(isGrid);
            if (movies != null) {
                mMovieListRVAdapter.submitData(getLifecycle(), movies);
            }
        });

//        mMovieListRVAdapter.setOnFavClickListener(v -> {
//            Movie movie = (Movie) v.getTag();
//            movie.setFavorite(!movie.isFavorite());
//            mViewModel.updateMovie(movie);
//            int position = mMovieListRVAdapter.getCurrentList().indexOf(movie);
//            if (position != -1) {
//                mMovieListRVAdapter.notifyItemChanged(position);
//            }
//        });


        return binding.getRoot();
    }

    public void setupRecyclerView(boolean isGrid) {
        mMovieListRVAdapter = new MovieListRVAdapter(isGrid);
        LinearLayoutManager layoutManager;
        if (isGrid) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getContext());
        }

        mMovieListRVAdapter.addLoadStateListener(loadState -> {
            binding.movieRv.post(() -> {
                mMovieListRVAdapter.setLoading(loadState.getAppend() instanceof LoadState.Loading);
            });
            return null;
        });

        mMovieListRVAdapter.setOnSelectedItemListener(v -> {
            Movie movie = (Movie) v.getTag();
            mViewModel.setSelectedMovieLiveData(movie);
        });

        binding.movieRv.setLayoutManager(layoutManager);
        binding.movieRv.setAdapter(mMovieListRVAdapter);

    }
}