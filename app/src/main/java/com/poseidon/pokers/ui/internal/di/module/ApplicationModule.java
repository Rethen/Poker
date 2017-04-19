package com.poseidon.pokers.ui.internal.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.poseidon.pokers.UIThread;
import com.poseidon.pokers.data.executor.JobExecutor;
import com.poseidon.pokers.data.net.HttpApiManager;
import com.poseidon.pokers.data.repository.LoginDataRespositroy;
import com.poseidon.pokers.domain.executor.PostExecutionThread;
import com.poseidon.pokers.domain.executor.ThreadExecutor;
import com.poseidon.pokers.domain.repository.LoginRespositroy;
import com.poseidon.pokers.ui.application.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 42524 on 2017/4/12.
 */
@Module
public class ApplicationModule {

    private final App application;

    public ApplicationModule(App application) {
        this.application = application;
    }


    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    LoginRespositroy provideLoginRespository(LoginDataRespositroy respositroy){
        return respositroy;
    }

}
