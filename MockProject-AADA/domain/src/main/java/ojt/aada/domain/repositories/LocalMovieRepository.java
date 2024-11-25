package ojt.aada.domain.repositories;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import ojt.aada.domain.models.Movie;

public interface LocalMovieRepository {
    Flowable<List<Movie>> getFavoriteMovies();

    Completable deleteFavMovie(Movie movie);
    Completable insertFavMovie(Movie movie);
}
