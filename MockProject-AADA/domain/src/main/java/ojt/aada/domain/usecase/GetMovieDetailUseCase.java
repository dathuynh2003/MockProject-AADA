package ojt.aada.domain.usecase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.RemoteMovieRepository;

public class GetMovieDetailUseCase {
    private RemoteMovieRepository remoteMovieRepository;

    @Inject
    public GetMovieDetailUseCase(RemoteMovieRepository remoteMovieRepository) {
        this.remoteMovieRepository = remoteMovieRepository;
    }

    public Single<Movie> getMovieDetailFromAPI(int movieId) {
        return remoteMovieRepository.getMovieDetail(movieId);
    }
}
