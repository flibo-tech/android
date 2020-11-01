package com.pivot.flibo.ui.main;

import android.webkit.WebView;

import com.pivot.flibo.ui.base.MvpPresenter;

public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {

    void onViewPrepared(WebView webView, String url, String redirectUrl);

    boolean canGoBack();

    void goBack();

}