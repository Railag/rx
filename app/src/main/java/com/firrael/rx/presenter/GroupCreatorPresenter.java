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
                () -> service.addUser(addLogin, groupId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                GroupCreatorFragment::onSuccessAddUser,
                GroupCreatorFragment::onErrorAddUser);

        restartableLatestCache(REQUEST_REMOVE_USER,
                () -> service.removeUser(removeLogin)
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
    }

    public void sendMessage(String message, long groupId, long userId) {
        this.message = message;
        this.groupId = groupId;
        this.userId = userId;
        start(REQUEST_SEND_MESSAGE_CREATOR);
    }

    public void fetchMessages(long groupId) {
        this.groupId = groupId;
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

    public void fetchUsers(long groupId) {
        this.groupId = groupId;
        start(REQUEST_FETCH_USERS);
    }
}