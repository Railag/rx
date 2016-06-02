package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.model.CreateAccountResult;
import com.firrael.rx.view.CreateAccountFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_CREATE_ACCOUNT;

/**
 * Created by Railag on 01.06.2016.
 */
public class CreateAccountPresenter extends BasePresenter<CreateAccountFragment> {
    @State
    String login;

    @State
    String email;

    @State
    String password;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_CREATE_ACCOUNT,
                () -> service.createAccount(login, email, password)
                        .doOnNext(this::save)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                CreateAccountFragment::onSuccess,
                CreateAccountFragment::onError);

        if (savedState != null)
            start(REQUEST_CREATE_ACCOUNT);
    }

    private void save(CreateAccountResult result) {
        // TODO save user info
    }

    public void request(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        start(REQUEST_CREATE_ACCOUNT);
    }


}
