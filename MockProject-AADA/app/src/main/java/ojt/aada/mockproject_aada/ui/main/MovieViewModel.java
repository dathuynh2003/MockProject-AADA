package ojt.aada.mockproject_aada.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagingData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.domain.models.CastnCrew;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.usecase.GetCastNCrewUseCase;
import ojt.aada.domain.usecase.GetMovieDetailUseCase;
import ojt.aada.domain.usecase.GetMoviesUseCase;

@Singleton
public class MovieViewModel extends ViewModel {
    private final MutableLiveData<PagingData<Movie>> mMovieListLiveData;
    private LiveData<List<Movie>> mFavoriteMoviesLiveData;
    private final MutableLiveData<Movie> mSelectedMovieLiveData;
    private final MutableLiveData<ArrayList<CastnCrew>> mCastNCrewLiveData;

    private MutableLiveData<Boolean> mIsGrid = new MutableLiveData<>();
    private final CompositeDisposable mCompositeDisposable;
    private final GetMoviesUseCase mGetMoviesUseCase;
    private final GetMovieDetailUseCase mGetMovieDetailUseCase;
    private final GetCastNCrewUseCase mGetCastNCrewUseCase;

    @Inject
    public MovieViewModel(GetMoviesUseCase getMoviesUseCase, GetMovieDetailUseCase getMovieDetailUseCase, GetCastNCrewUseCase getCastNCrewUseCase) {
        mGetMoviesUseCase = getMoviesUseCase;
        mCompositeDisposable = new CompositeDisposable();
        mGetMovieDetailUseCase = getMovieDetailUseCase;
        mGetCastNCrewUseCase = getCastNCrewUseCase;

        mMovieListLiveData = new MutableLiveData<>();
        mSelectedMovieLiveData = new MutableLiveData<>();
        mCastNCrewLiveData = new MutableLiveData<>();
    }

    // Get/Set mMovieListLiveData
    public MutableLiveData<PagingData<Movie>> getRemoteMovieList() {
        Disposable disposable = mGetMoviesUseCase.getMovies(ViewModelKt.getViewModelScope(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mMovieListLiveData::setValue,
                        throwable -> Log.d("FATAL", "getMoviePagingData: " + throwable)
                );
        mCompositeDisposable.add(disposable);

        return mMovieListLiveData;
    }

    // Get/Set mMovieDetailLiveData
    public void setSelectedMovieLiveData(Movie movie) {
        mSelectedMovieLiveData.setValue(movie);     //Nearly like setter for mSelectedMovieLiveData
        Disposable disposable = mGetCastNCrewUseCase.getCastNCrewFromAPI(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mCastNCrewLiveData::setValue,   //Nearly like setter for mCastNCrewLiveData
                        throwable -> Log.d("FATAL", "getMovieDetail: " + throwable)
                );
        mCompositeDisposable.add(disposable);
    }
    public MutableLiveData<Movie> getSelectedMovieLiveData() {
        return mSelectedMovieLiveData;
    }

    // Get mCastNCrewLiveData
    public MutableLiveData<ArrayList<CastnCrew>> getCastNCrewLiveData() {
        return mCastNCrewLiveData;
    }


    // Get/Set isGrid
    public MutableLiveData<Boolean> getIsGrid() {
        return mIsGrid;
    }

    public void setIsGrid(boolean isGrid) {
        mIsGrid.setValue(isGrid);
    }

//    public void updateMovie(Movie movie) {
//        mGetMoviesUseCase.updateMovie(movie);
//        // Update the movie list
//        getMovieList(); // Reload the data
//    }

//    public LiveData<List<Movie>> getFavoriteMovies() {
//        if (mFavoriteMoviesLiveData == null) {
//            mFavoriteMoviesLiveData = mGetMoviesUseCase.getFavMovies();
//        }
//        return mFavoriteMoviesLiveData;
//    }

}
