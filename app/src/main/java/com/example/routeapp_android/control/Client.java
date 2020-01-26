package com.example.routeapp_android.control;


import com.example.routeapp_android.encryption.Encrypt;
import com.example.routeapp_android.model.Session;
import com.example.routeapp_android.model.User;


import java.sql.Time;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Client {
    private static final String SERVER_URL = "http://192.168.0.2:8080/RouteApp_Server/webresources/";
    private static String code;
    private ClientService service;

    public void setCode(String code) {
        this.code = code;
    }

    public Client() {
        code = "";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.callTimeout(3, TimeUnit.SECONDS);
        httpClient.connectTimeout(3, TimeUnit.SECONDS);
        httpClient.readTimeout(3, TimeUnit.SECONDS);
        httpClient.writeTimeout(3, TimeUnit.SECONDS);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        service = retrofit.create(ClientService.class);
    }

    public String getSessionCode() {
        String fullCode = code + Time.from(Instant.now()).getTime();
        return Encrypt.cifrarTexto(fullCode);
    }
    public void login(final CallbackReceiver callback, User loginData) throws Exception {
        try {
            loginData.setPassword(Encrypt.cifrarTexto(loginData.getPassword()));
            Call<Session> call =  service.login(loginData);

            call.enqueue(new Callback<Session>() {
                @Override
                public void onResponse(Call<Session> call, Response<Session> response) {
                    try {
                        if(response.isSuccessful()) {
                            setCode(response.body().getCode());
                            callback.onSuccess(response);
                        }  else {
                            callback.onError(new Exception ("Error trying to connect. HTTP code: " + response.code()));
                        }
                    }catch(Exception ex) {
                        Logger.getAnonymousLogger().severe(ex.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Call<Session> call, Throwable t) {
                    callback.onError(t);

                }
            });
        } catch (Exception ex) {
            Logger.getAnonymousLogger().severe(ex.getLocalizedMessage());
            //TODO
            throw ex;
        }
    }

    public void registerUser(final CallbackReceiver callback, User loginData) throws Exception {
        try {
            loginData.setPassword(Encrypt.cifrarTexto(loginData.getPassword()));
            Call<Void> call =  service.createUser(loginData);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        if(response.isSuccessful()) {
                            callback.onSuccess(response);
                        }  else {
                            callback.onError(new Exception ("Error trying to connect. HTTP code: " + response.code()));
                        }
                    }catch(Exception ex) {
                        Logger.getAnonymousLogger().severe(ex.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    callback.onError(t);

                }
            });
        } catch (Exception ex) {
            Logger.getAnonymousLogger().severe(ex.getLocalizedMessage());
            //TODO
            throw ex;
        }
    }
}
