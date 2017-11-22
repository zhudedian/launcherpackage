package com.ider.launcherpackage.launcher;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by ider-eric on 2016/12/19.
 */

public class LauncherApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LitePalApplication.initialize(mContext);
    }

    public static Context getContext() {
        return mContext;
    }

}
