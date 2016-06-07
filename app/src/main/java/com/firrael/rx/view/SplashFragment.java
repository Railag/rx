package com.firrael.rx.view;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.firrael.rx.R;
import com.firrael.rx.model.User;
import com.firrael.rx.model.UserResult;
import com.firrael.rx.presenter.SplashPresenter;

import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 31.05.2016.
 */
@RequiresPresenter(SplashPresenter.class)
public class SplashFragment extends BaseFragment<SplashPresenter> {

    public static SplashFragment newInstance() {

        Bundle args = new Bundle();

        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLoading();

        if (savedInstanceState == null) {
            startLoading();

            User user = User.get(getActivity());
            String token = user.getToken();

            if (!TextUtils.isEmpty(token))
                getPresenter().request(user.getLogin(), token);
            else {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    stopLoading();
                    getMainActivity().toLogin();
                }, 3000);
            }
        }
    }

    @Override
    protected String getTitle() {
        return getString(R.string.rconnector);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_splash;
    }

    public void onSuccess(UserResult result) {
        stopLoading();
        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            getMainActivity().toLogin();
            return;
        }
        toast("success login");
        User.save(result, getActivity());
        getMainActivity().updateNavigationMenu();
        getMainActivity().toUserLandingScreen();
    }

    public void onError(Throwable error) {
        error.printStackTrace();
        stopLoading();
        getMainActivity().toLogin();
    }
}