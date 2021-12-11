package cn.bigorange.wheel;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


public class MyApplication extends Application {

    private static MyApplication mMyApplication;

    public static MyApplication getInstance() {
        return mMyApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplication = this;

    }

}
