package com.vsv.vova.androidreader;

import android.app.Application;

import com.vsv.vova.androidreader.Realm.ReaderRealm;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ReaderRealm.init(this);
    }
}
