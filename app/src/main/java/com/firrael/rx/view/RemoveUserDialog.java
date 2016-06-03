package com.firrael.rx.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firrael.rx.R;
import com.firrael.rx.Utils;
import com.firrael.rx.presenter.GroupCreatorPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Railag on 03.06.2016.
 */
public class RemoveUserDialog extends Dialog {

    @BindView(R.id.userSpinner)
    Spinner userSpinner;

    GroupCreatorPresenter presenter;

    List<ChatUser> users;

    protected RemoveUserDialog(Context context, GroupCreatorPresenter presenter, List<ChatUser> users) {
        super(context);
        this.presenter = presenter;
        this.users = users;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_remove_user, null);

        ButterKnife.bind(v, this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(v);

        getWindow().setLayout((int) Utils.dp2px(280, context), ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        List<String> logins = new ArrayList<>();
        for (ChatUser user : users) {
            logins.add(user.getLogin());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, logins);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        userSpinner.setAdapter(adapter);
    }

    @OnClick(R.id.removeButton)
    void remove() {
        String login = (String) userSpinner.getSelectedItem();
        if (!TextUtils.isEmpty(login)) {
            for (ChatUser user : users) {
                if (user.getLogin().equals(login)) {
                    presenter.removeUser(login);
                    dismiss();
                    break;
                }
            }
        }

    }
}