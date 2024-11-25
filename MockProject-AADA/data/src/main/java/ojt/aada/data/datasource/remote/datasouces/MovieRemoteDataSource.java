package ojt.aada.data.datasource.remote.datasouces;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava2.RxPagingSource;

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
import ojt.aada.domain.models.Movie;

@Singleton
public class MovieRemoteDataSource extends RxPagingSource<Integer, Movie> {
    private List<Movie> mMovieList;
    private final MovieRetrofitAPI movieRetrofitAPI;
    private MovieDAO mMovieDAO;


    @Inject
    public MovieRemoteDataSource(MovieRetrofitAPI movieRetrofitAPI, Application application) {
        this.movieRetrofitAPI = movieRetrofitAPI;

        mMovieDAO = AppDatabase.getInstance(application).movieDAO();
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
//        int pageSize = loadParams.getLoadSize();

        Single<MovieResponse> movieSingle = movieRetrofitAPI.getMovies("popular", page)
                .subscribeOn(Schedulers.io());

        Single<List<MovieEntity>> favMovieSingle = mMovieDAO.getAllMovies()
                .subscribeOn(Schedulers.io()).firstOrError();

        return Single.zip(
                movieSingle,
                favMovieSingle,
                (response1, response2) -> {
                    mMovieList = response1.getResults();
                    List<MovieEntity> favMovies = response2;
                    favMovies.forEach(favMovie -> {
                        mMovieList.forEach(movie1 -> {
                            if (movie1.getId() == favMovie.getId()) {
                                movie1.setFavorite(true);
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

    public Single<Movie> getMovieDetail(Movie movie) {
        return movieRetrofitAPI.getMovieDetail(movie.getId());
    }
}
