package ojt.aada.mockproject.di;

import javax.inject.Singleton;

import dagger.Component;
import ojt.aada.mockproject.ui.main.MainActivity;
import ojt.aada.mockproject.ui.movie.detail.MovieDetailFragment;
import ojt.aada.mockproject.ui.movie.favoritelist.FavoriteListFragment;
import ojt.aada.mockproject.ui.movie.list.MovieListFragment;

@Singleton
@Component(modules = {NetworkModule.class, AppModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(MovieListFragment fragment);
    void inject(FavoriteListFragment fragment);
    void inject(MovieDetailFragment movieDetailFragment);
}
