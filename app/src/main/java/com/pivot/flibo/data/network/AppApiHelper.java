package com.pivot.flibo.data.network;

import com.pivot.flibo.data.network.model.ApiRequest;
import com.pivot.flibo.data.network.model.ApiResponse;
import com.pivot.flibo.data.network.model.notification.Detail;
import com.pivot.flibo.data.network.model.notification.DetailResponse;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Single;

@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;

    @Inject
    public AppApiHelper(ApiHeader apiHeader) {
        mApiHeader = apiHeader;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }

    @Override
    public Single<ApiResponse> doLoginApiCall(ApiRequest request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_LOGIN)
                .addApplicationJsonBody(request)
                .build()
                .getObjectSingle(ApiResponse.class);
    }

    @Override
    public Single<DetailResponse> doSaveNotificationResponseApiCall(Detail request) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_SAVE_NOTIFICATION)
                .addApplicationJsonBody(request)
                .build()
                .getObjectSingle(DetailResponse.class);
    }
}

