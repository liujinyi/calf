package com.calf.fragments.base;

import android.os.Bundle;
import android.text.TextUtils;

import com.calf.frame.log.Logger;
import com.calf.player.manager.NetworkManager;

/**
 * Created by JinYi Liu on 2017/2/8.
 */

public abstract class HttpBehavior<T> implements BaseFragment.Behavior<T>, BaseFragment.IHttpBehavior<T> {

    private static final int DEFAULT_START = 0;
    private static final int DEFAULT_COUNT = 30;

    private HttpTask<T> mHttpTask;

    @Override
    public final void doInBackground(Bundle savedInstanceState, BaseFragment.Callback<T> callback) {
        if (mHttpTask != null && !mHttpTask.isCancelOrDie()) {
            // is in background loading
            Logger.e(BaseFragment.TAG, "HttpBehavior [doInBackground] mHttpTask is loading");
        } else {
            mHttpTask = new HttpTask(this, callback);
            mHttpTask.setSavedInstanceState(savedInstanceState);
            new Thread(mHttpTask).start();
        }
    }

    private static class HttpTask<T> extends BaseFragment.BaseTask {

        private String mUrl;
        private boolean mUseCache;
        private Bundle mSavedInstanceState;
        private BaseFragment.Callback<T> mCallback;
        private BaseFragment.IHttpBehavior<T> mBehavior;

        public HttpTask(BaseFragment.IHttpBehavior<T> behavior, BaseFragment.Callback<T> callback) {
            this.mBehavior = behavior;
            this.mCallback = callback;
            this.mUrl = behavior.giveMeUrl(DEFAULT_START, DEFAULT_COUNT);
        }

        public void setUseCache(boolean useCache) {
            this.mUseCache = useCache;
        }

        public void setSavedInstanceState(Bundle savedInstanceState) {
            this.mSavedInstanceState = savedInstanceState;
        }

        @Override
        public void run() {
            mCallback.onState(BaseFragment.State.LOADING, "HttpTask start loading");
            String cacheData = null;
            if (mUseCache) {
                cacheData = null;
            }
            if (TextUtils.isEmpty(cacheData)) {
                if (NetworkManager.isAvailable()) {

                } else {
                    mCallback.onState(BaseFragment.State.NO_NET, "HttpTask no net");
                }
            } else {

            }
        }

    }


}
