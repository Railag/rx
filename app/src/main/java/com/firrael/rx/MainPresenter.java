package com.firrael.rx;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.List;

import icepick.State;
import io.realm.Realm;
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

        TestService service = App.createRetrofitService(TestService.class);

        /*restartableLatestCache(REQUEST_ITEMS, () ->
                        service.getPosts()
                                //    .filter(response -> response.get("code").getAsInt() == 0)
                                //    .map(jsonObject1 -> jsonObject1.get("data"))
                                .subscribeOn(Schedulers.newThread()),
                (activity, response) -> activity.onItems(response),
                (activity, throwable) -> activity.onItemsError(throwable));
        */
        restartableLatestCache(REQUEST_ITEMS,
                () -> service.getPosts()
                        .doOnNext(this::save)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                MainActivity::onItems,
                MainActivity::onItemsError);

        if (savedState == null)
            start(REQUEST_ITEMS);
    }

    private void save(List<Post> posts) {
        Realm realm = App.realm();

        realm.beginTransaction();

        realm.copyToRealm(posts);

        realm.commitTransaction();
    }

    public void request(String name) {
        this.name = name;
        start(REQUEST_ITEMS);
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