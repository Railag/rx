package com.firrael.rx;

import android.os.Bundle;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Railag on 31.05.2016.
 */
public class SplashPresenter extends BasePresenter<SplashFragment> {

    private static final int REQUEST_SPLASH = 2;

    @State
    String login;

    @State
    String password; // TODO replace with Token

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_SPLASH,
                () -> service.login(login, password)
                        .doOnNext(this::save)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                SplashFragment::onSuccess,
                SplashFragment::onError);

        if (savedState == null)
            start(REQUEST_SPLASH);
    }

    private void save(LoginResult result) {
        // TODO save user info
    }

    public void request(String login, String password) {
        this.login = login;
        this.password = password;
        start(REQUEST_SPLASH);
    }

   /* private void save(List<Post> posts) {
        Realm realm = App.realm();

        realm.beginTransaction();

        realm.copyToRealm(posts);

        realm.commitTransaction();
    }*/
}
