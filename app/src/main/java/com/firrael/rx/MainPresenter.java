package com.firrael.rx;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import java.io.IOException;

import icepick.State;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by firrael on 24.05.2016.
 */
public class MainPresenter extends BasePresenter<MainActivity> {

    private static final int REQUEST_ITEMS = 1;

    @State
    String name;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        /*restartableLatestCache(REQUEST_ITEMS,
                () -> App.getServerAPI()
                        .getItems(name)
                        .observeOn(AndroidSchedulers.mainThread()),
                (activity, response) -> activity.onItems(response.items),
                (activity, throwable) -> activity.onItemsError(throwable));
        */


    }

    interface IPService {
        String END = "http://ip.taobao.com";

        @GET("/service/getIpInfo.php")
        Observable<JsonObject> getIPInfo(@Query("ip") String ip);
    }


    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            System.out.println(
                    String.format("Sending request %s on %s%n%s", request.url(), chain.connection(),
                            request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            System.out.println(
                    String.format("Received response for %s in %.1fms%n%s", response.request().url(),
                            (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }

    public void request(String name) {
        this.name = name;
        //    start(REQUEST_ITEMS);
        OkHttpClient client = new OkHttpClient();
        //    client.interceptors().add(new LoggingInterceptor());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(IPService.END).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        retrofit.create(IPService.class)
                .getIPInfo("58.19.239.11")
                .filter(jsonObject -> jsonObject.get("code").getAsInt() == 0)
                .map(jsonObject1 -> jsonObject1.get("data"))
                .subscribeOn(Schedulers.newThread())
                .subscribe(System.out::println);
    }

    @Override
    protected void onSave(@NonNull Bundle state) {
        super.onSave(state);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onTakeView(MainActivity mainActivity) {
        super.onTakeView(mainActivity);
    }

    @Override
    protected void onDropView() {
        super.onDropView();
    }
}