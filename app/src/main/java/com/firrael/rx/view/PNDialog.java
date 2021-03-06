package com.firrael.rx.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.firrael.rx.R;
import com.firrael.rx.Utils;
import com.firrael.rx.presenter.GroupCreatorPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Railag on 10.06.2016.
 */
public class PNDialog extends Dialog {

    @BindView(R.id.pnTitleField)
    EditText titleField;
    @BindView(R.id.pnTextField)
    EditText textField;
    @BindView(R.id.pnImage)
    ImageView image;

    GroupCreatorPresenter presenter;

    protected PNDialog(Context context, GroupCreatorPresenter presenter) {
        super(context);
        this.presenter = presenter;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_pn, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(v);

        ButterKnife.bind(this, v);

        getWindow().setLayout((int) Utils.dp2px(280, context), ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    }

    @OnClick(R.id.pnPickImageButton)
    void pickImage() {
        // TODO pick image from gallery and set to imageview and save to mediastore and send in request
    }

    @OnClick(R.id.pnSendButton)
    void send() {
        String title = titleField.getText().toString();
        String text = textField.getText().toString();
        // TODO send image, send file
        if (!TextUtils.isEmpty(title)) {
            presenter.sendPNToGroup(title, text);
            dismiss();
        }
    }

}