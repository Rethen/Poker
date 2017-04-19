package com.poseidon.pokers.ui.internal.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.poseidon.pokers.data.net.HttpApiManager;
import com.poseidon.pokers.data.repository.LoginDataRespositroy;
import com.poseidon.pokers.domain.executor.PostExecutionThread;
import com.poseidon.pokers.domain.executor.ThreadExecutor;
import com.poseidon.pokers.domain.repository.LoginRespositroy;
import com.poseidon.pokers.ui.internal.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 42524 on 2017/4/12.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context context();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    LoginRespositroy loginRespository();

    Gson gson();

}
