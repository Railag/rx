package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.view.MyGroupsFragment;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_MY_GROUPS;

/**
 * Created by Railag on 02.06.2016.
 */
public class MyGroupsPresenter extends BasePresenter<MyGroupsFragment> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_MY_GROUPS,
                () -> service.getGroups()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                MyGroupsFragment::onSuccess,
                MyGroupsFragment::onError);

        if (savedState != null)
            start(REQUEST_MY_GROUPS);
    }

    public void request() {
        start(REQUEST_MY_GROUPS);
    }
}
