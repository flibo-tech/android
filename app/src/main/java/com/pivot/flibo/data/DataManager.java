package com.pivot.flibo.data;

import com.pivot.flibo.data.network.ApiHelper;
import com.pivot.flibo.data.prefs.PreferencesHelper;

public interface DataManager extends PreferencesHelper, ApiHelper {

    void updateApiHeader(String userId, String accessToken);

    void setUserAsLoggedOut();

    void updateUserInfo(
            String userId,
            LoggedInMode loggedInMode);

    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }
    }
}
