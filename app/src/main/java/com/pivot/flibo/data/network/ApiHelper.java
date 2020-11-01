package com.pivot.flibo.data.network;

import com.pivot.flibo.data.network.model.ApiRequest;
import com.pivot.flibo.data.network.model.ApiResponse;
import com.pivot.flibo.data.network.model.notification.Detail;
import com.pivot.flibo.data.network.model.notification.DetailResponse;

import io.reactivex.Single;

public interface ApiHelper {

    ApiHeader getApiHeader();

    Single<ApiResponse> doLoginApiCall(ApiRequest request);

    Single<DetailResponse> doSaveNotificationResponseApiCall(Detail request);

}
