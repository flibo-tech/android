package com.pivot.flibo.ui.login;

import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.pivot.flibo.ui.base.MvpPresenter;

public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {

    void onViewPrepared(CallbackManager callbackManager);

    void updateGoogleAccountDetails(GoogleSignInAccount account);
}