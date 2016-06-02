package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.Requests;
import com.firrael.rx.model.LoginResult;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.view.LoginFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_LOGIN;

/**
 * Created by Railag on 31.05.2016.
 */
public class LoginPresenter extends BasePresenter<LoginFragment> {

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

