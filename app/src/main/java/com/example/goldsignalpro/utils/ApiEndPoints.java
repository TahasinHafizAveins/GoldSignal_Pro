package com.example.goldsignalpro.utils;

import com.example.goldsignalpro.model.AppVersionModel;
import com.example.goldsignalpro.model.LatestSignal;
import com.example.goldsignalpro.model.SignalsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndPoints {
    @GET("/app-version.php")
    Call<AppVersionModel> getAppVersion();

    @GET("/api/signals")
    Call<SignalsModel> getSignals(@Query("page") String page);

    @GET("/api/signal/{id}")
    Call<SignalsModel.Data> getSignalDetails(@Path("id") String id);

    @GET("/api/latest-signal")
    Call<LatestSignal> getLatestSignal();
}
