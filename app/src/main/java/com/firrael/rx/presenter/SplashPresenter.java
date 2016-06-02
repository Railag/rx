package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.Requests;
import com.firrael.rx.model.LoginResult;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.view.SplashFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_SPLASH;

/**
 * Created by Railag on 31.05.2016.
 */
public class SplashPresenter extends BasePresenter<SplashFragment> {

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
