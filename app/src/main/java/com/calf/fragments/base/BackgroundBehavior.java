package com.calf.fragments.base;

import android.os.Bundle;

import com.calf.frame.log.Logger;
import com.calf.frame.utils.NetworkUtils;

/**
 * Created by JinYi Liu on 16-12-3.
 */

public abstract class BackgroundBehavior<T> extends BaseFragment.Behavior {

    protected boolean mUseNetCallback;
    private BackgroundTask mBackgroundTask;
    private BaseFragment.State mCurrentState;

    protected abstract T onBackgroundLoading() throws Exception;

    public void useNetCallback() {
        this.mUseNetCallback = true;
    }

    protected void handlerStateCallback(BaseFragment.Callback<T> callback) {
        if (mUseNetCallback) {
            if (NetworkUtils.isAvailable(getContainer().getContext())) {
                callback.onState(BaseFragment.State.LOADING, "BackgroundBehavior begin loading");
                mCurrentState = BaseFragment.State.LOADING;
            } else {
                callback.onState(BaseFragment.State.NO_NET, "BackgroundBehavior no net");
                mCurrentState = BaseFragment.State.NO_NET;
            }
        } else {
            callback.onState(BaseFragment.State.LOADING, "BackgroundBehavior begin loading");
            mCurrentState = BaseFragment.State.LOADING;
        }
    }

    @Override
    protected void doInBackground(Bundle savedInstanceState) {
        if (mBackgroundTask != null && mBackgroundTask.isAlive()) {
            // is in background loading
        } else {
            mBackgroundTask = new BackgroundTask(savedInstanceState);
            new Thread(mBackgroundTask).start();
        }
    }

    private class BackgroundTask extends BaseFragment.BaseBehaviorTask {

        private Bundle mSavedInstanceState;

        public BackgroundTask(Bundle savedInstanceState) {
            this.mSavedInstanceState = savedInstanceState;
        }

        @Override
        public void run() {
            try {
                handlerStateCallback(getCallback());
                if (mCurrentState == BaseFragment.State.LOADING) {
                    getCallback().onSuccess(onBackgroundLoading(), mSavedInstanceState);
                }
                die();
            } catch (Exception e) {
                Logger.printStackTrace(e);
                getCallback().onState(BaseFragment.State.FAILURE, e.getMessage());
            }
        }
    }

}
