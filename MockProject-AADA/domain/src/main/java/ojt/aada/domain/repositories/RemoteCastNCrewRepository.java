package ojt.aada.domain.repositories;

import java.util.ArrayList;

import io.reactivex.Single;
import ojt.aada.domain.models.CastnCrew;
import ojt.aada.domain.models.Movie;

public interface RemoteCastNCrewRepository {
    Single<ArrayList<CastnCrew>> getCastNCrew(Movie movie);
}
