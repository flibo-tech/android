package com.pivot.flibo.data;

import android.content.Context;
import com.pivot.flibo.data.network.ApiHeader;
import com.pivot.flibo.data.network.ApiHelper;
import com.pivot.flibo.data.network.model.ApiRequest;
import com.pivot.flibo.data.network.model.ApiResponse;
import com.pivot.flibo.data.prefs.PreferencesHelper;
import com.pivot.flibo.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Single;

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          PreferencesHelper preferencesHelper,
                          ApiHelper apiHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mApiHelper = apiHelper;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
        mApiHelper.getApiHeader().getProtectedApiHeader().setAccessToken(accessToken);
    }

    @Override
    public Single<ApiResponse> doLoginApiCall(ApiRequest request) {
        return mApiHelper.doLoginApiCall(request);
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode mode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(mode);
    }

    @Override
    public String getCurrentUserId() {
        return mPreferencesHelper.getCurrentUserId();
    }

    @Override
    public void setCurrentUserId(String userId) {
        mPreferencesHelper.setCurrentUserId(userId);
    }

    @Override
    public void updateApiHeader(String userId, String accessToken) {
        mApiHelper.getApiHeader().getProtectedApiHeader().setUserId(userId);
        mApiHelper.getApiHeader().getProtectedApiHeader().setAccessToken(accessToken);
    }

    @Override
    public void updateUserInfo(
            String userId,
            LoggedInMode loggedInMode) {

        setCurrentUserId(userId);
        setCurrentUserLoggedInMode(loggedInMode);
    }

    @Override
    public void setUserAsLoggedOut() {
        updateUserInfo(
                null,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT);
    }
}
