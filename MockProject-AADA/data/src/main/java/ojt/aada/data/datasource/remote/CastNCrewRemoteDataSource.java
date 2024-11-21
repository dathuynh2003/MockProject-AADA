package ojt.aada.data.datasource.remote;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ojt.aada.data.datasource.remote.response.CastNCrewResponse;
import ojt.aada.domain.models.CastnCrew;
import ojt.aada.domain.models.Movie;

public class CastNCrewRemoteDataSource {
    private MovieRetrofitAPI mMovieRetrofitAPI;
    @Inject
    public CastNCrewRemoteDataSource(MovieRetrofitAPI movieRetrofitAPI) {
        mMovieRetrofitAPI = movieRetrofitAPI;
    }


    public Single<ArrayList<CastnCrew>> getCastNCrew(Movie movie) {
        return mMovieRetrofitAPI.getCastNCrew(movie.getId())
                .subscribeOn(Schedulers.io())
                .map(CastNCrewResponse::getCastList);
    }
}
