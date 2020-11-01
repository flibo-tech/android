package com.pivot.flibo.ui.splash;

import com.pivot.flibo.ui.base.MvpView;

public interface SplashMvpView extends MvpView {

    void openLoginActivity(String url);

    void openMainActivity(String url);
}