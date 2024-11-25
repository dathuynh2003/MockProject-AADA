package ojt.aada.data.repositories;

import android.app.Application;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava2.PagingRx;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import kotlinx.coroutines.CoroutineScope;
import ojt.aada.data.datasource.local.AppDatabase;
import ojt.aada.data.datasource.local.dao.MovieDAO;
import ojt.aada.data.datasource.local.entities.MovieEntity;
import ojt.aada.data.datasource.remote.datasouces.MovieRemoteDataSource;
import ojt.aada.data.mapper.MovieMapper;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.RemoteMovieRepository;

public class RemoteMovieRepositoryImpl implements RemoteMovieRepository {

    private MovieRemoteDataSource movieRemoteDataSource;
    private final PagingConfig pagingConfig = new PagingConfig(
            10,
            5,
            false,
            20,
            10 + 2 * 5
    );

    //Constructor
    @Inject
    public RemoteMovieRepositoryImpl(MovieRemoteDataSource movieRemoteDataSource, Application application) {
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
