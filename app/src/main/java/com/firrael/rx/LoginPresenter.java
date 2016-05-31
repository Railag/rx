package com.firrael.rx;

import android.os.Bundle;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Railag on 31.05.2016.
 */
public class LoginPresenter extends BasePresenter<LoginFragment> {

    private static final int REQUEST_LOGIN = 3;

    @State
    String login;

    @State
    String password; // TODO replace with Token

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_LOGIN,
                () -> service.login(login, password)
                        .doOnNext(this::save)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                LoginFragment::onSuccess,
                LoginFragment::onError);

        if (savedState == null)
            start(REQUEST_LOGIN);
    }

    private void save(LoginResult result) {
        // TODO save user info
    }

    public void request(String login, String password) {
        this.login = login;
        this.password = password;
        start(REQUEST_LOGIN);
    }
}

