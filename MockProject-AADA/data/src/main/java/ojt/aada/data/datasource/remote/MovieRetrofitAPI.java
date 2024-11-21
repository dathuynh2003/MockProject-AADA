package ojt.aada.data.datasource.remote;

import javax.inject.Singleton;

import io.reactivex.Single;
import ojt.aada.data.datasource.remote.response.CastNCrewResponse;
import ojt.aada.data.datasource.remote.response.MovieResponse;
import ojt.aada.domain.models.Movie;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Singleton
public interface MovieRetrofitAPI {
    // https://api.themoviedb.org/3/movie/{movieId}?api_key=e7631ffcb8e766993e5ec0c1f4245f93
    @GET("3/movie/{movieId}?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Single<Movie> getMovieDetail(@Path("movieId") int movieId);

    @GET("3/movie/{category}?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Single<MovieResponse> getMovies(@Path("category") String category, @Query("page") int page);

    @GET("3/movie/{movieId}/credits?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Single<CastNCrewResponse> getCastNCrew(@Path("movieId") int id);

}
