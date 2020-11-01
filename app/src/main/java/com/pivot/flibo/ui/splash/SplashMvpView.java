package com.pivot.flibo.ui.splash;

import android.os.Bundle;

import com.pivot.flibo.ui.base.MvpView;

public interface SplashMvpView extends MvpView {

    void openLoginActivity(String url);

    void openMainActivity(String url);

    void openMainActivity(String url, String redirectUrl);

    void checkUrl(String url);
}