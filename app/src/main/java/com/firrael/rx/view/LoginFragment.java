package com.firrael.rx.view;

import android.os.Bundle;
import android.widget.EditText;

import com.firrael.rx.R;
import com.firrael.rx.Utils;
import com.firrael.rx.model.User;
import com.firrael.rx.model.UserResult;
import com.firrael.rx.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 31.05.2016.
 */
@RequiresPresenter(LoginPresenter.class)
public class LoginFragment extends BaseFragment<LoginPresenter> {

    @BindView(R.id.loginField)
    EditText loginField;
    @BindView(R.id.passwordField)
    EditText passwordField;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.login);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_login;
    }

    @OnClick(R.id.loginButton)
    public void login() {
        Utils.hideKeyboard(getActivity());
        startLoading();
        getPresenter().request(loginField.getText().toString(), passwordField.getText().toString());
    }

    @OnClick(R.id.createAccountButton)
    public void createAccount() {
        getMainActivity().toCreateAccount();
    }

    public void onSuccess(UserResult result) {
        stopLoading();
        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            toast(result.error);
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
        toast(error.getMessage());
    }
}
