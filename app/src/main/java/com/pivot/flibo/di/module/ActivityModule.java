package com.pivot.flibo.di.module;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pivot.flibo.di.ActivityContext;
import com.pivot.flibo.di.PerActivity;
import com.pivot.flibo.ui.login.LoginMvpPresenter;
import com.pivot.flibo.ui.login.LoginMvpView;
import com.pivot.flibo.ui.login.LoginPresenter;
import com.pivot.flibo.ui.main.MainMvpPresenter;
import com.pivot.flibo.ui.main.MainMvpView;
import com.pivot.flibo.ui.main.MainPresenter;
import com.pivot.flibo.ui.splash.SplashMvpPresenter;
import com.pivot.flibo.ui.splash.SplashMvpView;
import com.pivot.flibo.ui.splash.SplashPresenter;
import com.pivot.flibo.utils.rx.AppSchedulerProvider;
import com.pivot.flibo.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(
            MainPresenter<MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(
            LoginPresenter<LoginMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(
            SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }
}
