package ojt.aada.domain.usecase;

import androidx.paging.PagingData;

import javax.inject.Inject;

import io.reactivex.Flowable;
import kotlinx.coroutines.CoroutineScope;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.RemoteMovieRepository;

public class GetMoviesUseCase {
    private RemoteMovieRepository remoteMovieRepository;

    @Inject
    public GetMoviesUseCase(RemoteMovieRepository remoteMovieRepository) {
        this.remoteMovieRepository = remoteMovieRepository;
    }

    public Flowable<PagingData<Movie>> getMovies(CoroutineScope viewModelScope) {
        return remoteMovieRepository.getMoviesFromAPI(viewModelScope);
    }

}
