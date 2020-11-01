package com.pivot.flibo.ui.splash;

import android.net.Uri;
import android.widget.ImageView;

import com.pivot.flibo.ui.base.MvpPresenter;

public interface SplashMvpPresenter<V extends SplashMvpView> extends MvpPresenter<V> {

    void onViewAttached(ImageView logo, Uri data);

}