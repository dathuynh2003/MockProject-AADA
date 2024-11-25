package ojt.aada.data.mapper;

import ojt.aada.data.datasource.local.entities.MovieEntity;
import ojt.aada.domain.models.Movie;
import retrofit2.http.PUT;

public class MovieMapper {
    public static Movie mapToDomain(MovieEntity movieEntity) {
        return new Movie(
                movieEntity.getId(),
                movieEntity.getTitle(),
                movieEntity.getReleaseDate(),
                movieEntity.getRating(),
                movieEntity.isAdult(),
                movieEntity.getOverview(),
                movieEntity.getPosterPath(),
                movieEntity.isFavorite()
        );
    }

    public static MovieEntity mapToEntity(Movie movie) {
        return new MovieEntity(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getRating(),
                movie.isAdult(),
                movie.getOverview(),
                movie.getPosterPath(),
                movie.isFavorite()
        );
    }
}
