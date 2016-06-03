package com.firrael.rx.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.firrael.rx.R;
import com.firrael.rx.Utils;
import com.firrael.rx.presenter.GroupCreatorPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Railag on 03.06.2016.
 */
public class AddUserDialog extends Dialog {

    @BindView(R.id.userField)
    EditText userField;

    GroupCreatorPresenter presenter;

    protected AddUserDialog(Context context, GroupCreatorPresenter presenter) {
        super(context);
        this.presenter = presenter;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_add_user, null);

        ButterKnife.bind(v, this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(v);

        getWindow().setLayout((int) Utils.dp2px(280, context), ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @OnClick(R.id.addButton)
    void add() {
        String login = userField.getText().toString();
        if (!TextUtils.isEmpty(login)) {
            presenter.addUser(login);
            dismiss();
        }
    }
}
