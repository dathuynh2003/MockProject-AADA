package ojt.aada.data.repositories;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Single;
import ojt.aada.data.datasource.remote.datasouces.CastNCrewRemoteDataSource;
import ojt.aada.domain.models.CastnCrew;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.RemoteCastNCrewRepository;

public class RemoteCastNCrewRepositoryImpl implements RemoteCastNCrewRepository {
    private CastNCrewRemoteDataSource mCastNCrewRemoteDataSource;

    @Inject
    public RemoteCastNCrewRepositoryImpl(CastNCrewRemoteDataSource castnCrewRemoteDataSource) {
        mCastNCrewRemoteDataSource = castnCrewRemoteDataSource;
    }

    @Override
    public Single<ArrayList<CastnCrew>> getCastNCrew(Movie movie) {
        return mCastNCrewRemoteDataSource.getCastNCrew(movie);
    }
}
