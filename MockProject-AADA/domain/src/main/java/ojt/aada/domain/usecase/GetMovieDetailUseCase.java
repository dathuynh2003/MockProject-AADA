package ojt.aada.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Single;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.RemoteMovieRepository;

public class GetMovieDetailUseCase {
    private RemoteMovieRepository remoteMovieRepository;

    @Inject
    public GetMovieDetailUseCase(RemoteMovieRepository remoteMovieRepository) {
        this.remoteMovieRepository = remoteMovieRepository;
    }

    public Single<Movie> getMovieDetailFromAPI(Movie movie) {
        return remoteMovieRepository.getMovieDetail(movie);
    }
}
