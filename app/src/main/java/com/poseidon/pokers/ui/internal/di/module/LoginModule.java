package com.poseidon.pokers.ui.internal.di.module;

import com.poseidon.pokers.domain.interactor.Login;
import com.poseidon.pokers.domain.interactor.UseCase;
import com.poseidon.pokers.ui.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 42524 on 2017/4/13.
 */
@Module
public class LoginModule {

    @Provides
    @PerActivity
    @Named("login")
    UseCase provideLoginUseCase(Login login) {
        return login;
    }
}
