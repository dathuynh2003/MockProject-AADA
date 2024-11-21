package ojt.aada.mockproject_aada.ui.movie.favoritelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import ojt.aada.domain.models.Movie;
import ojt.aada.mockproject_aada.R;
import ojt.aada.mockproject_aada.databinding.MovieListItemViewBinding;

public class FavoriteListRVAdapter extends ListAdapter<Movie, FavoriteListRVAdapter.MovieListViewHolder> {
    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/original";
    private View.OnClickListener onFavClickListener;

    public void setOnFavClickListener(View.OnClickListener onFavClickListener) {
        this.onFavClickListener = onFavClickListener;
    }


    public FavoriteListRVAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(Movie oldItem, Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieListItemViewBinding binding = MovieListItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewHolder holder, int position) {
        Movie movie = getItem(position);
        holder.itemBinding.setMovie(movie);
        Picasso.get().load(BASE_IMG_URL.concat(movie.getPosterPath()))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.error_image_black_24)
                .into(holder.itemBinding.moviePoster);

        holder.itemBinding.favouriteStar.setTag(movie);
        holder.itemBinding.favouriteStar.setOnClickListener(onFavClickListener);
    }

    public static class MovieListViewHolder extends RecyclerView.ViewHolder {
        MovieListItemViewBinding itemBinding;

        public MovieListViewHolder(@NonNull MovieListItemViewBinding itemView) {
            super(itemView.getRoot());
            itemBinding = itemView;
        }
    }
}
