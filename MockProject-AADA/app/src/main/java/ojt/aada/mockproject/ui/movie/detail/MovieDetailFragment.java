package ojt.aada.mockproject.ui.movie.detail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import ojt.aada.domain.models.Movie;
import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.FragmentMovieDetailBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.main.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {
    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/original";

    private FragmentMovieDetailBinding binding;
    private CastnCrewRVAdapter adapter;
    @Inject
    MainViewModel mViewModel;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance() {
        MovieDetailFragment fragment = new MovieDetailFragment();
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
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        adapter = new CastnCrewRVAdapter();
        binding.castNCrewRecyclerView.setAdapter(adapter);

        mViewModel.getSelectedMovieLiveData().observe(getViewLifecycleOwner(), movie -> {
            binding.setDetail(movie);
            Log.d("TAG", "onCreateView: " +movie.isFavorite());
            Picasso.get().load(BASE_IMG_URL.concat(movie.getPosterPath()))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.error_image_black_24)
                    .into(binding.movieDetailPoster);
        });

        mViewModel.getCastNCrewLiveData().observe(getViewLifecycleOwner(), castNCrew -> {
            if (castNCrew != null) {
                adapter.submitList(castNCrew);
            }
            binding.castNCrewPb.setVisibility(View.GONE);
        });

        binding.favStar.setOnClickListener(v -> {
            Movie movie = binding.getDetail();
            movie.setFavorite(!movie.isFavorite());
            mViewModel.updateMovie(binding.getDetail());
            //binding again to update the UI
            binding.setDetail(movie);
        });

        return binding.getRoot();
    }
}