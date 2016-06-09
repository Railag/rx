package com.firrael.rx.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.firrael.rx.ChatAdapter;
import com.firrael.rx.R;
import com.firrael.rx.model.AddUserResult;
import com.firrael.rx.model.Group;
import com.firrael.rx.model.Message;
import com.firrael.rx.model.RemoveUserResult;
import com.firrael.rx.model.SendMessageResult;
import com.firrael.rx.model.User;
import com.firrael.rx.presenter.GroupCreatorPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 03.06.2016.
 */
@RequiresPresenter(GroupCreatorPresenter.class)
public class GroupCreatorFragment extends BaseFragment<GroupCreatorPresenter> {

    private final static String KEY_GROUP = "group";

    private Group group;

    @BindView(R.id.chatList)
    RecyclerView chatList;
    @BindView(R.id.sendField)
    EditText sendField;
    @BindViews({R.id.addUserButton, R.id.removeUserButton, R.id.sendPNButton, R.id.editGroupButton})
    View[] viewsToCollapse;


    ChatAdapter adapter;

    List<ChatUser> groupUsers;

    public static GroupCreatorFragment newInstance(Group group) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_GROUP, group);

        GroupCreatorFragment fragment = new GroupCreatorFragment();
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

        getPresenter().fetchUsers(group.getId());
        getPresenter().fetchMessages(group.getId());
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

    @OnClick(R.id.addUserButton)
    void addUser() {
        AddUserDialog dialog = new AddUserDialog(getActivity(), getPresenter());
        dialog.show();
    }

    @OnClick(R.id.removeUserButton)
    void removeUser() {
        RemoveUserDialog dialog = new RemoveUserDialog(getActivity(), getPresenter(), groupUsers);
        dialog.show();
    }

    @OnClick(R.id.editGroupButton)
    void editGroup() {
        // TODO out of scope (edit group name, group image dialog)
    }

    @OnClick(R.id.sendPNButton)
    void sendPN() {
        // TODO show dialog with PN content construction
    }

    @OnClick(R.id.collapseButtonsButton)
    void collapseButtons() {
        int visibility = viewsToCollapse[0].getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        for (View v : viewsToCollapse)
            v.setVisibility(visibility);
        // TODO out of scope (hide all creator buttons, convert UI into member group UI + expand buttons button)
    }

    @Override
    protected String getTitle() {
        return "";
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_group_creator;
    }

    public void onSuccessSendMessage(SendMessageResult result) {
        if (result.invalid())
            toast(result.error);
        else
            toast(result.result);

        getPresenter().fetchMessages(group.getId());
    }

    public void onErrorSendMessage(Throwable throwable) {
        throwable.printStackTrace();
    }

    public void onSuccessFetchMessages(List<Message> messages) {
        adapter.setMessages(messages);
    }

    public void onErrorFetchMessages(Throwable throwable) {
        throwable.printStackTrace();
    }

    public void onSuccessAddUser(AddUserResult result) {
        if (result.invalid())
            toast(result.error);
        else {
            toast(result.result);
            getPresenter().fetchUsers(group.getId());
        }
    }

    public void onErrorAddUser(Throwable throwable) {
        throwable.printStackTrace();
    }

    public void onSuccessRemoveUser(RemoveUserResult result) {
        if (result.invalid())
            toast(result.error);
        else {
            toast(result.result);
            getPresenter().fetchUsers(group.getId());
        }
    }

    public void onErrorRemoveUser(Throwable throwable) {
        throwable.printStackTrace();
    }

    public void onSuccessFetchUsers(List<ChatUser> users) {
        this.groupUsers = users;
    }

    public void onErrorFetchUsers(Throwable throwable) {
        throwable.printStackTrace();
    }
}