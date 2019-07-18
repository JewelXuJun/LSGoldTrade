package com.jme.common.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by XuJun on 2018/11/7.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;
    private static Context context;

    {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

}
