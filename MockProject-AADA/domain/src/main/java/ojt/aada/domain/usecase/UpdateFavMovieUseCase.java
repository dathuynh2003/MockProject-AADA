package ojt.aada.domain.usecase;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.LocalMovieRepository;

public class UpdateFavMovieUseCase {
    private LocalMovieRepository localMovieRepository;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public UpdateFavMovieUseCase(LocalMovieRepository localMovieRepository) {
        this.localMovieRepository = localMovieRepository;
    }

    public void updateFavMovie(Movie movie) {
        mCompositeDisposable = new CompositeDisposable();

        Disposable disposable;
        if (movie.isFavorite()) {
            disposable = localMovieRepository.insertFavMovie(movie)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                // Handle success
                            },
                            throwable -> {
                                // Handle error
                                Log.e("UpdateFavMovieUseCase", "Error inserting favorite movie", throwable);
                            }
                    );
        } else {
            disposable = localMovieRepository.deleteFavMovie(movie)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                // Handle success
                            },
                            throwable -> {
                                // Handle error
                                Log.e("UpdateFavMovieUseCase", "Error deleting favorite movie", throwable);
                            }
                    );
        }

        mCompositeDisposable.add(disposable);
    }
}
