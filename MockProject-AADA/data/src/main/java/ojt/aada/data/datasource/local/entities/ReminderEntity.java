package ojt.aada.data.datasource.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminder_table")
public class ReminderEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long time;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "movie_title")
    private String title;
    @ColumnInfo(name = "movie_release_date")
    private String releaseDate;
    @ColumnInfo(name = "movie_poster_path")
    private String posterPath;
    private double rating;

    public ReminderEntity() {
    }

    public ReminderEntity(long id, long time, int movieId, String title, String releaseDate, String posterPath, double rating) {
        this.id = id;
        this.time = time;
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.rating = rating;
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
}
