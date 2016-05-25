package com.firrael.rx;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.List;

import icepick.State;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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

    /*static class LoggingInterceptor implements Interceptor {
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
    }*/

    public void request(String name) {
        this.name = name;
        //    start(REQUEST_ITEMS);
        //    client.interceptors().add(new LoggingInterceptor());


        TestService service = App.api().create(TestService.class);

        /*restartableLatestCache(REQUEST_ITEMS, () ->
                        service.getPosts()
                                //    .filter(response -> response.get("code").getAsInt() == 0)
                                //    .map(jsonObject1 -> jsonObject1.get("data"))
                                .subscribeOn(Schedulers.newThread()),
                (activity, response) -> activity.onItems(response),
                (activity, throwable) -> activity.onItemsError(throwable));
        */
        service.getPosts()
                //    .filter(response -> response.get("code").getAsInt() == 0)
                //    .map(jsonObject1 -> jsonObject1.get("data"))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onItemsError(e);
                    }

                    @Override
                    public void onNext(List<JsonObject> jsonObjects) {
                        getView().onItems(jsonObjects);
                    }
                });
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