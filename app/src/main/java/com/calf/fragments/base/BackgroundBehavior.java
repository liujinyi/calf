package com.calf.fragments.base;

import android.os.Bundle;

import com.calf.frame.log.Logger;

/**
 * Created by JinYi Liu on 16-12-3.
 */

public abstract class BackgroundBehavior<T> implements BaseFragment.Behavior<T> {

    private BackgroundTask mBackgroundTask;

    protected abstract T onBackgroundLoading(Bundle savedInstanceState) throws Exception;

    protected void showStateView(BaseFragment.State state) {
        String message = "";
        switch (state) {
            case LOADING:
                message = "BackgroundBehavior start loading";
                break;
            case FAILURE:
                message = "BackgroundBehavior failure";
                break;
            case NO_NET:
                message = "BackgroundBehavior no net";
                break;
            default:
                break;
        }
        showStateView(state, message);
    }

    protected void showStateView(BaseFragment.State state, String message) {
        mBackgroundTask.getCallback().onState(state, message);
    }

    @Override
    public final void doInBackground(Bundle savedInstanceState, BaseFragment.Callback<T> callback) {
        if (mBackgroundTask != null && !mBackgroundTask.isCancelOrDie()) {
            // is in background loading
            Logger.e(BaseFragment.TAG, "BackgroundBehavior [doInBackground] mBackgroundTask is loading");
        } else {
            mBackgroundTask = new BackgroundTask(savedInstanceState, callback, this);
            new Thread(mBackgroundTask).start();
        }
    }

    private static class BackgroundTask<T> extends BaseFragment.BaseTask {

        private Bundle mSavedInstanceState;
        private BackgroundBehavior<T> mBehavior;
        private BaseFragment.Callback<T> mCallback;

        public BackgroundTask(Bundle savedInstanceState, BaseFragment.Callback<T> callback, BackgroundBehavior<T> behavior) {
            this.mBehavior = behavior;
            this.mCallback = callback;
            this.mSavedInstanceState = savedInstanceState;
        }

        BaseFragment.Callback<T> getCallback() {
            return mCallback;
        }

        @Override
        public void run() {
            try {
                mBehavior.showStateView(BaseFragment.State.LOADING);
                T t = mBehavior.onBackgroundLoading(mSavedInstanceState);
                if (t == null) {
                    mCallback.onState(BaseFragment.State.FAILURE, "onBackgroundLoading can't return null");
                } else {
                    mCallback.onSuccess(mSavedInstanceState, t);
                }
                die();
            } catch (Exception e) {
                Logger.printStackTrace(e);
                mCallback.onState(BaseFragment.State.FAILURE, e.getMessage());
            }
        }
    }

}
