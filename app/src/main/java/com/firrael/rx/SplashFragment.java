package com.firrael.rx;

import android.os.Bundle;

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
            getPresenter().request("test1", "test2");
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

    public void onSuccess(LoginResult result) {
        toast("success login");
        stopLoading();
        getMainActivity().updateNavigationMenu();
        getMainActivity().toUserLandingScreen();
    }

    public void onError(Throwable error) {
        error.printStackTrace();
        stopLoading();
        getMainActivity().toLogin();
    }
}