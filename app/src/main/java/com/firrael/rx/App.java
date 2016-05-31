package com.firrael.rx;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;
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

    private static Retrofit api;
    private static Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
    }

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

    public static Realm realm() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }

        return realm;
    }

    static <T> T createRetrofitService(final Class<T> clazz) {
        return api().create(clazz);
    }
}