package com.pivot.flibo.ui.login;

import android.os.Bundle;

import com.androidnetworking.error.ANError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.pivot.flibo.R;
import com.pivot.flibo.data.DataManager;
import com.pivot.flibo.data.network.model.ApiRequest;
import com.pivot.flibo.data.network.model.ApiResponse;
import com.pivot.flibo.data.network.model.ApiToken;
import com.pivot.flibo.ui.base.BasePresenter;
import com.pivot.flibo.utils.AppLogger;
import com.pivot.flibo.utils.rx.SchedulerProvider;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    private static final String TAG = "LoginPresenter";

    @Inject
    public LoginPresenter(DataManager dataManager,
                          SchedulerProvider schedulerProvider,
                          CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onViewPrepared(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        sendGraphRequest(loginResult);
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        getMvpView().showMessage(exception.getMessage());
                    }
                });
    }

    private void sendGraphRequest(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject json, GraphResponse response) {
                if (response.getError() != null) {
                    getMvpView().showMessage(R.string.some_error);
                } else {
                    String id = json.optString("id");
                    String name = json.optString("name");
                    String email = json.optString("email");
                    String profilePic = "https://graph.facebook.com/v5.0/"+id+"/picture?height=400&width=400";
                    updateFacebookDetails(id, name, email, profilePic);
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void updateFacebookDetails(String id, String name, String email, String profilePic){
        ApiRequest request = new ApiRequest();
        request.setEmail(email != null ? email : "");
        request.setName(name);
        request.setPicture(profilePic != null ? StringEscapeUtils.unescapeJava(profilePic) : "");
        request.setProvider("Facebook");
        request.setProviderId(id);
        ApiToken token = new ApiToken();
        token.setAccess(AccessToken.getCurrentAccessToken().getToken());
        request.setToken(token);

        doSignIn(request);
    }

    @Override
    public void updateGoogleAccountDetails(GoogleSignInAccount account) {
        ApiRequest request = new ApiRequest();
        request.setEmail(account.getEmail());
        request.setName(account.getDisplayName());
        request.setPicture(account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "");
        request.setProvider("Google");
        request.setProviderId(account.getId());
        ApiToken token = new ApiToken();
        // null
        token.setAccess("");
        request.setToken(token);

        doSignIn(request);
    }

    private void doSignIn(ApiRequest request){
        getMvpView().showLoading();

        getCompositeDisposable().add(getDataManager()
                .doLoginApiCall(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ApiResponse>() {
                    @Override
                    public void accept(ApiResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getDataManager().updateUserInfo(response.getSessionId(), DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE);
                        getMvpView().hideLoading();
                        getMvpView().openMainActivity(response.getSessionId());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (throwable instanceof ANError) {
                            ANError anError = (ANError) throwable;
                            handleApiError(anError);
                        }
                        getMvpView().hideLoading();
                    }
                }));
    }
}