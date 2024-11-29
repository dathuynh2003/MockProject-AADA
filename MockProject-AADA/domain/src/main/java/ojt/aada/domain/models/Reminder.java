package ojt.aada.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Reminder implements Parcelable {
    private long id;
    private long time;
    private int movieId;
    private String title;
    private String releaseDate;
    private String posterPath;
    private double rating;

    public Reminder() {
    }

    public Reminder(long time, int movieId, String title, String releaseDate, String posterPath, double rating) {
        this.time = time;
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.rating = rating;
    }

    protected Reminder(Parcel in) {
        id = in.readLong();
        time = in.readLong();
        movieId = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        rating = in.readDouble();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(time);
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeDouble(rating);
    }

    public Map toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("time", time);
        map.put("movieId", movieId);
        map.put("title", title);
        map.put("releaseDate", releaseDate);
        map.put("posterPath", posterPath);
        map.put("rating", rating);
        return map;
    }
}
