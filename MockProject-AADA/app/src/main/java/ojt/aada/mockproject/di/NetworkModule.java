package ojt.aada.mockproject.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ojt.aada.data.datasource.remote.MovieRetrofitAPI;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    private static final String BASE_URL = "https://api.themoviedb.org/";

    @Singleton
    @Provides
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public MovieRetrofitAPI provideMovieRetrofitAPI(Retrofit retrofit) {
        return retrofit.create(MovieRetrofitAPI.class);
    }
}
