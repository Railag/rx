package com.firrael.rx.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.view.MainActivity;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_PN_ADD_USER;
import static com.firrael.rx.Requests.REQUEST_SAVE_IMAGE;

/**
 * Created by Railag on 02.06.2016.
 */
public class MainPresenter extends BasePresenter<MainActivity> {

    @State
    Bitmap image;

    @State
    long userId;

    @State
    long groupId;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_SAVE_IMAGE,
                () -> service.saveImage(image)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                MainActivity::onSuccessSaveImage,
                MainActivity::onErrorSaveImage);

        restartableLatestCache(REQUEST_PN_ADD_USER,
                () -> service.addUser(userId, groupId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                MainActivity::onSuccessAddUser,
                MainActivity::onErrorAddUser);
    }

    public void saveImage(Bitmap image) {
        this.image = image;
        start(REQUEST_SAVE_IMAGE);
    }

    public void addToGroup(long userId, long groupId) {
        this.userId = userId;
        this.groupId = groupId;
        start(REQUEST_PN_ADD_USER);
    }
}
