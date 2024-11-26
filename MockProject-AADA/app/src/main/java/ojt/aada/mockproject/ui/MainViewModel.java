package ojt.aada.mockproject.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagingData;
import androidx.paging.PagingDataTransforms;
import androidx.paging.rxjava2.PagingRx;

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
import ojt.aada.domain.usecase.GetFavMoviesUseCase;
import ojt.aada.domain.usecase.GetMovieDetailUseCase;
import ojt.aada.domain.usecase.GetMoviesUseCase;
import ojt.aada.domain.usecase.UpdateFavMovieUseCase;

@Singleton
public class MainViewModel extends ViewModel {
    private final MutableLiveData<PagingData<Movie>> mMovieListLiveData;
    private LiveData<List<Movie>> mFavoriteMoviesLiveData;
    private MutableLiveData<Movie> mSelectedMovieLiveData;
    private final MutableLiveData<ArrayList<CastnCrew>> mCastNCrewLiveData;
    private final MutableLiveData<Integer> currentPageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Movie> mUpdatedMovieLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> mIsGrid = new MutableLiveData<>();
    private final CompositeDisposable mCompositeDisposable;
    private final GetMoviesUseCase mGetMoviesUseCase;
    private final GetMovieDetailUseCase mGetMovieDetailUseCase;
    private final GetCastNCrewUseCase mGetCastNCrewUseCase;
    private final GetFavMoviesUseCase mGetFavMoviesUseCase;
    private final UpdateFavMovieUseCase mUpdateFavMovieUseCase;

    @Inject
    public MainViewModel(GetMoviesUseCase getMoviesUseCase,
                         GetMovieDetailUseCase getMovieDetailUseCase,
                         GetCastNCrewUseCase getCastNCrewUseCase,
                         GetFavMoviesUseCase getFavMoviesUseCase,
                         UpdateFavMovieUseCase updateFavMovieUseCase) {

        mGetMoviesUseCase = getMoviesUseCase;
        mCompositeDisposable = new CompositeDisposable();
        mGetMovieDetailUseCase = getMovieDetailUseCase;
        mGetCastNCrewUseCase = getCastNCrewUseCase;
        mGetFavMoviesUseCase = getFavMoviesUseCase;
        mUpdateFavMovieUseCase = updateFavMovieUseCase;

        mMovieListLiveData = new MutableLiveData<>();
        mSelectedMovieLiveData = new MutableLiveData<>();
        mCastNCrewLiveData = new MutableLiveData<>();
    }

    // Get/Set mMovieListLiveData
    public MutableLiveData<PagingData<Movie>> getRemoteMovieList(String category, String sortBy, int rating, int releaseYear) {
        if (mMovieListLiveData.getValue() == null) {
            Disposable disposable = mGetMoviesUseCase.getMovies(ViewModelKt.getViewModelScope(this), category, sortBy, rating, releaseYear)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            mMovieListLiveData::setValue,
                            throwable -> Log.d("FATAL", "getMoviePagingData: " + throwable)
                    );
            mCompositeDisposable.add(disposable);
        }

        return mMovieListLiveData;
    }

    // Get/Set mMovieDetailLiveData
    public void setSelectedMovieLiveData(Movie movie) {
        if (movie == null) {
            mSelectedMovieLiveData.setValue(null);
            return;
        }
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

    /**
     * Update the movie when the user click on the favorite button
     *
     * @param movie
     */
    public void updateMovie(Movie movie) {
        mUpdateFavMovieUseCase.updateFavMovie(movie);
        // Update the movie list
        mUpdatedMovieLiveData.setValue(movie);
//        getRemoteMovieList(); // Reload the data
    }


    public LiveData<List<Movie>> getFavoriteMovies() {
        if (mFavoriteMoviesLiveData == null) {
            mFavoriteMoviesLiveData = mGetFavMoviesUseCase.getFavoriteMovies();
        }
        return mFavoriteMoviesLiveData;
    }

    // Current Page in ViewPager2
    public LiveData<Integer> getCurrentPageLiveData() {
        return currentPageLiveData;
    }

    public void setCurrentPageLiveData(int page) {
        currentPageLiveData.setValue(page);
    }

    /**
     * Get the updated movie
     *
     * @return MutableLiveData<Movie>
     */
    public MutableLiveData<Movie> getUpdatedMovieLiveData() {
        return mUpdatedMovieLiveData;
    }

}
