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

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    protected abstract String getTitle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            getMainActivity().setTitle(title);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void initView() {
        // TODO
    }
}