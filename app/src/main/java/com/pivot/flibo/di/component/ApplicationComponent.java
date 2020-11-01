package com.pivot.flibo.di.component;

import android.app.Application;
import android.content.Context;

import com.pivot.flibo.MvpApp;
import com.pivot.flibo.data.DataManager;
import com.pivot.flibo.di.ApplicationContext;
import com.pivot.flibo.di.module.ApplicationModule;
import com.pivot.flibo.service.SyncService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MvpApp app);

    void inject(SyncService service);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}