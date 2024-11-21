package ojt.aada.mockproject_aada.di;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import ojt.aada.data.repositories.RemoteCastNCrewRepositoryImpl;
import ojt.aada.data.repositories.RemoteMovieRepositoryImpl;
import ojt.aada.domain.repositories.RemoteCastNCrewRepository;
import ojt.aada.domain.repositories.RemoteMovieRepository;

@Module
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    public Application provideApplication() {
        return application;
    }

    @Provides
    public RemoteMovieRepository provideMovieRepository(RemoteMovieRepositoryImpl remoteMovieRepositoryImpl) {
        return remoteMovieRepositoryImpl;
    }

    @Provides
    public RemoteCastNCrewRepository provideCastNCrewRepository(RemoteCastNCrewRepositoryImpl remoteCastNCrewRepositoryImpl) {
        return remoteCastNCrewRepositoryImpl;
    }
}
