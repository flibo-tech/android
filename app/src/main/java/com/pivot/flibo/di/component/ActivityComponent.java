package com.pivot.flibo.di.component;

import com.pivot.flibo.di.PerActivity;
import com.pivot.flibo.di.module.ActivityModule;
import com.pivot.flibo.ui.login.LoginActivity;
import com.pivot.flibo.ui.main.MainActivity;
import com.pivot.flibo.ui.splash.SplashActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(LoginActivity activity);

    void inject(SplashActivity activity);

}
