package com.firrael.rx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nucleus.view.NucleusFragment;

/**
 * Created by firrael on 27.05.2016.
 */
public abstract class BaseFragment<P extends BasePresenter> extends NucleusFragment<P> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            getMainActivity().setTitle(title);
        }

        View view = super.onCreateView(inflater, container, savedInstanceState);

        initView(view);

        return view;
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    protected abstract String getTitle();

    protected abstract void initView(View v);
}