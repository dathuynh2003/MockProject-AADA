package ojt.aada.mockproject.ui.movie.favoritelist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import ojt.aada.domain.models.Movie;
import ojt.aada.mockproject.databinding.FragmentFavoriteListBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.MainViewModel;

public class FavoriteListFragment extends Fragment {

    @Inject
    MainViewModel mViewModel;

    private FragmentFavoriteListBinding binding;
    private FavoriteListRVAdapter mFavoriteListRVAdapter;

    public FavoriteListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavoriteListFragment newInstance() {
        FavoriteListFragment fragment = new FavoriteListFragment();
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
        binding = FragmentFavoriteListBinding.inflate(getLayoutInflater());
        mFavoriteListRVAdapter = new FavoriteListRVAdapter();
        binding.favMovieRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.favMovieRv.setAdapter(mFavoriteListRVAdapter);

        mViewModel.getFavoriteMovies().observe(getViewLifecycleOwner(), movies -> {
            if (mViewModel.getFilteredFavMoviesLiveData().getValue() == null) {
                mFavoriteListRVAdapter.submitList(movies);
            } else {
                mViewModel.fetchFilteredFavMovies();
            }
        });

        mViewModel.getFilteredFavMoviesLiveData().observe(getViewLifecycleOwner(), movies -> {
            if(movies == null) {
                mFavoriteListRVAdapter.submitList(mViewModel.getFavoriteMovies().getValue());
            } else {
                mFavoriteListRVAdapter.submitList(movies);
            }
        });

        mFavoriteListRVAdapter.setOnFavClickListener(v -> {
            Movie movie = (Movie) v.getTag();
            movie.setFavorite(!movie.isFavorite());
            mViewModel.updateMovie(movie); // Update ViewModel
//            mViewModel.setSelectedMovieLiveData(movie); // Update ViewModel
        });

        mFavoriteListRVAdapter.setOnSelectedMovieListener(v -> {
            Movie movie = (Movie) v.getTag();
            mViewModel.setMoveToDetail(true);
            mViewModel.setSelectedMovieLiveData(movie); // Update ViewModel
        });

        return binding.getRoot();
    }
}