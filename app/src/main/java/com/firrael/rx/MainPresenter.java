package com.firrael.rx;

import android.graphics.Bitmap;
import android.os.Bundle;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Railag on 02.06.2016.
 */
public class MainPresenter extends BasePresenter<MainActivity> {

    private static final int REQUEST_SAVE_IMAGE = 5;

    @State
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_SAVE_IMAGE,
                () -> service.saveImage(image)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                MainActivity::onSuccess,
                MainActivity::onError);

        if (savedState != null)
            start(REQUEST_SAVE_IMAGE);
    }

    public void request(Bitmap image) {
        this.image = image;
        start(REQUEST_SAVE_IMAGE);
    }
}
