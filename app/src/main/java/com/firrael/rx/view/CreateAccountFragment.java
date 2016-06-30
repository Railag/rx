package com.firrael.rx.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firrael.rx.App;
import com.firrael.rx.R;
import com.firrael.rx.RConnectorService;
import com.firrael.rx.model.SendFCMTokenResult;
import com.firrael.rx.model.User;
import com.firrael.rx.model.UserResult;
import com.firrael.rx.presenter.CreateAccountPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Railag on 01.06.2016.
 */
@RequiresPresenter(CreateAccountPresenter.class)
public class CreateAccountFragment extends BaseFragment<CreateAccountPresenter> {
    private final static String TAG = CreateAccountFragment.class.getSimpleName();

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
        User.save(result, getActivity());
        User user = User.get(getActivity());
        String fcmToken = user.getFcmToken(getActivity());
        if (!TextUtils.isEmpty(fcmToken)) {
            RConnectorService service = App.restService();

            service.sendFCMToken(user.getId(), fcmToken)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onFcmTokenSuccess, this::onFcmTokenError);
        }

        toast("success create account");
        getMainActivity().updateNavigationMenu();
        getMainActivity().toUserLandingScreen();
    }

    public void onError(Throwable error) {
        error.printStackTrace();
        stopLoading();
        toast(error.getMessage());
    }

    public void onFcmTokenSuccess(SendFCMTokenResult result) {
        if (result.invalid()) {
            Log.e(TAG, result.error);
            return;
        }

        Log.i(TAG, result.result);

        User.fcmSaved(getActivity());
    }

    public void onFcmTokenError(Throwable throwable) {
        throwable.printStackTrace();
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