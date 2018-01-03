package com.jasen.kimjaeseung.homebase;

import android.app.Application;

/**
 * Created by kimjaeseung on 2018. 1. 3..
 */

public class GlobalApp extends Application {
    private static final String TAG = GlobalApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
