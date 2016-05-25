package com.firrael.rx;

import android.app.Application;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by firrael on 25.05.2016.
 */
public class App extends Application {
    private final static String API_ENDPOINT = "http://jsonplaceholder.typicode.com";

    public static Retrofit api;

    public static Retrofit api() {
        if (api == null) {
            OkHttpClient client = new OkHttpClient();

            api = new Retrofit.Builder()
                    .baseUrl(API_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        return api;
    }
}