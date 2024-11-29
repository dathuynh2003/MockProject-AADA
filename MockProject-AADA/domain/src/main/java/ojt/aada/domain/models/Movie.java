package ojt.aada.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    private int id;
    private String title;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double rating;
    @SerializedName("adult")
    private boolean isAdult;
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    private boolean isFavorite = false;
    private Reminder reminder;

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Movie() {
    }

    public Movie(int id, String title, String releaseDate, double rating, boolean isAdult, String overview, String posterPath) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.isAdult = isAdult;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    public Movie(int id, String title, String releaseDate, double rating, boolean isAdult, String overview, String posterPath, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.isAdult = isAdult;
        this.overview = overview;
        this.posterPath = posterPath;
        this.isFavorite = isFavorite;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        rating = in.readDouble();
        isAdult = in.readByte() != 0;
        overview = in.readString();
        posterPath = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.rating);
        dest.writeByte(this.isAdult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Movie movie = (Movie) obj;
        return movie.title.equals(this.title) &&
                movie.releaseDate.equals(this.releaseDate) &&
                movie.rating == this.rating &&
                movie.isAdult == this.isAdult &&
                movie.overview.equals(this.overview) &&
                movie.posterPath.equals(this.posterPath) &&
                movie.isFavorite == this.isFavorite;
    }
}
