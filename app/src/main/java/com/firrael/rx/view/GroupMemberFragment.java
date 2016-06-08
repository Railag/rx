package com.firrael.rx.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.firrael.rx.ChatAdapter;
import com.firrael.rx.R;
import com.firrael.rx.model.Group;
import com.firrael.rx.model.Message;
import com.firrael.rx.model.SendMessageResult;
import com.firrael.rx.model.User;
import com.firrael.rx.presenter.GroupMemberPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 03.06.2016.
 */
@RequiresPresenter(GroupMemberPresenter.class)
public class GroupMemberFragment extends BaseFragment<GroupMemberPresenter> {

    private final static String KEY_GROUP = "group";

    private Group group;

    @BindView(R.id.chatList)
    RecyclerView chatList;
    @BindView(R.id.sendField)
    EditText sendField;

    ChatAdapter adapter;


    public static GroupMemberFragment newInstance(Group group) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_GROUP, group);

        GroupMemberFragment fragment = new GroupMemberFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View v) {
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(KEY_GROUP)) {
                group = args.getParcelable(KEY_GROUP);
                getMainActivity().setTitle(group.getTitle());
            }
        }

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        chatList.setLayoutManager(manager);
        adapter = new ChatAdapter();
        chatList.setAdapter(adapter);
    }

    @OnTextChanged(R.id.sendField)
    void onType() {
        // TODO send type events?
    }

    @OnClick(R.id.sendButton)
    void sendMessage() {
        String message = sendField.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            long userId = User.get(getActivity()).getId();
            getPresenter().sendMessage(message, group.getId(), userId);
        }
    }

    @Override
    protected String getTitle() {
        return "";
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_group_member;
    }

    public void onSuccess(SendMessageResult result) {
        // TODO fetch messages
    }

    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}