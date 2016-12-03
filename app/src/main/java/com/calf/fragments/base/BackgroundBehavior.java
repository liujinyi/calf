package com.calf.fragments.base;

import android.os.Bundle;
import android.view.ViewGroup;

import com.calf.frame.log.Logger;

/**
 * Created by JinYi Liu on 16-12-3.
 */

public abstract class BackgroundBehavior<T> extends BaseFragment.Behavior {

    public BackgroundBehavior() {
        super(new BaseFragment.BackgroundStateViewFactory());
    }

    @Override
    protected ViewGroup onCreateStateView(int state) {
        switch (state) {
            case BaseFragment.STATE_LOADING:
                return getFactory().onCreateLoadingView(getInflater(), getContainer());
            case BaseFragment.STATE_FAILURE:
                return getFactory().onCreateFailureView(getInflater(), getContainer());
            default:
                return null;
        }
    }

    @Override
    protected void doInBackground(final Bundle savedInstanceState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getCallback().onState(BaseFragment.STATE_LOADING, "start onBackgroundLoading");
                    getCallback().onSuccess(onBackgroundLoading(), savedInstanceState);
                } catch (Exception e) {
                    Logger.printStackTrace(e);
                    getCallback().onState(BaseFragment.STATE_FAILURE, e.getMessage());
                }
            }
        }).start();
    }

    protected abstract T onBackgroundLoading() throws Exception;

}
