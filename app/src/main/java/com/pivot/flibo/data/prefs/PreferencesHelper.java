package com.pivot.flibo.data.prefs;

import com.pivot.flibo.data.DataManager;

public interface PreferencesHelper {

    int getCurrentUserLoggedInMode();

    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);

    String getCurrentUserId();

    void setCurrentUserId(String userId);

    String getAccessToken();

    void setAccessToken(String accessToken);

}
