package com.example.routeapp_android.control;

import retrofit2.Response;

public interface CallbackReceiver {
    void onSuccess(Response response);
    void onError(Throwable t);
}
