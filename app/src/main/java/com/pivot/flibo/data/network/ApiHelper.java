package com.pivot.flibo.data.network;

import com.pivot.flibo.data.network.model.ApiRequest;
import com.pivot.flibo.data.network.model.ApiResponse;

import io.reactivex.Single;

public interface ApiHelper {

    ApiHeader getApiHeader();

    Single<ApiResponse> doLoginApiCall(ApiRequest request);

}
