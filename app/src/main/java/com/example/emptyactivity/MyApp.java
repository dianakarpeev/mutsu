package com.example.emptyactivity;

import android.app.Application;

public class MyApp extends Application {
    /* Always be able to access the module ("static") */
    public static AppModule appModule;
    /* Called only once at beginning of application's lifetime */
    @Override
    public void onCreate() {
        super.onCreate();
        appModule = new AppModule(this);
    }
}
