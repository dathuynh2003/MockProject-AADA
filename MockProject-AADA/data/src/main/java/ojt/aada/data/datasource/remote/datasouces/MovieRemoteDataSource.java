package ojt.aada.data.datasource.remote.datasouces;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava2.RxPagingSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.data.datasource.local.AppDatabase;
import ojt.aada.data.datasource.local.dao.MovieDAO;
import ojt.aada.data.datasource.local.entities.MovieEntity;
import ojt.aada.data.datasource.remote.MovieRetrofitAPI;
import ojt.aada.data.datasource.remote.response.MovieResponse;
import ojt.aada.data.mapper.MovieMapper;
import ojt.aada.data.utils.Validators;
import ojt.aada.domain.models.Movie;

@Singleton
public class MovieRemoteDataSource extends RxPagingSource<Integer, Movie> {
    private List<Movie> mMovieList;
    private Single<List<MovieEntity>> favMovieSingle;
    private final MovieRetrofitAPI movieRetrofitAPI;
    private MovieDAO mMovieDAO;

    private String category;
    private String sortBy;
    private int rating;
    private int releaseYear;


    @Inject
    public MovieRemoteDataSource(MovieRetrofitAPI movieRetrofitAPI, Application application) {
        this.movieRetrofitAPI = movieRetrofitAPI;

        mMovieDAO = AppDatabase.getInstance(application).movieDAO();
    }

    public void setParameters(String category, String sortBy, int rating, int releaseYear) {
        switch (category) {
            case "Popular Movies":
                this.category = "popular";
                break;
            case "Top Rated Movies":
                this.category = "top_rated";
                break;
            case "Upcoming Movies":
                this.category = "upcoming";
                break;
            case "Now Playing Movies":
                this.category = "now_playing";
                break;
        }
        this.sortBy = sortBy;
        this.rating = rating;
        this.releaseYear = releaseYear;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
//        int pageSize = loadParams.getLoadSize();

        Single<MovieResponse> movieSingle = movieRetrofitAPI.getMovies(category, page)
                .subscribeOn(Schedulers.io());

        favMovieSingle = mMovieDAO.getAllMovies()
                .subscribeOn(Schedulers.io()).firstOrError();

        return Single.zip(
                movieSingle,
                favMovieSingle,
                (response1, response2) -> {
                    mMovieList = response1.getResults();

                    //Filter
                    mMovieList.removeIf(movie -> movie.getRating() < rating ||
                            Integer.parseInt(movie.getReleaseDate().substring(0, 4)) < releaseYear);
                    //Sort
                    switch (sortBy) {
                        case "Rating":
                            mMovieList.sort((o1, o2) -> Float.compare((float) o2.getRating(), (float) o1.getRating()));
                            break;
                        case "Release Date":
                            mMovieList.sort((o1, o2) -> {
                                String formater = "yyyy-MM-dd";
                                Date date1 = Validators.convertDate(o1.getReleaseDate(), formater);
                                Date date2 = Validators.convertDate(o2.getReleaseDate(), formater);
                                if (date1 != null && date2 != null) {
                                    return date2.compareTo(date1);
                                }
                                return 0;
                            });
                            break;
                    }

                    // Sync favorite movies
                    List<MovieEntity> favMovies = response2;
                    favMovies.forEach(favMovie -> {
                        mMovieList.forEach(movie1 -> {
                            if (movie1.getId() == favMovie.getId()) {
                                movie1.setFavorite(favMovie.isFavorite());
                            }
                        });
                    });

                    return toLoadResult(mMovieList, page, response1.getTotalPages());
                }
        );
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();

        if (anchorPosition != null) {
            return (anchorPosition / 20) + 1;
        }
        return null;
    }

    private LoadResult<Integer, Movie> toLoadResult(List<Movie> results, Integer page, int totalPages) {
        return new LoadResult.Page<>(
                results,
                page == 1 ? null : page - 1,
                page < totalPages ? page + 1 : null
        );
    }

    public Single<Movie> getMovieDetail(int movieId) {

        Single<Movie> movieSingle = movieRetrofitAPI.getMovieDetail(movieId)
                .subscribeOn(Schedulers.io());

        return Single.zip(
                movieSingle,
                favMovieSingle,
                (response1, response2) -> {
                    response1.setFavorite(response2.stream().anyMatch(movieEntity -> movieEntity.getId() == movieId));
                    return response1;
                }
        );
    }
}
