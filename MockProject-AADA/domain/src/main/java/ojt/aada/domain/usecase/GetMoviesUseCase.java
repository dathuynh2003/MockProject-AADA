package ojt.aada.domain.usecase;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import kotlinx.coroutines.CoroutineScope;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.LocalMovieRepository;
import ojt.aada.domain.repositories.RemoteMovieRepository;

public class GetMoviesUseCase {
    private RemoteMovieRepository mRemoteMovieRepository;

    @Inject
    public GetMoviesUseCase(RemoteMovieRepository remoteMovieRepository) {
        mRemoteMovieRepository = remoteMovieRepository;
    }

    public Flowable<PagingData<Movie>> getMovies(CoroutineScope viewModelScope, String category, String sortBy, int rating, int releaseYear) {
        return mRemoteMovieRepository.getMoviesFromAPI(viewModelScope, category, sortBy, rating, releaseYear);
    }

}
