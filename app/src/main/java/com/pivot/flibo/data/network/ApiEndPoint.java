package com.pivot.flibo.data.network;

import com.pivot.flibo.BuildConfig;

public final class ApiEndPoint {

    public static final String ENDPOINT_LOGIN = BuildConfig.BASE_URL
            + "/app_login";

    public static final String ENDPOINT_SAVE_NOTIFICATION = BuildConfig.BASE_APP_URL
            + "/save_notification_response";

    private ApiEndPoint() {

    }

}
