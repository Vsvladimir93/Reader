package com.vsv.vova.androidreader;

import android.content.Context;

import io.realm.Realm;

public class ReaderRealm {
    private static ReaderRealm readerRealm = new ReaderRealm();
    private Realm realm;

    private ReaderRealm() {}

    public static ReaderRealm getInstance() {
        return readerRealm;
    }

    public static void init(Context context) {
        Realm.init(context);
        getInstance().realm = Realm.getDefaultInstance();
    }

    public static Realm getRealm() {
        return getInstance().realm;
    }
}
