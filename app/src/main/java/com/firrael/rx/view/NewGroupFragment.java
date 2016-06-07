package com.firrael.rx.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.firrael.rx.R;
import com.firrael.rx.model.CreateGroupResult;
import com.firrael.rx.model.Group;
import com.firrael.rx.model.User;
import com.firrael.rx.presenter.NewGroupPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 02.06.2016.
 */
@RequiresPresenter(NewGroupPresenter.class)
public class NewGroupFragment extends BaseFragment<NewGroupPresenter> {

    public static NewGroupFragment newInstance() {

        Bundle args = new Bundle();

        NewGroupFragment fragment = new NewGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.groupNameField)
    EditText groupNameField;
    @BindView(R.id.createGroupButton)
    Button createGroupButton;

    @OnTextChanged(R.id.groupNameField)
    void validate() {
        String text = groupNameField.getText().toString();
        createGroupButton.setEnabled(!TextUtils.isEmpty(text));
    }

    @OnClick(R.id.createGroupButton)
    void create() {
        startLoading();
        User user = User.get(getActivity());
        getPresenter().request(groupNameField.getText().toString(), user.getId());
    }

    public void onSuccess(CreateGroupResult result) {
        stopLoading();

        if (result.invalid()) {
            toast(result.error);
            return;
        }

        Group group = new Group();
        group.setId(result.id);
        group.setTitle(result.title);
        group.setCreator(result.creator);
        group.setImageUrl(result.imageUrl);

        getMainActivity().toGroupCreatorScreen(group);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.createNewGroup);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_new_group;
    }
}
