package ojt.aada.mockproject_aada.ui.movie.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import ojt.aada.domain.models.CastnCrew;
import ojt.aada.domain.models.Movie;
import ojt.aada.mockproject_aada.R;
import ojt.aada.mockproject_aada.databinding.CastncrewItemViewBinding;
import ojt.aada.mockproject_aada.databinding.MovieListItemViewBinding;

public class CastnCrewRVAdapter extends ListAdapter<CastnCrew, CastnCrewRVAdapter.ViewHolder> {
    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/original";

    public CastnCrewRVAdapter() {
        super(CastnCrewRVAdapter.DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CastnCrew> DIFF_CALLBACK = new DiffUtil.ItemCallback<CastnCrew>() {
        @Override
        public boolean areItemsTheSame(CastnCrew oldItem, CastnCrew newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(CastnCrew oldItem, CastnCrew newItem) {
            return oldItem.getActorName().equals(newItem.getActorName()) &&
                    oldItem.getImg().equals(newItem.getImg());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CastncrewItemViewBinding binding = CastncrewItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CastnCrew castnCrew = getItem(position);
        holder.binding.setCastNCrew(castnCrew);
        Picasso.get().load(BASE_IMG_URL + (castnCrew.getImg()))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.error_image_black_24)
                .into(holder.binding.castNCrewImg);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CastncrewItemViewBinding binding;

        public ViewHolder(@NonNull CastncrewItemViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
