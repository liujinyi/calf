package com.calf.fragments.base;

import android.os.Bundle;
import android.view.ViewGroup;

/**
 * Created by JinYi Liu on 16-12-3.
 */

public abstract class NetBehavior<T> extends BaseFragment.Behavior {

    private int mCacheMinutes;
    private String mCachePath;
    private BaseFragment.Decoder mDecoder;

    protected NetBehavior() {
        super(new BaseFragment.NetStateViewFactory());
    }

    @Override
    protected ViewGroup onCreateStateView(int state) {
        switch (state) {
            case BaseFragment.STATE_LOADING:
                return getFactory().onCreateLoadingView(getInflater(), getContainer());
            case BaseFragment.STATE_FAILURE:
                return getFactory().onCreateFailureView(getInflater(), getContainer());
            case BaseFragment.STATE_NO_NET:
                return ((BaseFragment.NetStateViewFactory) getFactory()).onCreateNoNetView(getInflater(), getContainer());
            default:
                return null;
        }
    }

    @Override
    protected void doInBackground(Bundle savedInstanceState) {

    }

    protected abstract String giveMeUrl(int start, int count);

    protected abstract T onBackgroundParser(String data);

}
