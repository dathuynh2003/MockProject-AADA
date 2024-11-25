package ojt.aada.data.repositories;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.data.datasource.local.AppDatabase;
import ojt.aada.data.datasource.local.dao.MovieDAO;
import ojt.aada.data.datasource.local.entities.MovieEntity;
import ojt.aada.data.mapper.MovieMapper;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.LocalMovieRepository;

public class LocalMovieRepositoryImpl implements LocalMovieRepository {

    private MovieDAO mMovieDAO;

    @Inject
    public LocalMovieRepositoryImpl(Application application) {
        mMovieDAO = AppDatabase.getInstance(application).movieDAO();
    }

    @Override
    public Flowable<List<Movie>> getFavoriteMovies() {
        return mMovieDAO.getAllMovies()
                .map(movieEntities -> {
                    List<Movie> movies = new ArrayList<>();
                    for (MovieEntity movieEntity : movieEntities) {
                        movies.add(MovieMapper.mapToDomain(movieEntity));
                    }
                    return movies;
                });

    }

    @Override
    public Completable insertFavMovie(Movie movie) {
        return Completable.fromAction(() -> mMovieDAO.insert(MovieMapper.mapToEntity(movie)))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deleteFavMovie(Movie movie) {
        return Completable.fromAction(() -> mMovieDAO.delete(movie.getId()))
                .subscribeOn(Schedulers.io());
    }
}
