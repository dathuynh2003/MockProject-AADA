package ojt.aada.mockproject.ui.reminder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import ojt.aada.domain.models.Movie;
import ojt.aada.domain.models.Reminder;
import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.ReminderItemViewBinding;
import ojt.aada.mockproject.utils.Constants;

public class ReminderListRVAdapter extends ListAdapter<Reminder, ReminderListRVAdapter.ReminderListViewHolder> {

    private View.OnClickListener mDeleteOnClickListener;
    private View.OnClickListener mOnClickListener;

    public void setDeleteOnClickListener(View.OnClickListener deleteOnClickListener) {
        mDeleteOnClickListener = deleteOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ReminderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReminderItemViewBinding binding = ReminderItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReminderListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderListViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: " + getItem(position).getTitle());
        holder.itemViewBinding.setReminder(getItem(position));
        Picasso.get().load(Constants.BASE_IMG_URL.concat(getItem(position).getPosterPath()))
                .placeholder(R.drawable.default_picture_100dp)
                .into(holder.itemViewBinding.reminderMovieIv);

        holder.itemViewBinding.deleteReminderIv.setTag(getItem(position));
        holder.itemViewBinding.deleteReminderIv.setOnClickListener(mDeleteOnClickListener);

        holder.itemViewBinding.getRoot().setTag(getItem(position));
        holder.itemViewBinding.getRoot().setOnClickListener(mOnClickListener);
    }

    public ReminderListRVAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Reminder> DIFF_CALLBACK = new DiffUtil.ItemCallback<Reminder>() {
        @Override
        public boolean areItemsTheSame(Reminder oldItem, Reminder newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Reminder oldItem, Reminder newItem) {
            return oldItem.getTime() == newItem.getTime() &&
                    oldItem.getMovieId() == newItem.getMovieId() &&
                    oldItem.getRating() == newItem.getRating() &&
                    oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getReleaseDate().equals(newItem.getReleaseDate()) &&
                    oldItem.getPosterPath().equals(newItem.getPosterPath());
        }
    };

    public static class ReminderListViewHolder extends RecyclerView.ViewHolder {
        ReminderItemViewBinding itemViewBinding;

        public ReminderListViewHolder(ReminderItemViewBinding itemView) {
            super(itemView.getRoot());
            itemViewBinding = itemView;
        }
    }
}
