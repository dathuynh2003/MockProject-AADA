package ojt.aada.mockproject.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import ojt.aada.domain.models.Reminder;
import ojt.aada.mockproject.databinding.HeaderReminderItemViewBinding;

public class NavHeaderReminderAdapter extends ListAdapter<Reminder, NavHeaderReminderAdapter.ReminderViewHolder> {

    public NavHeaderReminderAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Reminder> DIFF_CALLBACK = new DiffUtil.ItemCallback<Reminder>() {
        @Override
        public boolean areItemsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.getMovieId() == newItem.getMovieId();
        }

        @Override
        public boolean areContentsTheSame(Reminder oldItem, Reminder newItem) {
            return oldItem.getTime() == newItem.getTime() &&
                    oldItem.getMovieId() == newItem.getMovieId() &&
                    oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getPosterPath().equals(newItem.getPosterPath()) &&
                    oldItem.getReleaseDate().equals(newItem.getReleaseDate());
        }
    };

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HeaderReminderItemViewBinding binding = HeaderReminderItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReminderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        holder.itemViewBinding.setReminder(getItem(position));
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        HeaderReminderItemViewBinding itemViewBinding;

        public ReminderViewHolder(HeaderReminderItemViewBinding itemView) {
            super(itemView.getRoot());
            itemViewBinding = itemView;
        }
    }
}
