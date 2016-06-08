package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.Requests;
import com.firrael.rx.view.GroupMemberFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_SEND_MESSAGE;

/**
 * Created by Railag on 03.06.2016.
 */
public class GroupMemberPresenter extends BasePresenter<GroupMemberFragment> {

    @State
    String message;
    @State
    long groupId;
    @State
    long userId;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(Requests.REQUEST_SEND_MESSAGE,
                () -> service.sendMessage(groupId, userId, message)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                GroupMemberFragment::onSuccess,
                GroupMemberFragment::onError);
    }

    public void sendMessage(String message, long groupId, long userId) {
        this.message = message;
        this.groupId = groupId;
        this.userId = userId;
        start(REQUEST_SEND_MESSAGE);
    }
}
