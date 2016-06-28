package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
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
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                LoginFragment::onSuccess,
                LoginFragment::onError);
    }

    public void request(String login, String password) {
        this.login = login;
        this.password = password;
        start(REQUEST_LOGIN);
    }
}

