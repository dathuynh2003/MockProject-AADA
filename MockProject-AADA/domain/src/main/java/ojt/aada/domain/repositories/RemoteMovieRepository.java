package ojt.aada.domain.repositories;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import kotlinx.coroutines.CoroutineScope;
import ojt.aada.domain.models.Movie;

public interface RemoteMovieRepository {
    Flowable<PagingData<Movie>> getMoviesFromAPI(CoroutineScope viewModelScope, String category, String sortBy, int rating, int releaseYear);

    Single<Movie> getMovieDetail(Movie movie);
}
