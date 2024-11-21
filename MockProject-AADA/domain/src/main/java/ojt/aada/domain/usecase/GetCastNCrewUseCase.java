package ojt.aada.domain.usecase;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Single;
import ojt.aada.domain.models.CastnCrew;
import ojt.aada.domain.models.Movie;
import ojt.aada.domain.repositories.RemoteCastNCrewRepository;

public class GetCastNCrewUseCase {
    private RemoteCastNCrewRepository remoteCastnCrewRepository;

    @Inject
    public GetCastNCrewUseCase(RemoteCastNCrewRepository remoteCastnCrewRepository) {
        this.remoteCastnCrewRepository = remoteCastnCrewRepository;
    }

    public Single<ArrayList<CastnCrew>> getCastNCrewFromAPI(Movie movie) {
        return remoteCastnCrewRepository.getCastNCrew(movie);
    }

}
