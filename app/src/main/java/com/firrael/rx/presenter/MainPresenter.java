package com.firrael.rx.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.firrael.rx.App;
import com.firrael.rx.Requests;
import com.firrael.rx.view.MainActivity;
import com.firrael.rx.RConnectorService;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.rx.Requests.REQUEST_SAVE_IMAGE;

/**
 * Created by Railag on 02.06.2016.
 */
public class MainPresenter extends BasePresenter<MainActivity> {

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
    }

    public void request(Bitmap image) {
        this.image = image;
        start(REQUEST_SAVE_IMAGE);
    }
}
