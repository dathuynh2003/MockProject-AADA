package ojt.aada.domain.usecase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.LocalMovieRepository;

public class GetFavMoviesUseCase {
    private LocalMovieRepository localMovieRepository;

    @Inject
    public GetFavMoviesUseCase(LocalMovieRepository localMovieRepository) {
        this.localMovieRepository = localMovieRepository;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        CompositeDisposable mCompositeDisposable = new CompositeDisposable();

        MutableLiveData<List<Movie>> favoriteMovies = new MutableLiveData<>();
        Disposable disposable = localMovieRepository.getFavoriteMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        favoriteMovies::setValue,
                        throwable -> Log.d("TAG", "getFavoriteMovies: " +throwable)
                );

        mCompositeDisposable.add(disposable);

        return favoriteMovies;
    }
}
