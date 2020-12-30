package com.pivot.flibo.ui.main;

import com.pivot.flibo.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void requestMicrophonePermission();

    void shareProfile(String imageUrl, String profileUrl);

    String getStringById(int id);

    void logout();

    void openLoginActivity();

    void startBrowser(String url);

}