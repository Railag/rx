package com.firrael.rx.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firrael.rx.R;
import com.firrael.rx.model.User;
import com.firrael.rx.presenter.UserLandingPresenter;

import butterknife.BindView;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 01.06.2016.
 */
@RequiresPresenter(UserLandingPresenter.class)
public class UserLandingFragment extends BaseFragment<UserLandingPresenter> { // TODO view / edit modes, send update request to refresh

    @BindView(R.id.loginText)
    TextView loginText;
    @BindView(R.id.emailText)
    TextView emailText;

    @Override
    protected void initView(View v) {
        User user = User.get();
        loginText.setText(user.getLogin());
        emailText.setText(user.getEmail());
    }

    public static UserLandingFragment newInstance() {

        Bundle args = new Bundle();

        UserLandingFragment fragment = new UserLandingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.userInfo);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_user_landing;
    }
}