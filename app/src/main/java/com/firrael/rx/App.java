package com.firrael.rx;

import android.app.Application;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            OkHttpClient client = httpClient.build();

            Gson gson = new Gson();

            api = new Retrofit.Builder()
                    .baseUrl(API_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        return api;
    }

    static <T> T createRetrofitService(final Class<T> clazz) {
        return api().create(clazz);
    }
}