package com.example.goldsignalpro.utils;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {


    private static final String API_BASE_URL = "https://goldsignalpro.com/";
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();
    public static Retrofit adapter = null;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//            .callTimeout(2, TimeUnit.MINUTES)
//            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS);


   /* private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient);

    private static Retrofit retrofit = builder.build();*/

    public static <S> S createService(Context context, Class<S> serviceClass) {
        httpClient.addInterceptor(addLoggin());
        adapter = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        //   }


        return adapter.create(serviceClass);
    }

    // To have a verbose log
    private static HttpLoggingInterceptor addLoggin() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }


}


