package com.firrael.rx.presenter;

import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.view.GroupCreatorFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_ADD_USER;
import static com.firrael.rx.Requests.REQUEST_FETCH_MESSAGES;
import static com.firrael.rx.Requests.REQUEST_FETCH_USERS;
import static com.firrael.rx.Requests.REQUEST_REMOVE_USER;
import static com.firrael.rx.Requests.REQUEST_SEND_MESSAGE_CREATOR;
import static com.firrael.rx.Requests.REQUEST_SEND_PN;
import static com.firrael.rx.Requests.REQUEST_START_CALL;

/**
 * Created by Railag on 03.06.2016.
 */
public class GroupCreatorPresenter extends BasePresenter<GroupCreatorFragment> {
    @State
    String message;
    @State
    long groupId;
    @State
    long userId;
    @State
    String addLogin;
    @State
    String removeLogin;

    @State
    String pnTitle;
    @State
    String pnText;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_SEND_MESSAGE_CREATOR,
                () -> service.sendMessage(groupId, userId, message)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                GroupCreatorFragment::onSuccessSendMessage,
                GroupCreatorFragment::onErrorSendMessage);

        restartableLatestCache(REQUEST_FETCH_MESSAGES,
                () -> service.fetchMessages(groupId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                GroupCreatorFragment::onSuccessFetchMessages,
                GroupCreatorFragment::onErrorFetchMessages);

        restartableLatestCache(REQUEST_ADD_USER,
                () -> service.addInviteUser(addLogin, groupId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                GroupCreatorFragment::onSuccessAddUser,
                GroupCreatorFragment::onErrorAddUser);

        restartableLatestCache(REQUEST_REMOVE_USER,
                () -> service.removeUser(removeLogin, groupId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                GroupCreatorFragment::onSuccessRemoveUser,
                GroupCreatorFragment::onErrorRemoveUser);

        restartableLatestCache(REQUEST_FETCH_USERS,
                () -> service.fetchUsers(groupId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                GroupCreatorFragment::onSuccessFetchUsers,
                GroupCreatorFragment::onErrorFetchUsers);

        restartableLatestCache(REQUEST_SEND_PN,
                () -> service.sendPNToGroup(groupId, pnTitle, pnText)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                GroupCreatorFragment::onSuccessSendPN,
                GroupCreatorFragment::onErrorSendPN);

        restartableLatestCache(REQUEST_START_CALL,
                () -> service.startCall(userId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                GroupCreatorFragment::onSuccessStartCall,
                GroupCreatorFragment::onErrorStartCall);
    }

    public void sendMessage(String message) {
        this.message = message;
        start(REQUEST_SEND_MESSAGE_CREATOR);
    }

    public void fetchMessages() {
        start(REQUEST_FETCH_MESSAGES);
    }

    public void addUser(String login) {
        this.addLogin = login;
        start(REQUEST_ADD_USER);
    }

    public void removeUser(String login) {
        this.removeLogin = login;
        start(REQUEST_REMOVE_USER);
    }

    public void fetchUsers() {
        start(REQUEST_FETCH_USERS);
    }

    public void sendPNToGroup(String title, String text) {
        this.pnTitle = title;
        this.pnText = text;
        start(REQUEST_SEND_PN);
    }

    public void startCall() {
        start(REQUEST_START_CALL);
    }

    public void initialize(long groupId, long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }
}