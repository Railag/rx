package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.view.WebrtcActivity;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_INVITE_TO_CALL;

/**
 * Created by Railag on 15.06.2016.
 */
public class WebrtcPresenter extends BasePresenter<WebrtcActivity> {

    @State
    long userId;

    @State
    String socketAddress;

    @State
    String callId;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_INVITE_TO_CALL,
                () -> service.inviteToCall(userId, socketAddress, callId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                WebrtcActivity::onSuccessInviteCall,
                WebrtcActivity::onErrorInviteCall);
    }


    public void inviteToCall(long userId, String socketAddress, String callId) {
        this.userId = userId;
        this.socketAddress = socketAddress;
        this.callId = callId;
        start(REQUEST_INVITE_TO_CALL);
    }
}