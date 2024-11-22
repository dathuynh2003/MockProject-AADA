package ojt.aada.mockproject.ui.movie.favoritelist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import ojt.aada.mockproject.databinding.FragmentFavoriteListBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.main.MovieViewModel;

public class FavoriteListFragment extends Fragment {

    @Inject
    MovieViewModel mViewModel;

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

//        mViewModel.getFavoriteMovies().observe(getViewLifecycleOwner(), movies -> {
//            mFavoriteListRVAdapter.submitList(movies);
//        });

//        mFavoriteListRVAdapter.setOnFavClickListener(v -> {
//            Movie movie = (Movie) v.getTag();
//            movie.setFavorite(!movie.isFavorite());
//            mViewModel.updateMovie(movie); // Update ViewModel
//            int pos = mFavoriteListRVAdapter.getCurrentList().indexOf(movie);
//            if (pos != -1) {
//                mFavoriteListRVAdapter.notifyItemChanged(pos);
//            }
//        });

        return binding.getRoot();
    }
}