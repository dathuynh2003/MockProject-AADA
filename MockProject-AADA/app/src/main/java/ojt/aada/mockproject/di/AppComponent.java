package ojt.aada.mockproject.di;

import javax.inject.Singleton;

import dagger.Component;
import ojt.aada.mockproject.ui.container.ContainerFragment;
import ojt.aada.mockproject.ui.MainActivity;
import ojt.aada.mockproject.ui.main.MainFragment;
import ojt.aada.mockproject.ui.movie.detail.MovieDetailFragment;
import ojt.aada.mockproject.ui.movie.favoritelist.FavoriteListFragment;
import ojt.aada.mockproject.ui.movie.list.MovieListFragment;
import ojt.aada.mockproject.ui.profile.ProfileFragment;
import ojt.aada.mockproject.ui.reminder.ReminderListFragment;
import ojt.aada.mockproject.ui.settings.SettingsFragment;
import ojt.aada.mockproject.workers.ReminderWorker;

@Singleton
@Component(modules = {NetworkModule.class, AppModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(MovieListFragment fragment);
    void inject(FavoriteListFragment fragment);
    void inject(MovieDetailFragment movieDetailFragment);
    void inject(ContainerFragment containerFragment);
    void inject(MainFragment mainFragment);
    void inject(ProfileFragment profileFragment);
    void inject(ReminderListFragment reminderListFragment);
    void inject(ReminderWorker reminderWorker);
    void inject(SettingsFragment settingsFragment);
}
