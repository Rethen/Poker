package com.poseidon.pokers.ui.internal.di.component;

import com.poseidon.pokers.ui.activity.login.LoginActivity;
import com.poseidon.pokers.ui.internal.di.PerActivity;
import com.poseidon.pokers.ui.internal.di.module.LoginModule;

import dagger.Component;

/**
 * Created by 42524 on 2017/4/13.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = LoginModule.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}
