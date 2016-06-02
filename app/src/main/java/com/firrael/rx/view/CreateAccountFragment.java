package com.firrael.rx.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firrael.rx.R;
import com.firrael.rx.model.CreateAccountResult;
import com.firrael.rx.presenter.CreateAccountPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 01.06.2016.
 */
@RequiresPresenter(CreateAccountPresenter.class)
public class CreateAccountFragment extends BaseFragment<CreateAccountPresenter> {

    @BindView(R.id.loginField)
    EditText loginField;
    @BindView(R.id.emailField)
    EditText emailField;
    @BindView(R.id.passwordField)
    EditText passwordField;
    @BindView(R.id.repeatPasswordField)
    EditText repeatPasswordField;

    @BindView(R.id.createAccountButton)
    Button createAccount;

    public static CreateAccountFragment newInstance() {

        Bundle args = new Bundle();

        CreateAccountFragment fragment = new CreateAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View v) {
        loginField.addTextChangedListener(inputListener);
        emailField.addTextChangedListener(inputListener);
        passwordField.addTextChangedListener(inputListener);
        repeatPasswordField.addTextChangedListener(inputListener);
    }

    private TextWatcher inputListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            validate();
        }
    };

    private void validate() {
        String login = loginField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String repeatPassword = repeatPasswordField.getText().toString();

        boolean isValid = true;

        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)) {
            //    toast(getString(R.string.emptyFieldError));
            isValid = false;
        }

        if (!password.equals(repeatPassword)) {
            //    toast(getString(R.string.passwordRepeatPasswordError)); // TODO validation
            isValid = false;
        }

        if (isValid) {
            createAccount.setEnabled(true);
        } else {
            createAccount.setEnabled(false);
        }
    }

    @OnClick(R.id.createAccountButton)
    public void create() {
        startLoading();

        String login = loginField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        getPresenter().request(login, email, password);
    }

    public void onSuccess(CreateAccountResult result) {
        toast("success create account");
        stopLoading();
        getMainActivity().updateNavigationMenu();
        getMainActivity().toUserLandingScreen();
    }

    public void onError(Throwable error) {
        error.printStackTrace();
        stopLoading();
        toast(error.getMessage());
    }

    @Override
    protected String getTitle() {
        return getString(R.string.createAccount);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_create_account;
    }
}