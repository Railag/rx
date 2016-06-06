package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
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
    String token;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_SPLASH,
                () -> service.startupLogin(login, token)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                SplashFragment::onSuccess,
                SplashFragment::onError);
    }

    public void request(String login, String token) {
        this.login = login;
        this.token = token;
        start(REQUEST_SPLASH);
    }

   /* private void save(List<Post> posts) {
        Realm realm = App.realm();

        realm.beginTransaction();

        realm.copyToRealm(posts);

        realm.commitTransaction();
    }*/
}
