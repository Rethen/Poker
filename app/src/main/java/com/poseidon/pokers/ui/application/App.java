package com.poseidon.pokers.ui.application;

import android.support.multidex.MultiDexApplication;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.poseidon.pokers.BuildConfig;
import com.poseidon.pokers.ui.internal.di.component.ApplicationComponent;
import com.poseidon.pokers.ui.internal.di.component.DaggerApplicationComponent;
import com.poseidon.pokers.ui.internal.di.module.ApplicationModule;

/**
 * Created by 42524 on 2017/4/7.
 */

public class App extends MultiDexApplication {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
