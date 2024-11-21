package ojt.aada.data.datasource.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava2.RxPagingSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.domain.models.Movie;

@Singleton
public class MovieRemoteDataSource extends RxPagingSource<Integer, Movie> {
    private List<Movie> mMovieList;
    private final MovieRetrofitAPI movieRetrofitAPI;


    @Inject
    public MovieRemoteDataSource(MovieRetrofitAPI movieRetrofitAPI) {
        this.movieRetrofitAPI = movieRetrofitAPI;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
//        int pageSize = loadParams.getLoadSize();

        return movieRetrofitAPI.getMovies("popular", page)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    mMovieList = response.getResults();
                    return toLoadResult(mMovieList, page, response.getTotalPages());
                })
                .onErrorReturn(LoadResult.Error::new);
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
