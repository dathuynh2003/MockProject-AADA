package ojt.aada.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava2.PagingRx;


import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import kotlinx.coroutines.CoroutineScope;
import ojt.aada.data.datasource.remote.MovieRemoteDataSource;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.RemoteMovieRepository;

public class RemoteMovieRepositoryImpl implements RemoteMovieRepository {

    private MovieRemoteDataSource movieRemoteDataSource;
    private final PagingConfig pagingConfig = new PagingConfig(
            20,
            10,
            false,
            10,
            20 + 2 * 10
    );

    //Constructor
    @Inject
    public RemoteMovieRepositoryImpl(MovieRemoteDataSource movieRemoteDataSource) {
        this.movieRemoteDataSource = movieRemoteDataSource;
    }

    @Override
    public Flowable<PagingData<Movie>> getMoviesFromAPI(CoroutineScope viewModelScope) {
        return PagingRx.cachedIn(
                PagingRx.getFlowable(new Pager<>(pagingConfig, () -> movieRemoteDataSource)),
                viewModelScope
        );
    }

    @Override
    public Single<Movie> getMovieDetail(Movie movie) {
        return movieRemoteDataSource.getMovieDetail(movie);
    }
}
