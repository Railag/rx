package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.Requests;
import com.firrael.rx.view.NewGroupFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_CREATE_GROUP;

/**
 * Created by Railag on 02.06.2016.
 */
public class NewGroupPresenter extends BasePresenter<NewGroupFragment> {

    @State
    String groupName;

    @State
    long creatorId;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_CREATE_GROUP,
                () -> service.createGroup(groupName, creatorId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                NewGroupFragment::onSuccess,
                NewGroupFragment::onError);
    }

    public void request(String groupName, long creatorId) {
        this.groupName = groupName;
        this.creatorId = creatorId;
        start(REQUEST_CREATE_GROUP);
    }
}