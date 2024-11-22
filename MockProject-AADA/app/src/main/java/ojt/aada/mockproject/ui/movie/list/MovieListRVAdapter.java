package ojt.aada.mockproject.ui.movie.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ojt.aada.domain.models.Movie;
import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.MovieGridItemViewBinding;
import ojt.aada.mockproject.databinding.MovieListItemViewBinding;

public class MovieListRVAdapter extends PagingDataAdapter<Movie, RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/original";
    private boolean isGrid = false;
    private boolean isLoading = false;

    private View.OnClickListener onFavClickListener;
    private View.OnClickListener onSelectedItemListener;

    public void setOnFavClickListener(View.OnClickListener onFavClickListener) {
        this.onFavClickListener = onFavClickListener;
    }

    public void setOnSelectedItemListener(View.OnClickListener onSelectedItemListener) {
        this.onSelectedItemListener = onSelectedItemListener;
    }

    public MovieListRVAdapter(boolean isGrid) {
        super(DIFF_CALLBACK);
        this.isGrid = isGrid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_PROGRESS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item_view, parent, false);
            return new LoadingViewHolder(view);
        } else {
            if (isGrid) {
                MovieGridItemViewBinding binding = MovieGridItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new MovieGridViewHolder(binding);
            } else {
                MovieListItemViewBinding binding = MovieListItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new MovieListViewHolder(binding);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_PROGRESS) {
            ((LoadingViewHolder) holder).showLoading();
            return;
        }


        Movie movie = getItem(position);
        if (isGrid) {
            MovieGridViewHolder viewHolder = (MovieGridViewHolder) holder;
            viewHolder.itemViewBinding.setMovie(movie);
            Picasso.get().load(BASE_IMG_URL.concat(movie.getPosterPath()))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.error_image_black_24)
                    .into(viewHolder.itemViewBinding.moviePoster);
        } else {
            MovieListViewHolder viewHolder = (MovieListViewHolder) holder;
            viewHolder.itemBinding.setMovie(movie);
            Picasso.get().load(BASE_IMG_URL.concat(movie.getPosterPath()))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.error_image_black_24)
                    .into(viewHolder.itemBinding.moviePoster);

            viewHolder.itemBinding.favouriteStar.setTag(movie);
            viewHolder.itemBinding.favouriteStar.setOnClickListener(onFavClickListener);
        }

        holder.itemView.setTag(movie);
        holder.itemView.setOnClickListener(onSelectedItemListener);

    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    public static class MovieListViewHolder extends RecyclerView.ViewHolder {
        MovieListItemViewBinding itemBinding;

        public MovieListViewHolder(@NonNull MovieListItemViewBinding itemView) {
            super(itemView.getRoot());
            itemBinding = itemView;
        }
    }

    public static class MovieGridViewHolder extends RecyclerView.ViewHolder {
        MovieGridItemViewBinding itemViewBinding;

        public MovieGridViewHolder(@NonNull MovieGridItemViewBinding itemView) {
            super(itemView.getRoot());
            itemViewBinding = itemView;
        }
    }

    public static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.equals(newItem);
        }
    };

    public List<Movie> getCurrentList() {
        return snapshot().getItems();
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loading_pb);
        }

        public void showLoading() {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyItemChanged(getItemCount());
    }


}
