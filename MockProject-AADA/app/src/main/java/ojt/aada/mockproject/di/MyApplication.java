package ojt.aada.mockproject.di;

import android.app.Application;

public class MyApplication extends Application {
    public AppComponent appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();
}
