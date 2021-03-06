package com.firrael.rx.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firrael.rx.GroupsAdapter;
import com.firrael.rx.R;
import com.firrael.rx.model.Group;
import com.firrael.rx.model.GroupFetchResult;
import com.firrael.rx.model.User;
import com.firrael.rx.presenter.MyGroupsPresenter;

import java.util.List;

import butterknife.BindView;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 02.06.2016.
 */
@RequiresPresenter(MyGroupsPresenter.class)
public class MyGroupsFragment extends BaseFragment<MyGroupsPresenter> {

    public static MyGroupsFragment newInstance() {

        Bundle args = new Bundle();

        MyGroupsFragment fragment = new MyGroupsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.groupsList)
    RecyclerView groupsList;

    GroupsAdapter adapter;

    @Override
    protected void initView(View v) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        groupsList.setLayoutManager(manager);
        adapter = new GroupsAdapter();
        groupsList.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if (bundle == null) {
            startLoading();
            User user = User.get(getActivity());
            getPresenter().request(user.getId());
        }
    }

    @Override
    protected String getTitle() {
        return getString(R.string.myGroups);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_my_groups;
    }

    public void onSuccess(GroupFetchResult result) {
        stopLoading();
        if (result.invalid()) {
            toast(result.error);
            return;
        }

        List<Group> groups = result.groups;
        adapter.setGroups(groups);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }
}